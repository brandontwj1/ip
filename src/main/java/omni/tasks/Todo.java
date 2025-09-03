package omni.tasks;

/**
 * Represents a simple todo task without any date or time constraints.
 * Extends the base Task class with basic todo functionality.
 *
 * @author Brandon Tan
 */
public class Todo extends Task {

    /**
     * Constructs a Todo task with the specified description and completion status.
     *
     * @param description The task description.
     * @param isDone Whether the task is completed.
     */
    public Todo(String description, boolean isDone) {
        super(description, isDone);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}