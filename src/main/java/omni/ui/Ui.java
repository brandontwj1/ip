package omni.ui;
import java.util.ArrayList;

import omni.tasklist.TaskList;
import omni.tasks.Task;

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
     * @param errorMessage The error message to display.
     */
    public void showError(String errorMessage) {
        System.out.println(INDENT + errorMessage);
    }

    /**
     * Displays a loading error message.
     *
     * @param errorMessage The loading error message to display.
     */
    public void showLoadingError(String errorMessage) {
        System.out.println("Error creating tasks.txt file: " + errorMessage);
    }

    /**
     * Displays the list of tasks to the user.
     *
     * @param tasks The task list to display.
     */
    public void showTasks(TaskList tasks) {
        if (tasks.isEmpty()) {
            System.out.println(INDENT + "You have no tasks... Add one!");
            return;
        }
        System.out.print(INDENT + "Here are the tasks you've added:\n");
        for (int i = 0; i < tasks.getSize(); i++) {
            Task t = tasks.getTask(i);
            System.out.printf(INDENT + "%d.%s\n", i + 1, t);
        }
    }

    /**
     * Displays a confirmation message when a task is marked as done.
     *
     * @param task The task that was marked as done.
     */
    public void showMarked(Task task) {
        System.out.println(
                INDENT + "Congrats! I've marked this task as done:\n" +
                        INDENT + "  " + task
        );
    }

    /**
     * Displays a confirmation message when a task is unmarked.
     *
     * @param task The task that was unmarked.
     */
    public void showUnmarked(Task task) {
        System.out.println(
                INDENT + "Sure thing, I've marked this task as not done yet:\n" +
                        INDENT + "  " + task
        );
    }

    /**
     * Displays a confirmation message when a task is added.
     *
     * @param task The task that was added.
     * @param tasks The updated task list.
     */
    public void showAdded(Task task, TaskList tasks) {
        String taskStr = tasks.getSize() == 1 ? "task" : "tasks";
        System.out.println(
                INDENT + "Got it. I've added this task:\n" +
                        INDENT + "  " + task + "\n" +
                        INDENT + "Now you have " + tasks.getSize() + " " + taskStr + " in the list."
        );
    }

    /**
     * Displays a confirmation message when a task is deleted.
     *
     * @param task The task that was deleted.
     */
    public void showErased(Task task) {
        System.out.println(
                INDENT + "Gotchu, I've deleted this task for you:\n" +
                        INDENT + "  " + task
        );
    }

    /**
     * Displays the matching tasks with a given keyword.
     *
     * @param matchingTasks The tasks that contain the given keyword.
     */
    public void showMatchingTasks(ArrayList<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            System.out.println(
                    INDENT + "No tasks containing that keyword. Try another one!"
            );
        } else {
            System.out.println("Here are the matching tasks in your list: ");
            for (int i = 0; i < matchingTasks.size(); i++) {
                Task t = matchingTasks.get(i);
                System.out.printf(INDENT + "%d.%s\n", i + 1, t);
            }
        }
    }
}
