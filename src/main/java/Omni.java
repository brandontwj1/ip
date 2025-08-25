import exceptions.*;

import java.util.Scanner;
import java.util.regex.*;

public class Omni {
    private static final String HORIZONTAL_LINE = "   _________________________________________________________\n";
    private static final String INDENT = "    ";
    private static Task[] tasks = new Task[100];
    private static int taskCount = 0;

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

    private static void addTask(String input) {
        tasks[taskCount] = new Task(input);
        taskCount++;
        System.out.println(
            HORIZONTAL_LINE +
            INDENT + "Added: " + input + "\n" +
            HORIZONTAL_LINE
        );
    }

    private static void handleList() {
        if (taskCount == 0) {
            System.out.println(INDENT + "You have no tasks... Add one!");
            return;
        }
        System.out.print(INDENT + "Here are the tasks you've added:\n");
        for (int i = 0; i < taskCount; i++) {
            Task t = tasks[i];
            System.out.printf(INDENT + "%d.%s\n", i+1, t);
        }
    }


    private static void handleMark(String n) throws InvalidArgumentException {
        int num;
        try {
            num = Integer.parseInt(n);
        } catch (NumberFormatException e) {
            System.out.println(INDENT + "Invalid mark command. Try again.");
            return;
        }

        if (num > taskCount) {
            throw new InvalidArgumentException("That task does not exist! Try again!");
        } else {
            tasks[num-1].markDone();
            System.out.println(
                INDENT + "Congrats! I've marked this task as done:\n" +
                INDENT + "  " + tasks[num-1]
            );
        }
    }


    private static void handleUnmark(String n) throws InvalidArgumentException {
        int num;
        try {
            num = Integer.parseInt(n);
        } catch (NumberFormatException e) {
            System.out.println(INDENT + "Invalid unmark command. Try again.");
            return;
        }

        if (num > taskCount) {
            throw new InvalidArgumentException("That task does not exist! Try again!");
        } else {
            tasks[num-1].unmarkDone();
            System.out.println(
                INDENT + "Sure thing, I've marked this task as not done yet:\n" +
                INDENT + "  " + tasks[num-1]
            );
        }
    }

    private static void handleUnknownCmd() throws UnknownCommandException {
        throw new UnknownCommandException("I can't lie I have no idea what that means...");
    }

    private static void handleTodo(String arg) throws InvalidArgumentException {
        if (arg.isEmpty()) {
            throw new InvalidArgumentException("Give your todo a description!");
        }
        Todo newTodo = new Todo(arg);
        tasks[taskCount] = newTodo;
        taskCount++;
        String taskStr = taskCount == 1 ? "task" : "tasks";
        System.out.println(
            INDENT + "Got it. I've added this task:\n" +
            INDENT + "  " + newTodo + "\n" +
            INDENT + "Now you have " + taskCount + " " + taskStr + " in the list."
        );
    }

    private static void handleDeadline(String arg) throws InvalidArgumentException {
        String[] parts = arg.split("/by", 2);
        if (parts.length < 2) {
            throw new InvalidArgumentException("Unable to set deadline, remember to use /by to specify your deadline!");
        } else {
            String description = parts[0].trim();
            if (description.isEmpty()) {
                throw new InvalidArgumentException("Give your deadline a description!");
            }
            String date = parts[1].trim();
            Deadline newDeadline = new Deadline(description, date);
            tasks[taskCount] = newDeadline;
            taskCount++;
            String taskStr = taskCount == 1 ? "task" : "tasks";
            System.out.println(
                INDENT + "Got it. I've added this task:\n" +
                INDENT + "  " + newDeadline + "\n" +
                INDENT + "Now you have " + taskCount + " " + taskStr + " in the list."
            );
        }
    }

    private static void handleEvent(String arg) throws InvalidArgumentException {
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
                Event newEvent = new Event(description, dates[0].trim(), dates[1].trim());
                tasks[taskCount] = newEvent;
                taskCount++;
                String taskStr = taskCount == 1 ? "task" : "tasks";
                System.out.println(
                    INDENT + "Got it. I've added this task:\n" +
                    INDENT + "  " + newEvent + "\n" +
                    INDENT + "Now you have " + taskCount + " " + taskStr + " in the list."
                );
            }
        }
    }

    public static void main(String[] args) {
        Omni.greet();
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
                    default:
                        Omni.handleUnknownCmd();
                }
            } catch (OmniException e) {
                System.out.println(INDENT + e.getUserMessage());
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
