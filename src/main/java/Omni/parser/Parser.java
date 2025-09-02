package Omni.parser;

import static java.lang.Integer.parseInt;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import Omni.exceptions.InvalidArgumentException;
import Omni.exceptions.OmniException;
import Omni.exceptions.UnknownCommandException;
import Omni.ui.Ui;
import Omni.tasklist.TaskList;
import Omni.storage.Storage;
import Omni.tasks.Task;
import Omni.tasks.Todo;
import Omni.tasks.Deadline;
import Omni.tasks.Event;

public class Parser {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmm");

    private Ui ui;
    private TaskList tasks;
    private Storage storage;

    public Parser(Ui ui, TaskList tasks, Storage storage) {
        this.ui = ui;
        this.tasks = tasks;
        this.storage = storage;
    }

    private void handleList() {
        ui.showTasks(tasks);
    }

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

    private void handleUnknownCmd() throws UnknownCommandException, IOException {
        throw new UnknownCommandException("I can't lie I have no idea what that means...");
    }

    private void handleAddTask(Task task) throws IOException {
        storage.writeTask(task);
        Task t = tasks.addTask(task);
        ui.showAdded(t, tasks);
    }

    private void handleTodo(String arg) throws InvalidArgumentException, IOException {
        if (arg.isEmpty()) {
            throw new InvalidArgumentException("Give your todo a description!");
        }

        Todo newTodo = new Todo(arg, false);
        handleAddTask(newTodo);
    }

    boolean checkValidDateString(String date) throws InvalidArgumentException {
        String[] dateAndTime = date.split(" ");
        if (dateAndTime.length > 2) {
            throw new InvalidArgumentException("Invalid date format! Check your date and time is in the form" +
                    " DD-MM-YYYY HHMM");
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
            throw new InvalidArgumentException("Invalid date format! Check your date and time is in the form" +
                    " DD-MM-YYYY HHMM");
        }
        return true;
    }

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

    private void handleDelete(String n) throws InvalidArgumentException, IOException {
        int num;
        try {
            num = parseInt(n);
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

    public boolean handleInput(String input) {
        String[] parts = input.split("\\s+", 2);
        String cmd = parts[0];
        String arg = parts.length > 1 ? parts[1] : "";

        boolean cont = true;
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
                cont = false;
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
        return cont;
    }
}
