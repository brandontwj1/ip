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
     *
     * @return The greeting message string.
     */
    public String greet() {
        return "Helloo! I'm Omni!\n"
                + "What can I do for you?\n";
    }

    /**
     * Displays an exit message to the user.
     *
     * @return The exit message string.
     */
    public String exit() {
        return "Byeee! See you in a bit!";
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
     * @return String representation of the task list.
     */
    public String showTasks(TaskList tasks) {
        if (tasks.isEmpty()) {
            return "You have no tasks... Add one!";
        }
        System.out.print(INDENT + "Here are the tasks you've added:\n");
        StringBuilder reply = new StringBuilder("Here are the tasks you've added:\n");
        for (int i = 0; i < tasks.getSize(); i++) {
            Task t = tasks.getTask(i);
            reply.append(String.format("    %d.%s\n", i + 1, t));
        }
        return reply.toString();
    }

    /**
     * Returns a confirmation message when a task is marked as done.
     *
     * @param task The task that was marked as done.
     * @return Confirmation message string.
     */
    public String showMarked(Task task) {
        return "Congrats! I've marked this task as done:\n"
                + "  " + task;
    }

    /**
     * Displays a confirmation message when a task is unmarked.
     *
     * @param task The task that was unmarked.
     * @return Confirmation message string.
     */
    public String showUnmarked(Task task) {
        return "Sure thing, I've marked this task as not done yet:\n"
                + "  " + task;
    }

    /**
     * Displays a confirmation message when a task is added.
     *
     * @param task The task that was added.
     * @param tasks The updated task list.
     * @return Confirmation message string with task count.
     */
    public String showAdded(Task task, TaskList tasks) {
        String taskStr = tasks.getSize() == 1 ? "task" : "tasks";
        return "Got it. I've added this task:\n"
                + "  " + task + "\n"
                + "Now you have " + tasks.getSize() + " " + taskStr + " in the list.";
    }

    /**
     * Displays a confirmation message when a task is deleted.
     *
     * @param task The task that was deleted.
     * @return Confirmation message string.
     */
    public String showErased(Task task) {
        return "Gotchu, I've deleted this task for you:\n"
                + "  " + task;
    }

    /**
     * Displays the matching tasks with a given keyword.
     *
     * @param matchingTasks The tasks that contain the given keyword.
     * @return String representation of matching tasks or message if none found.
     */
    public String showMatchingTasks(ArrayList<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            return "No tasks containing that keyword. Try another one!";
        } else {
            StringBuilder reply = new StringBuilder("Here are the matching tasks in your list:\n");
            for (int i = 0; i < matchingTasks.size(); i++) {
                Task t = matchingTasks.get(i);
                reply.append(String.format("    %d.%s\n", i + 1, t));
            }
            return reply.toString();
        }
    }
}
