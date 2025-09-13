package omni.parser;

import static java.lang.Integer.parseInt;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import omni.exceptions.InvalidArgumentException;
import omni.exceptions.OmniException;
import omni.exceptions.UnknownCommandException;
import omni.storage.Storage;
import omni.tasklist.TaskList;
import omni.tasks.Deadline;
import omni.tasks.Event;
import omni.tasks.Task;
import omni.tasks.Todo;
import omni.ui.Ui;

/**
 * The Parser class is responsible for parsing and executing user commands in the Omni task management system.
 * It handles various commands such as adding tasks (todo, deadline, event), marking/unmarking tasks,
 * deleting tasks, and listing all tasks. The parser validates input formats and coordinates between
 * the UI, TaskList, and Storage components.
 *
 * @author Brandon Tan
 */
public class Parser {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmm");

    private static final String MESSAGE_INVALID_DATE = "Invalid date format! Check your date and time is in the form"
            + " DD-MM-YYYY HHMM";
    private static final String MESSAGE_INVALID_EVENT_FORMAT = "Unable to set event,"
            + " remember to use /from and /to in that order!";
    private static final String MESSAGE_INVALID_DEADLINE_FORMAT = "Unable to set deadline,"
            + " remember to use /by to specify your deadline!";
    private static final String MESSAGE_INVALID_DELETE_COMMAND = "Invalid delete command. Try again.";
    private static final String MESSAGE_INVALID_MARK_COMMAND = "Invalid mark command. Try again.";
    private static final String MESSAGE_INVALID_UNMARK_COMMAND = "Invalid unmark command. Try again.";

    private Ui ui;
    private TaskList tasks;
    private Storage storage;

    /**
     * Constructs a Parser with the specified UI, TaskList, and Storage objects.
     *
     * @param ui      The UI object.
     * @param tasks   The TaskList object.
     * @param storage The Storage object.
     */
    public Parser(Ui ui, TaskList tasks, Storage storage) {
        this.ui = ui;
        this.tasks = tasks;
        this.storage = storage;
    }

    /**
     * Displays the list of all tasks to the user.
     */
    private String handleList() {
        return ui.showTasks(tasks);
    }

    /**
     * Marks a task as done based on the given task number.
     *
     * @param n The task number as a string.
     * @return Reply string for the user.
     * @throws InvalidArgumentException If the task number is invalid or task doesn't exist.
     * @throws IOException              If an I/O error occurs during storage update.
     */
    private String handleMark(String n) throws InvalidArgumentException, IOException {
        int num = getIndexFromString(n, MESSAGE_INVALID_MARK_COMMAND);
        Task markedTask = tasks.markTaskDone(num);
        storage.rewriteTask(markedTask, num);
        return ui.showMarked(markedTask);
    }

    /**
     * Marks a task as not done based on the given task number.
     *
     * @param n The task number as a string.
     * @throws InvalidArgumentException If the task number is invalid or task doesn't exist.
     * @throws IOException              If an I/O error occurs during storage update.
     */
    private String handleUnmark(String n) throws InvalidArgumentException, IOException {
        int num = getIndexFromString(n, MESSAGE_INVALID_UNMARK_COMMAND);
        Task unmarkedTask = tasks.unmarkTaskDone(num);
        storage.rewriteTask(unmarkedTask, num);
        return ui.showUnmarked(unmarkedTask);
    }

    /**
     * Deletes a task based on the given task index.
     *
     * @param n The task number as a string.
     * @throws InvalidArgumentException If the task number is invalid or task doesn't exist.
     * @throws IOException              If an I/O error occurs during storage update.
     */
    private String handleDelete(String n) throws InvalidArgumentException, IOException {
        int num = getIndexFromString(n, MESSAGE_INVALID_DELETE_COMMAND);
        Task removedTask = tasks.removeTask(num);
        storage.eraseTask(num);
        return ui.showErased(removedTask);
    }

