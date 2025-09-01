package Omni.ui;
import Omni.tasklist.TaskList;
import Omni.tasks.Task;


public class Ui {
    private static final String HORIZONTAL_LINE = "   _________________________________________________________\n";
    private static final String INDENT = "    ";

    public Ui() {}

    public void startReply() {
        System.out.print(HORIZONTAL_LINE);
    }

    public void endReply() {
        System.out.println(HORIZONTAL_LINE);
    }

    public void greet() {
        System.out.println(
                HORIZONTAL_LINE
                        + INDENT + "Helloo! I'm Omni!\n"
                        + INDENT + "What can I do for you?\n"
                        + HORIZONTAL_LINE
        );
    }

    public void exit() {
        System.out.println(INDENT + "Byeee! See you in a bit!");
    }

    public void showError(String errorMessage) {
        System.out.println(INDENT + errorMessage);
    }

    public void showLoadingError(String errorMessage) {
        System.out.println("Error creating tasks.txt file: " + errorMessage);
    }

    public void showTasks(TaskList tasks) {
        if (tasks.isEmpty()) {
            System.out.println(INDENT + "You have no tasks... Add one!");
            return;
        }
        System.out.print(INDENT + "Here are the tasks you've added:\n");
        for (int i = 0; i < tasks.getSize(); i++) {
            Task t = tasks.getTask(i);
            System.out.printf(INDENT + "%d.%s\n", i+1, t);
        }
    }

    public void showMarked(Task t) {
        System.out.println(
                INDENT + "Congrats! I've marked this task as done:\n" +
                        INDENT + "  " + t
        );
    }

    public void showUnmarked(Task t) {
        System.out.println(
                INDENT + "Sure thing, I've marked this task as not done yet:\n" +
                        INDENT + "  " + t
        );
    }

    public void showAdded(Task t, TaskList tasks) {
        String taskStr = tasks.getSize() == 1 ? "task" : "tasks";
        System.out.println(
                INDENT + "Got it. I've added this task:\n" +
                        INDENT + "  " + t + "\n" +
                        INDENT + "Now you have " + tasks.getSize() + " " + taskStr + " in the list."
        );
    }

    public void showErased(Task t) {
        System.out.println(
                INDENT + "Gotchu, I've deleted this task for you:\n" +
                        INDENT + "  " + t
        );
    }


}
