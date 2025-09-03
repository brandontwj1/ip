package Omni.ui;
import Omni.tasklist.TaskList;
import Omni.tasks.Task;

/**
 * Handles user interface interactions and displays messages to the user.
 * Provides methods to show various types of feedback including greetings, errors,
 * task status updates, and formatted task lists.
 *
 * @author Brandon Tan
 */
public class Ui {
    private static final String HORIZONTAL_LINE = "   _________________________________________________________\n";
    private static final String INDENT = "    ";

    /**
     * Constructs a new Ui object.
     */
    public Ui() {}

    /**
     * Prints the starting horizontal line for a reply.
     */
    public void startReply() {
        System.out.print(HORIZONTAL_LINE);
    }

    /**
     * Prints the ending horizontal line for a reply.
     */
    public void endReply() {
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays a greeting message to the user.
     */
    public void greet() {
        System.out.println(
                HORIZONTAL_LINE
                        + INDENT + "Helloo! I'm Omni!\n"
                        + INDENT + "What can I do for you?\n"
                        + HORIZONTAL_LINE
        );
    }

    /**
     * Displays an exit message to the user.
     */
    public void exit() {
        System.out.println(INDENT + "Byeee! See you in a bit!");
    }

    /**
     * Displays an error message to the user.
     *
     * @param errorMessage the error message to display
     */
    public void showError(String errorMessage) {
        System.out.println(INDENT + errorMessage);
    }

    /**
     * Displays a loading error message.
     *
     * @param errorMessage the loading error message to display
     */
    public void showLoadingError(String errorMessage) {
        System.out.println("Error creating tasks.txt file: " + errorMessage);
    }

    /**
     * Displays the list of tasks to the user.
     *
     * @param tasks the task list to display
     */
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

    /**
     * Displays a confirmation message when a task is marked as done.
     *
     * @param t the task that was marked as done
     */
    public void showMarked(Task t) {
        System.out.println(
                INDENT + "Congrats! I've marked this task as done:\n" +
                        INDENT + "  " + t
        );
    }

    /**
     * Displays a confirmation message when a task is unmarked.
     *
     * @param t the task that was unmarked
     */
    public void showUnmarked(Task t) {
        System.out.println(
                INDENT + "Sure thing, I've marked this task as not done yet:\n" +
                        INDENT + "  " + t
        );
    }

    /**
     * Displays a confirmation message when a task is added.
     *
     * @param t the task that was added
     * @param tasks the updated task list
     */
    public void showAdded(Task t, TaskList tasks) {
        String taskStr = tasks.getSize() == 1 ? "task" : "tasks";
        System.out.println(
                INDENT + "Got it. I've added this task:\n" +
                        INDENT + "  " + t + "\n" +
                        INDENT + "Now you have " + tasks.getSize() + " " + taskStr + " in the list."
        );
    }

    /**
     * Displays a confirmation message when a task is deleted.
     *
     * @param t the task that was deleted
     */
    public void showErased(Task t) {
        System.out.println(
                INDENT + "Gotchu, I've deleted this task for you:\n" +
                        INDENT + "  " + t
        );
    }


}
