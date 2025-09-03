package Omni.tasks;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task with start and end dates and optional times.
 * Extends the base Task class to include event-specific functionality with duration.
 *
 * @author Brandon Tan
 */
public class Event extends Task {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmm");

    protected LocalDate startDate;
    protected LocalTime startTime;
    protected LocalDate endDate;
    protected LocalTime endTime;

    /**
     * Constructs an Event task with the specified description, completion status, start and end times.
     *
     * @param description the task description
     * @param isDone whether the task is completed
     * @param start the start date and time string in format "dd-MM-yyyy" or "dd-MM-yyyy HHmm"
     * @param end the end date and time string in format "dd-MM-yyyy" or "dd-MM-yyyy HHmm"
     */
    public Event(String description, boolean isDone, String start, String end) {
        super(description, isDone);
        String[] startDnT = start.split(" ");
        String startD = startDnT[0].trim();
        this.startDate = LocalDate.parse(startD, DATE_FORMATTER);
        if (startDnT.length > 1) {
            this.startTime = LocalTime.parse(startDnT[1].trim(), TIME_FORMATTER);
        }
        String[] endDnT = end.split(" ");
        String endD = endDnT[0].trim();
        this.endDate = LocalDate.parse(endD, DATE_FORMATTER);
        if (endDnT.length > 1) {
            this.endTime = LocalTime.parse(endDnT[1].trim(), TIME_FORMATTER);
        }
    }

    /**
     * Returns the start date and time as a formatted string.
     *
     * @return the start date and time string in the original format
     */
    public String getStartString() {
        String dnt = this.startDate.format(DATE_FORMATTER);
        if (this.startTime != null) {
            dnt = dnt + " " + this.startTime.format(TIME_FORMATTER);
        }
        return dnt;
    }

    /**
     * Returns the end date and time as a formatted string.
     *
     * @return the end date and time string in the original format
     */
    public String getEndString() {
        String dnt = this.endDate.format(DATE_FORMATTER);
        if (this.endTime != null) {
            dnt = dnt + " " + this.endTime.format(TIME_FORMATTER);
        }
        return dnt;
    }

    @Override
    public String getEntryString() {
        String done = this.isDone() ? "1" : "0";
        return "E | " + this.getDescription() + " | " + done + " | " + getStartString() + " | " + getEndString();
    }


    public String toString() {
        String startDnt = startDate.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        if (startTime != null) {
            startDnt = startDnt + " " + startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        String endDnt = endDate.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        if (endTime != null) {
            endDnt = endDnt + " " + endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return "[E]" + super.toString() + " (from: " + startDnt + " to: " + endDnt + ")";
    }
}