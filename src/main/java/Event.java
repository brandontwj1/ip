public class Event extends Task {
    protected String start;
    protected String end;

    public Event(String description, boolean isDone, String start, String end) {
        super(description, isDone);
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return this.start;
    }

    public String getEnd() {
        return this.end;
    }

    @Override
    public String getEntryString() {
        String done = this.isDone() ? "1" : "0";
        return "E | " + this.getDescription() + " | " + done + " | " + start + " | " + end + "\n";
    }



    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + start + " to: " + end + ")";
    }
}
