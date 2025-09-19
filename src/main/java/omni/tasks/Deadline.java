package omni.tasks;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import omni.exceptions.InvalidArgumentException;
import omni.exceptions.OmniException;
import omni.parser.Parser;

/**
 * Represents a deadline task with a specific due date and optional time.
 * Extends the base Task class to include deadline-specific functionality.
 *
 * @author Brandon Tan
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmm");

    protected LocalDate date;
    protected LocalTime time;

    /**
     * Constructs a Deadline task with the specified description, completion status, and deadline.
     *
     * @param description The task description.
     * @param isDone Whether the task is completed.
     * @param deadline The deadline string in format "dd-MM-yyyy" or "dd-MM-yyyy HHmm".
     */
    public Deadline(String description, boolean isDone, String deadline) throws InvalidArgumentException {
        super(description, isDone);
        this.date = Parser.parseDateFromDateTime(deadline);
        this.time = Parser.parseTimeFromDateTime(deadline);
    }

    public Deadline(Deadline other) {
        super(other);
        this.date = other.date;
        this.time = other.time;
    }

    @Override
    public Deadline copy() {
        return new Deadline(this);
    }



    /**
     * Returns the deadline as a formatted string.
     *
     * @return The deadline string in the original format.
     */
    public String getDeadlineString() {
        String dateTimeString = this.date.format(DATE_FORMATTER);
        if (this.time != null) {
            dateTimeString = dateTimeString + " " + this.time.format(TIME_FORMATTER);
        }
        return dateTimeString;
    }

    @Override
    public String getEntryString() {
        String done = this.isDone() ? "1" : "0";
        String dateTimeString = getDeadlineString();
        return "D | " + this.getDescription() + " | " + done + " | " + dateTimeString;
    }

    public void setDeadline(String deadline) throws InvalidArgumentException {
        this.date = Parser.parseDateFromDateTime(deadline);
        this.time = Parser.parseTimeFromDateTime(deadline);
    }

    @Override
    public String toString() {
        String dateTimeString = date.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        if (time != null) {
            dateTimeString = dateTimeString + " " + time.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return "[D]" + super.toString() + " (by: " + dateTimeString + ")";
    }
}
