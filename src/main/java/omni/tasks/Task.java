package omni.tasks;

import java.util.Date;

/**
 * Represents a task with a description and completion status.
 * Serves as the base class for all task types in the Omni task management system.
 *
 * @author Brandon Tan
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a Task with the specified description and completion status.
     *
     * @param description The task description.
     * @param isDone Whether the task is completed.
     */
    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    public Task(Task other) {
        this.description = other.description;
        this.isDone = other.isDone;
    }


    public Task copy() {
        return new Task(description, isDone);
    }

    /**
     * Marks this task as done.
     */
    public void markDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void unmarkDone() {
        this.isDone = false;
    }

    /**
     * Returns the completion status of the task.
     *
     * @return True if the task is done, false otherwise.
     */
    public boolean isDone() {
        return this.isDone;
    }

    /**
     * Returns the status icon representing the completion status of the task.
     *
     * @return "X" if the task is done, " " otherwise.
     */
    public String getStatusIcon() {
        return (this.isDone ? "X" : " ");
    }

    /**
     * Returns the description of the task.
     *
     * @return The task description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the entry string representation for file storage.
     *
     * @return The formatted string for saving to file.
     */
    public String getEntryString() {
        String done = this.isDone() ? "1" : "0";
        return "T | " + this.getDescription() + " | " + done;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the string representation of this task for display.
     *
     * @return the formatted task string for display
     */
    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + description;
    }
}
