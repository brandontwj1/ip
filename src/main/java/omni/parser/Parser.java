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
        assert ui != null : "Ui object cannot be null";
        assert tasks != null : "TaskList object cannot be null";
        assert storage != null : "Storage object cannot be null";
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
        int num;
        try {
            num = parseInt(n);
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("Invalid mark command. Try again.");
        }

        if (num > tasks.getSize()) {
            throw new InvalidArgumentException("That task does not exist! Try again!");
        } else {
            Task t = tasks.markTaskDone(num - 1);
            storage.rewriteTask(t, num - 1);
            return ui.showMarked(t);
        }
    }

    /**
     * Marks a task as not done based on the given task number.
     *
     * @param n The task number as a string.
     * @throws InvalidArgumentException If the task number is invalid or task doesn't exist.
     * @throws IOException              If an I/O error occurs during storage update.
     */
    private String handleUnmark(String n) throws InvalidArgumentException, IOException {
        int num;
        try {
            num = parseInt(n);
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("Invalid mark command. Try again.");
        }

        if (num > tasks.getSize()) {
            throw new InvalidArgumentException("That task does not exist! Try again!");
        } else {
            Task t = tasks.unmarkTaskDone(num - 1);
            storage.rewriteTask(t, num - 1);
            return ui.showUnmarked(t);
        }
    }

    /**
     * Handles unknown commands by throwing an exception.
     *
     * @throws UnknownCommandException If unknown command received.
     */
    private void handleUnknownCmd() throws UnknownCommandException {
        throw new UnknownCommandException("I can't lie I have no idea what that means...");
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
     * Checks if the given date string is valid according to the expected format (DD-MM-YYYY HHMM).
     *
     * @param date The date string to validate.
     * @return True if the date string is valid.
     * @throws InvalidArgumentException If the date format is invalid.
     */
    boolean checkValidDateString(String date) throws InvalidArgumentException {
        String[] dateAndTime = date.split(" ");
        if (dateAndTime.length > 2) {
            throw new InvalidArgumentException("Invalid date format! Check your date and time is in the form"
                    + " DD-MM-YYYY HHMM");
        }
        LocalDate d;
        LocalTime t;
        try {
            String dateStr = dateAndTime[0].trim();
            d = LocalDate.parse(dateStr, DATE_FORMATTER);
            if (dateAndTime.length > 1) {
                t = LocalTime.parse(dateAndTime[1].trim(), TIME_FORMATTER);
            }
        } catch (DateTimeParseException e) {
            throw new InvalidArgumentException("Invalid date format! Check your date and time is in the form"
                    + " DD-MM-YYYY HHMM");
        }
        return true;
    }

    /**
     * Creates and adds a new deadline task.
     *
     * @param arg The deadline argument containing description and due date.
     * @throws InvalidArgumentException If the format is invalid or description is empty.
     * @throws IOException              If an I/O error occurs during storage write.
     */
    private String handleDeadline(String arg) throws InvalidArgumentException, IOException {
        String[] parts = arg.split("/by", 2);
        if (parts.length < 2) {
            throw new InvalidArgumentException("Unable to set deadline, remember to use /by to specify your deadline!");
        } else {
            String description = parts[0].trim();
            if (description.isEmpty()) {
                throw new InvalidArgumentException("Give your deadline a description!");
            }
            String date = parts[1].trim();
            checkValidDateString(date);
            Deadline newDeadline = new Deadline(description, false, date);
            return handleAddTask(newDeadline);
        }
    }

    /**
     * Creates and adds a new event task.
     *
     * @param arg The event argument containing description, start and end times.
     * @throws InvalidArgumentException If the format is invalid or description is empty.
     * @throws IOException              If an I/O error occurs during storage write.
     */
    private String handleEvent(String arg) throws InvalidArgumentException, IOException {
        String[] parts = arg.split("/from", 2);
        if (parts.length < 2) {
            throw new InvalidArgumentException("Unable to set event, "
                    + "remember to use /from and /to in that order!");
        } else {
            String description = parts[0].trim();
            if (description.isEmpty()) {
                throw new InvalidArgumentException("Give your event a description!");
            }
            String[] dates = parts[1].trim().split("/to", 2);
            if (dates.length < 2) {
                throw new InvalidArgumentException("Unable to set event, remember to use /from and /to in that order!");
            } else {
                String from = dates[0].trim();
                String to = dates[1].trim();
                checkValidDateString(from);
                checkValidDateString(to);
                Event newEvent = new Event(description, false, from, to);
                return handleAddTask(newEvent);
            }
        }
    }

    /**
     * Deletes a task based on the given task index.
     *
     * @param index The task number as a string.
     * @throws InvalidArgumentException If the task number is invalid or task doesn't exist.
     * @throws IOException              If an I/O error occurs during storage update.
     */
    private String handleDelete(String index) throws InvalidArgumentException, IOException {
        int num;
        try {
            num = parseInt(index);
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("Invalid delete command. Try again.");
        }

        if (num > tasks.getSize()) {
            throw new InvalidArgumentException("That task does not exist! Try again!");
        } else {
            Task removedTask = tasks.removeTask(num - 1);
            storage.eraseTask(num - 1);
            return ui.showErased(removedTask);
        }
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
        assert input != null : "input cannot be null";
        String[] parts = input.split("\\s+", 2);
        String cmd = parts[0];
        String arg = parts.length > 1 ? parts[1] : "";
        String reply = "";

        boolean continueExecution = true;
        try {
            switch (cmd.toLowerCase()) {
            case "list":
                reply = handleList();
                break;
            case "mark":
                reply = handleMark(arg);
                break;
            case "unmark":
                reply = handleUnmark(arg);
                break;
            case "todo":
                reply = handleTodo(arg);
                break;
            case "deadline":
                reply = handleDeadline(arg);
                break;
            case "event":
                reply = handleEvent(arg);
                break;
            case "delete":
                reply = handleDelete(arg);
                break;
            case "find":
                reply = handleFind(arg);
                break;
            case "bye":
                continueExecution = false;
                reply = ui.exit();
                break;
            default:
                handleUnknownCmd();
            }
        } catch (OmniException e) {
            reply = e.getUserMessage();
        } catch (IOException e) {
            reply = e.getMessage();
        }

        assert !reply.isEmpty() : "Reply cannot be empty";
        return reply;
    }
}