    private int getIndexFromString(String n, String invalidCommandMessage) throws InvalidArgumentException {
        try {
            int index = parseInt(n);
            if (index > tasks.getSize() || index < 1) {
                throw new InvalidArgumentException("That task does not exist! Try again!");
            }
            return index - 1; // Correct to 0-indexed for parsing
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException(invalidCommandMessage);
        }
    }

    /**
     * Handles unknown commands by throwing an exception.
     *
     * @throws UnknownCommandException If unknown command received.
     */
    private void handleUnknownCmd() throws UnknownCommandException {
        throw new UnknownCommandException(ui.showUnknownCommandError());
    }

    /**
     * Adds a task to the task list and storage, then displays confirmation.
     *
     * @param task The task to add.
     * @throws IOException If an I/O error occurs during storage write.
     */
    private String handleAddTask(Task task) throws IOException {
        storage.writeTask(task);
        Task t = tasks.addTask(task);
        return ui.showAdded(t, tasks);
    }

    /**
     * Creates and adds a new todo task.
     *
     * @param arg The todo description.
     * @throws InvalidArgumentException If the description is empty.
     * @throws IOException              If an I/O error occurs during storage write.
     */
    private String handleTodo(String arg) throws InvalidArgumentException, IOException {
        if (arg.isEmpty()) {
            throw new InvalidArgumentException("Give your todo a description!");
        }

        Todo newTodo = new Todo(arg, false);
        return handleAddTask(newTodo);
    }

    /**
     * Checks if the date string(s) is valid according to the expected format (DD-MM-YYYY HHMM).
     *
     * @param firstDate The first date string to validate.
     * @param additionalDates Additional date strings to validate.
     * @return True if the date strings are all valid.
     * @throws InvalidArgumentException If the date format of any date string is invalid.
     */
    public static boolean checkValidDateString(String firstDate, String... additionalDates) throws InvalidArgumentException {
        validateSingleDate(firstDate);
        for (String date : additionalDates) {
            validateSingleDate(date);
        }
        return true;
    }

    private static void validateSingleDate(String date) throws InvalidArgumentException {
        parseDateFromDateTime(date);
        parseTimeFromDateTime(date);
    }

    private static void validateDateFormat(String date) throws InvalidArgumentException {
        String[] dateAndTime = date.split(" ");
        if (dateAndTime.length > 2) {
            throw new InvalidArgumentException(MESSAGE_INVALID_DATE);
        }
    }

    private static LocalDate parseDate(String dateStr) throws InvalidArgumentException {
        try {
            return LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidArgumentException(MESSAGE_INVALID_DATE);
        }
    }

