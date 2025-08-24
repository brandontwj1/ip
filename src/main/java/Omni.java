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

    private static void listTasks() {
        System.out.print(HORIZONTAL_LINE + INDENT + "Here are the tasks you've added:\n");
        for (int i = 0; i < taskCount; i++) {
            Task t = tasks[i];
            System.out.printf(INDENT + "%d.[%s] %s\n", i+1, t.getStatusIcon(), t);
        }
        System.out.print(HORIZONTAL_LINE);
    }

    private static void handleMark(String input) {
        System.out.print(HORIZONTAL_LINE);
        Pattern pattern = Pattern.compile("mark (\\d{1,3})");
        Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) {
            Omni.markTask(matcher.group(1));
        } else {
            System.out.println(INDENT + "Something went wrong. Try again!");
        }

        System.out.print(HORIZONTAL_LINE);
    }

    private static void markTask(String n) {
        int num;
        try {
            num = Integer.parseInt(n);
        } catch (NumberFormatException e) {
            System.out.println(INDENT + "Invalid mark command. Try again");
            return;
        }

        if (num > taskCount) {
            System.out.println(INDENT + "That task does not exist! Try again!");
        } else {
            tasks[num-1].markDone();
            System.out.println(
                INDENT + "Congrats! I've marked this task as done:\n" +
                INDENT + "  [X] " + tasks[num]
            );
        }
    }

    public static void main(String[] args) {
        Omni.greet();
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        while (!input.equals("bye")) {
            if (input.matches("list")) {
                Omni.listTasks();
            } else if (input.matches("mark \\d{1,3}")) {
                Omni.handleMark(input);
            } else {
                Omni.addTask(input);
            }
            input = sc.nextLine();
        }
        Omni.exit();
    }
}
