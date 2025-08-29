import exceptions.*;

import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

import static java.lang.Integer.parseInt;


public class Omni {
    private static final String HORIZONTAL_LINE = "   _________________________________________________________\n";
    private static final String INDENT = "    ";
    private static final ArrayList<Task> tasks = new ArrayList<>();
    private static final Path PATH_TASKLIST = Paths.get("data", "tasks.txt");

    private static void greet() {
        System.out.println(
            HORIZONTAL_LINE +
            INDENT + "Helloo! I'm Omni!\n" +
            INDENT + "What can I do for you?\n" +
            HORIZONTAL_LINE
        );
    }

    private static void exit() {
        System.out.println(
            HORIZONTAL_LINE +
            INDENT + "Byeee! See you in a bit!\n" +
            HORIZONTAL_LINE
        );
    }

    /*
    private static void echo(String input) {
        System.out.println(
            HORIZONTAL_LINE +
            INDENT + input + "\n" +
            HORIZONTAL_LINE
        );
    }
    */

    private static void printErrorMessage() throws OmniException {
        System.out.println(
            HORIZONTAL_LINE +
            INDENT + "My bad, something went wrong. Try again!\n" +
            HORIZONTAL_LINE
        );
        throw new OmniException("My bad, something went wrong. Try again!");
    }


