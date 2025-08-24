import org.w3c.dom.html.HTMLObjectElement;

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

    private static void echo(String input) {
        System.out.println(
            HORIZONTAL_LINE +
            INDENT + input + "\n" +
            HORIZONTAL_LINE
        );
    }

    private static void printErrorMessage() {
        System.out.println(
            HORIZONTAL_LINE +
            INDENT + "My bad something went wrong. Try again!\n" +
            HORIZONTAL_LINE
        );
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
        System.out.print(HORIZONTAL_LINE + INDENT + "Here are the tasks you've added:\n");
        for (int i = 0; i < taskCount; i++) {
            Task t = tasks[i];
            System.out.printf(INDENT + "%d.%s\n", i+1, t);
        }
        System.out.print(HORIZONTAL_LINE);
    }


    private static void handleMark(String n) {
        System.out.println(HORIZONTAL_LINE);
        int num;
        try {
            num = Integer.parseInt(n);
        } catch (NumberFormatException e) {
            System.out.println(INDENT + "Invalid mark command. Try again.");
            return;
        }

        if (num > taskCount) {
            System.out.println(INDENT + "That task does not exist! Try again!");
        } else {
            tasks[num-1].markDone();
            System.out.println(
                INDENT + "Congrats! I've marked this task as done:\n" +
                INDENT + tasks[num-1]
            );
        }
        System.out.println(HORIZONTAL_LINE);
    }


    private static void handleUnmark(String n) {
        System.out.println(HORIZONTAL_LINE);
        int num;
        try {
            num = Integer.parseInt(n);
        } catch (NumberFormatException e) {
            System.out.println(INDENT + "Invalid unmark command. Try again.");
            return;
        }

        if (num > taskCount) {
            System.out.println(INDENT + "That task does not exist! Try again!");
        } else {
            tasks[num-1].unmarkDone();
            System.out.println(
                INDENT + "Sure thing, I've marked this task as not done yet:\n" +
                INDENT + tasks[num-1]
            );
        }
        System.out.println(HORIZONTAL_LINE);
    }

    private static void handleUnknownCmd() {
        System.out.println(
            HORIZONTAL_LINE +
            INDENT + "I can't lie I have no idea what that means...\n" +
            HORIZONTAL_LINE
        );
    }

    public static void main(String[] args) {
        Omni.greet();
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();
        String[] parts = input.split("\\s+", 2);
        String cmd = parts[0];
        String arg = parts.length > 1 ? parts[1] : "";
        while (!cmd.equals("bye")) {
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
            default:
                //Omni.handleUnknownCmd();
                Omni.addTask(input);
            }
            input = sc.nextLine().trim();
            parts = input.split("\\s+", 2);
            cmd = parts[0];
            arg = parts.length > 1 ? parts[1] : "";
        }
        Omni.exit();
    }
}
