package omni.tasklist;

import java.util.ArrayList;

import omni.tasks.Task;

/**
 * Represents a list of tasks and provides methods to manipulate them.
 * Supports adding, removing, marking, and unmarking tasks.
 *
 * @author Brandon Tan
 */
public class TaskList {

    private ArrayList<Task> tasks;

    /**
     * Constructs a TaskList with the specified list of tasks.
     *
     * @param tasks The initial list of tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Checks if the task list is empty.
     *
     * @return True if the task list is empty, false otherwise.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The size of the task list.
     */
    public int getSize() {
        return tasks.size();
    }

    /**
     * Returns the task at the specified index.
     *
     * @param index The index of the task to retrieve.
     * @return The task at the specified index.
     */
    public Task getTask(int index) {
        return tasks.get(index);
    }

    /**
     * Marks the task at the specified index as done.
     *
     * @param index The index of the task to mark as done.
     * @return The task that was marked as done.
     */
    public Task markTaskDone(int index) {
        Task t = getTask(index);
        t.markDone();
        return t;
    }

    /**
     * Marks the task at the specified index as not done.
     *
     * @param index The index of the task to unmark.
     * @return The task that was unmarked.
     */
    public Task unmarkTaskDone(int index) {
        Task t = getTask(index);
        t.unmarkDone();
        return t;
    }

    /**
     * Adds a task to the list.
     *
     * @param task The task to add.
     * @return The added task.
     */
    public Task addTask(Task task) {
        tasks.add(task);
        return task;
    }

    /**
     * Removes the task at the specified index from the list.
     *
     * @param index The index of the task to remove.
     * @return The removed task.
     */
    public Task removeTask(int index) {
        return tasks.remove(index);
    }

    /**
     * Finds the tasks that contains the keyword provided.
     *
     * @param keyword The keyword to find.
     * @return The tasks containing the keyword
     */
    public ArrayList<Task> findMatchingTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}
