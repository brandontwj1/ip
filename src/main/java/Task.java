public class Task {
    private String description;
    private boolean done;

    public Task(String description) {
        this.description = description;
        this.done = false;
    }

    public void markDone() {
        this.done = true;
    }

    public void unmarkDone() {
        this.done = false;
    }

    public boolean isDone() {
        return this.done;
    }

    @Override
    public String toString() {
        return description;
    }
}