    private static void handleList() {
        if (tasks.isEmpty()) {
            System.out.println(INDENT + "You have no tasks... Add one!");
            return;
        }
        System.out.print(INDENT + "Here are the tasks you've added:\n");
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            System.out.printf(INDENT + "%d.%s\n", i+1, t);
        }
    }

    private static void markData(Task task, int index, boolean mark) throws IOException {
        List<String> lines = Files.readAllLines(PATH_TASKLIST);
        String newEntry = task.getEntryString();
    }


    private static void handleMark(String n) throws InvalidArgumentException, IOException {
        int num;
        try {
            num = parseInt(n);
        } catch (NumberFormatException e) {
            System.out.println(INDENT + "Invalid mark command. Try again.");
            return;
        }

        if (num > tasks.size()) {
            throw new InvalidArgumentException("That task does not exist! Try again!");
        } else {
            Task t = tasks.get(num-1);
            t.markDone();
            markData(t,num-1, true);
            System.out.println(
                INDENT + "Congrats! I've marked this task as done:\n" +
                INDENT + "  " + tasks.get(num-1)
            );
        }
    }


    private static void handleUnmark(String n) throws InvalidArgumentException, IOException {
        int num;
        try {
            num = parseInt(n);
        } catch (NumberFormatException e) {
            System.out.println(INDENT + "Invalid unmark command. Try again.");
            return;
        }

        if (num > tasks.size()) {
            throw new InvalidArgumentException("That task does not exist! Try again!");
        } else {
            Task t = tasks.get(num-1);
            t.unmarkDone();
            markData(t,num-1, false);
            System.out.println(
                INDENT + "Sure thing, I've marked this task as not done yet:\n" +
                INDENT + "  " + tasks.get(num-1)
            );
        }
    }

    private static void handleUnknownCmd() throws UnknownCommandException, IOException {
        throw new UnknownCommandException("I can't lie I have no idea what that means...");
    }

    private static void addTask(Task task) throws IOException {
        Files.writeString(PATH_TASKLIST, task.getEntryString(), StandardOpenOption.APPEND);
        tasks.add(task);
        String taskStr = tasks.size() == 1 ? "task" : "tasks";
        System.out.println(
            INDENT + "Got it. I've added this task:\n" +
            INDENT + "  " + task + "\n" +
            INDENT + "Now you have " + tasks.size() + " " + taskStr + " in the list."
        );
    }

    private static void handleTodo(String arg) throws InvalidArgumentException, IOException {
        if (arg.isEmpty()) {
            throw new InvalidArgumentException("Give your todo a description!");
        }

        Todo newTodo = new Todo(arg, false);
        addTask(newTodo);
    }

    private static void handleDeadline(String arg) throws InvalidArgumentException, IOException {
        String[] parts = arg.split("/by", 2);
        if (parts.length < 2) {
            throw new InvalidArgumentException("Unable to set deadline, remember to use /by to specify your deadline!");
        } else {
            String description = parts[0].trim();
            if (description.isEmpty()) {
                throw new InvalidArgumentException("Give your deadline a description!");
            }
            String date = parts[1].trim();
            Deadline newDeadline = new Deadline(description, false, date);
            addTask(newDeadline);
        }
    }

    private static void handleEvent(String arg) throws InvalidArgumentException, IOException {
        String[] parts = arg.split("/from", 2);
        if (parts.length < 2) {
            throw new InvalidArgumentException("Unable to set Event, remember to use /from and /to in that order!");
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
                Event newEvent = new Event(description, false, from, to);
                addTask(newEvent);
            }
        }
    }

    private static void handleDelete(String n) throws InvalidArgumentException {
        int num;
        try {
            num = parseInt(n);
        } catch (NumberFormatException e) {
            System.out.println(INDENT + "Invalid delete command. Try again.");
            return;
        }

        if (num > tasks.size()) {
            throw new InvalidArgumentException("That task does not exist! Try again!");
        } else {
            Task removedTask = tasks.remove(num-1);
            System.out.println(
                INDENT + "Gotchu, I've deleted this task for you:\n" +
                INDENT + "  " + removedTask
            );
        }
    }

    private static boolean initTaskList() throws CorruptedFileException {
        Path taskListPath = Paths.get("data", "tasks.txt");
        if (!Files.exists(taskListPath)) {
            try {
                Files.createDirectories(taskListPath.getParent());
                Files.createFile(taskListPath);
            } catch (IOException e) {
                System.out.println("Error creating tasks.txt file: " + e.getMessage());
                return false;
            }
        }

        try {
            List<String> lines = Files.readAllLines(taskListPath);
            for (String line : lines) {
                String[] values = line.split("\\|");
                if (values.length < 3 || values.length > 5) {
                    throw new CorruptedFileException("Entry length invalid.");
                } else {
                    String type = values[0].trim();
                    String desc = values[1].trim();
                    boolean isDone = parseInt(values[2].trim()) != 0;
                    switch (type) {
                        case "T":
                            if (values.length != 3) {
                                throw new CorruptedFileException("Entry length for todo invalid.");
                            }
                            tasks.add(new Todo(desc, isDone));
                            break;
                        case "D":
                            if (values.length != 4) {
                                throw new CorruptedFileException("Entry length for deadline invalid.");
                            }
                            tasks.add(new Deadline(desc, isDone, values[3].trim()));
                            break;
                        case "E":
                            if (values.length != 5) {
                                throw new CorruptedFileException("Entry length for event invalid.");
                            }
                            tasks.add(new Event(desc, isDone, values[3].trim(), values[4].trim()));
                            break;
                        default:
                            throw new CorruptedFileException("Task type not found.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error creating tasks.txt file: " + e.getMessage());
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        Omni.greet();
        boolean initTest = false;
        try {
            initTest = Omni.initTaskList();
            if (!initTest) {
                System.out.println("Error initialising task list");
                return;
            }
        } catch (OmniException e) {
            System.out.println(INDENT + e.getUserMessage());
            return;
        }

        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();
        String[] parts = input.split("\\s+", 2);
        String cmd = parts[0];
        String arg = parts.length > 1 ? parts[1] : "";

        while (!cmd.equals("bye")) {
            System.out.print(HORIZONTAL_LINE);
            try {
                switch (cmd.toLowerCase()) {
                case "list":
                    Omni.handleList();
                    break;
                case "mark":
                    Omni.handleMark(arg);
                    break;
                case "unmark":
                    Omni.handleUnmark(arg);
                    break;
                case "todo":
                    Omni.handleTodo(arg);
                    break;
                case "deadline":
                    Omni.handleDeadline(arg);
                    break;
                case "event":
                    Omni.handleEvent(arg);
                    break;
                case "delete":
                    Omni.handleDelete(arg);
                    break;
                default:
                    Omni.handleUnknownCmd();
                }
            } catch (OmniException e) {
                System.out.println(INDENT + e.getUserMessage());
            } catch (IOException e) {
                System.out.println(INDENT + e.getMessage());
            }
            System.out.println(HORIZONTAL_LINE);

            input = sc.nextLine().trim();
            parts = input.split("\\s+", 2);
            cmd = parts[0];
            arg = parts.length > 1 ? parts[1] : "";
        }
        Omni.exit();
    }
}
