package Omni.tasks;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
     * @param description the task description
     * @param isDone whether the task is completed
     * @param deadline the deadline string in format "dd-MM-yyyy" or "dd-MM-yyyy HHmm"
     */
    public Deadline(String description, boolean isDone, String deadline) {
        super(description, isDone);
        String[] dateAndTime = deadline.split(" ");
        String dateStr = dateAndTime[0].trim();
        this.date = LocalDate.parse(dateStr, DATE_FORMATTER);
        if (dateAndTime.length > 1) {
            this.time = LocalTime.parse(dateAndTime[1].trim(), TIME_FORMATTER);
        }
    }

    /**
     * Returns the deadline as a formatted string.
     *
     * @return the deadline string in the original format
     */
    public String getDeadlineString() {
        String dnt = this.date.format(DATE_FORMATTER);
        if (this.time != null) {
            dnt = dnt + " " + this.time.format(TIME_FORMATTER);
        }
        return dnt;
    }



    @Override
    public String getEntryString() {
        String done = this.isDone() ? "1" : "0";
        String dnt = getDeadlineString();
        return "D | " + this.getDescription() + " | " + done + " | " + dnt;
    }


    @Override
    public String toString() {
        String dnt = date.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        if (time != null) {
            dnt = dnt + " " + time.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return "[D]" + super.toString() + " (by: " + dnt + ")";
    }
}
