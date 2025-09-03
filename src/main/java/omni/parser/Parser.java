package omni.parser;

import static java.lang.Integer.parseInt;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import omni.exceptions.InvalidArgumentException;
import omni.exceptions.OmniException;
import omni.exceptions.UnknownCommandException;
import omni.ui.Ui;
import omni.tasklist.TaskList;
import omni.storage.Storage;
import omni.tasks.Task;
import omni.tasks.Todo;
import omni.tasks.Deadline;
import omni.tasks.Event;

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
     * @param ui The UI object.
     * @param tasks The TaskList object.
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
    private void handleList() {
        ui.showTasks(tasks);
    }

    /**
     * Marks a task as done based on the given task number.
     *
     * @param n The task number as a string.
     * @throws InvalidArgumentException If the task number is invalid or task doesn't exist.
     * @throws IOException If an I/O error occurs during storage update.
     */
    private void handleMark(String n) throws InvalidArgumentException, IOException {
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
            ui.showMarked(t);
        }
    }

    /**
     * Marks a task as not done based on the given task number.
     *
     * @param n The task number as a string.
     * @throws InvalidArgumentException If the task number is invalid or task doesn't exist.
     * @throws IOException If an I/O error occurs during storage update.
     */
    private void handleUnmark(String n) throws InvalidArgumentException, IOException {
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
            ui.showUnmarked(t);
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
    private void handleAddTask(Task task) throws IOException {
        storage.writeTask(task);
        Task t = tasks.addTask(task);
        ui.showAdded(t, tasks);
    }

    /**
     * Creates and adds a new todo task.
     *
     * @param arg The todo description.
     * @throws InvalidArgumentException If the description is empty.
     * @throws IOException If an I/O error occurs during storage write.
     */
    private void handleTodo(String arg) throws InvalidArgumentException, IOException {
        if (arg.isEmpty()) {
            throw new InvalidArgumentException("Give your todo a description!");
        }

        Todo newTodo = new Todo(arg, false);
        handleAddTask(newTodo);
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
     * @throws IOException If an I/O error occurs during storage write.
     */
    private void handleDeadline(String arg) throws InvalidArgumentException, IOException {
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
            handleAddTask(newDeadline);
        }
    }

    /**
     * Creates and adds a new event task.
     *
     * @param arg The event argument containing description, start and end times.
     * @throws InvalidArgumentException If the format is invalid or description is empty.
     * @throws IOException If an I/O error occurs during storage write.
     */
    private void handleEvent(String arg) throws InvalidArgumentException, IOException {
        String[] parts = arg.split("/from", 2);
        if (parts.length < 2) {
            throw new InvalidArgumentException("Unable to set Omni.tasks.Event, remember to use /from and /to in that order!");
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
                handleAddTask(newEvent);
            }
        }
    }

    /**
     * Deletes a task based on the given task number.
     *
     * @param index The task number as a string.
     * @throws InvalidArgumentException If the task number is invalid or task doesn't exist.
     * @throws IOException If an I/O error occurs during storage update.
     */
    private void handleDelete(String index) throws InvalidArgumentException, IOException {
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
            ui.showErased(removedTask);
        }
    }

    /**
     * Handles user input and executes the corresponding command.
     *
     * @param input The user input string.
     * @return True to continue, false to exit.
     */
    public boolean handleInput(String input) {
        String[] parts = input.split("\\s+", 2);
        String cmd = parts[0];
        String arg = parts.length > 1 ? parts[1] : "";

        boolean continueExecution = true;
        ui.startReply();
        try {
            switch (cmd.toLowerCase()) {
            case "list":
                handleList();
                break;
            case "mark":
                handleMark(arg);
                break;
            case "unmark":
                handleUnmark(arg);
                break;
            case "todo":
                handleTodo(arg);
                break;
            case "deadline":
                handleDeadline(arg);
                break;
            case "event":
                handleEvent(arg);
                break;
            case "delete":
                handleDelete(arg);
                break;
            case "bye":
                continueExecution = false;
                ui.exit();
                break;
            default:
                handleUnknownCmd();
            }
        } catch (OmniException e) {
            ui.showError(e.getUserMessage());
        } catch (IOException e) {
            ui.showError(e.getMessage());
        }
        ui.endReply();
        return continueExecution;
    }
}
