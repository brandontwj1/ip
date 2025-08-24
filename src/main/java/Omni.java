import java.util.Scanner;

public class Omni {
    private static final String HORIZONTAL_LINE = "   _________________________________________________________\n";
    private static final String INDENT = "    ";
    private static String[] tasks = new String[100];
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

    private static void addTask(String input) {
        tasks[taskCount] = input;
        taskCount++;
        System.out.println(
            HORIZONTAL_LINE +
            INDENT + "Added: " + input + "\n" +
            HORIZONTAL_LINE
        );
    }

    private static void listTasks() {
        System.out.print(HORIZONTAL_LINE);
        for (int i = 0; i < taskCount; i++) {
            System.out.printf(INDENT + "%d. %s\n", i, tasks[i]);
        }
        System.out.print(HORIZONTAL_LINE);
    }

    public static void main(String[] args) {
        Omni.greet();
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        while (!input.equals("bye")) {
            switch (input) {
            case "list":
                Omni.listTasks();
                break;
            default:
                addTask(input);
            }
            input = sc.nextLine();
        }
        Omni.exit();
    }
}