    private static LocalTime parseTime(String timeStr) throws InvalidArgumentException {
        try {
            return LocalTime.parse(timeStr.trim(), TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidArgumentException(MESSAGE_INVALID_DATE);
        }
    }

    /**
     * Parses and returns the {@link LocalDate} from a date-time string.
     * The expected format is "DD-MM-YYYY HHMM".
     *
     * @param date The date-time string to parse.
     * @return The parsed {@link LocalDate}.
     * @throws InvalidArgumentException If the date format is invalid.
     */
    public static LocalDate parseDateFromDateTime(String date) throws InvalidArgumentException {
        validateDateFormat(date);
        String dateStr = date.split(" ")[0].trim();
        return parseDate(dateStr);
    }

    /**
     * Parses and returns the {@link LocalTime} from a date-time string.
     * The expected format is "DD-MM-YYYY HHMM".
     *
     * @param date The date-time string to parse.
     * @return The parsed {@link LocalTime}, or null if no time is present.
     * @throws InvalidArgumentException If the time format is invalid.
     */
    public static LocalTime parseTimeFromDateTime(String date) throws InvalidArgumentException {
        validateDateFormat(date);
        String[] dateAndTime = date.split(" ");
        if (dateAndTime.length <= 1) {
            return null;
        }
        return parseTime(dateAndTime[1]);
    }

    /**
     * Creates and adds a new deadline task.
     *
     * @param arg The deadline argument containing description and due date.
     * @throws InvalidArgumentException If the format is invalid or description is empty.
     * @throws IOException              If an I/O error occurs during storage write.
     */
    private String handleDeadline(String arg) throws InvalidArgumentException, IOException {
        String[] parts = getDeadlineParts(arg);
        String description = getDeadlineDescription(parts);
        String date = parts[1].trim();
        checkValidDateString(date);

        Deadline newDeadline = new Deadline(description, false, date);
        return handleAddTask(newDeadline);
    }

    private static String getDeadlineDescription(String[] parts) throws InvalidArgumentException {
        String description = parts[0].trim();
        if (description.isEmpty()) {
            throw new InvalidArgumentException("Give your deadline a description!");
        }
        return description;
    }

    private static String[] getDeadlineParts(String arg) throws InvalidArgumentException {
        String[] parts = arg.split("/by", 2);
        if (parts.length < 2) {
            throw new InvalidArgumentException(MESSAGE_INVALID_DEADLINE_FORMAT);
        }
        return parts;
    }

    /**
     * Creates and adds a new event task.
     *
     * @param arg The event argument containing description, start and end times.
     * @throws InvalidArgumentException If the format is invalid or description is empty.
     * @throws IOException              If an I/O error occurs during storage write.
     */
    private String handleEvent(String arg) throws InvalidArgumentException, IOException {
        String[] parts = getEventParts(arg);
        String description = getEventDescription(parts);
        String[] dates = getDates(parts);
        String from = dates[0].trim();
        String to = dates[1].trim();
        checkValidDateString(from, to);

        Event newEvent = new Event(description, false, from, to);
        return handleAddTask(newEvent);
    }

    private static String[] getDates(String[] parts) throws InvalidArgumentException {
        String[] dates = parts[1].trim().split("/to", 2);
        if (dates.length < 2) {
            throw new InvalidArgumentException(MESSAGE_INVALID_EVENT_FORMAT);
        }
        return dates;
    }

    private static String getEventDescription(String[] parts) throws InvalidArgumentException {
        String description = parts[0].trim();
        if (description.isEmpty()) {
            throw new InvalidArgumentException("Give your event a description!");
        }
        return description;
    }

    private static String[] getEventParts(String arg) throws InvalidArgumentException {
        String[] parts = arg.split("/from", 2);
        if (parts.length < 2) {
            throw new InvalidArgumentException(MESSAGE_INVALID_EVENT_FORMAT);
        }
        return parts;
    }

    /**
     * Finds tasks containing the keyword.
     *
     * @param keyword The user input keyword.
     */
    private String handleFind(String keyword) {
        ArrayList<Task> matchingTasks = tasks.findMatchingTasks(keyword);
        return ui.showMatchingTasks(matchingTasks);
    }

    /**
     * Handles user input and executes the corresponding command.
     *
     * @param input The user input string.
     * @return Output string reply.
     */
    public String handleInput(String input) {
        String[] parts = input.split("\\s+", 2);
        String cmd = parts[0];
        String arg = parts.length > 1 ? parts[1] : "";
        String reply = handleCommand(cmd, arg);

        reply = handleCommand(cmd, arg);
        return reply;
    }

    private String handleCommand(String cmd, String arg) {
        try {
            return switch (cmd.toLowerCase()) {
            case "list" -> handleList();
            case "mark" -> handleMark(arg);
            case "unmark" -> handleUnmark(arg);
            case "todo" -> handleTodo(arg);
            case "deadline" -> handleDeadline(arg);
            case "event" -> handleEvent(arg);
            case "delete" -> handleDelete(arg);
            case "find" -> handleFind(arg);
            case "bye" -> ui.exit();
            default -> ui.showUnknownCommandError();
            };
        } catch (OmniException e) {
            return e.getUserMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
