package omni.tasks;

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
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmm");

    protected LocalDate startDate;
    protected LocalTime startTime;
    protected LocalDate endDate;
    protected LocalTime endTime;

    /**
     * Constructs an Event task with the specified description, completion status, start and end times.
     *
     * @param description The task description.
     * @param isDone Whether the task is completed.
     * @param start The start date and time string in format "dd-MM-yyyy" or "dd-MM-yyyy HHmm".
     * @param end The end date and time string in format "dd-MM-yyyy" or "dd-MM-yyyy HHmm".
     */
    public Event(String description, boolean isDone, String start, String end) {
        super(description, isDone);
        String[] startDateAndTime = start.split(" ");
        String startDate = startDateAndTime[0].trim();
        assert !startDate.isEmpty() : "start date cannot be empty";
        this.startDate = LocalDate.parse(startDate, DATE_FORMATTER);
        if (startDateAndTime.length > 1) {
            this.startTime = LocalTime.parse(startDateAndTime[1].trim(), TIME_FORMATTER);
        }
        String[] endDateAndTime = end.split(" ");
        String endDate = endDateAndTime[0].trim();
        assert !endDate.isEmpty() : "end date cannot be empty";
        this.endDate = LocalDate.parse(endDate, DATE_FORMATTER);
        if (endDateAndTime.length > 1) {
            this.endTime = LocalTime.parse(endDateAndTime[1].trim(), TIME_FORMATTER);
        }
    }

    /**
     * Returns the start date and time as a formatted string.
     *
     * @return The start date and time string in the original format.
     */
    public String getStartString() {
        String dateAndTime = this.startDate.format(DATE_FORMATTER);
        if (this.startTime != null) {
            dateAndTime = dateAndTime + " " + this.startTime.format(TIME_FORMATTER);
        }
        return dateAndTime;
    }

    /**
     * Returns the end date and time as a formatted string.
     *
     * @return The end date and time string in the original format.
     */
    public String getEndString() {
        String dateAndTime = this.endDate.format(DATE_FORMATTER);
        if (this.endTime != null) {
            dateAndTime = dateAndTime + " " + this.endTime.format(TIME_FORMATTER);
        }
        return dateAndTime;
    }

    @Override
    public String getEntryString() {
        String done = this.isDone() ? "1" : "0";
        return "E | " + this.getDescription() + " | " + done + " | " + getStartString() + " | " + getEndString();
    }

    @Override
    public String toString() {
        String startDateAndTime = startDate.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        if (startTime != null) {
            startDateAndTime = startDateAndTime + " " + startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        String endDateAndTime = endDate.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        if (endTime != null) {
            endDateAndTime = endDateAndTime + " " + endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return "[E]" + super.toString() + " (from: " + startDateAndTime + " to: " + endDateAndTime + ")";
    }
}