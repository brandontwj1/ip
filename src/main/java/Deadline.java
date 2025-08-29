public class Deadline extends Task {

    protected String by;

    public Deadline(String description, boolean isDone, String by) {
        super(description, isDone);
        this.by = by;
    }

    public String getBy() {
        return this.by;
    }

    @Override
    public String getEntryString() {
        String done = this.isDone() ? "1" : "0";
        return "D | " + this.getDescription() + " | " + done + " | " + by;
    }


    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
