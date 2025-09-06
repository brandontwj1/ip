package omni.exceptions;

/**
 * Exception thrown when there are issues with reading from or writing to the task storage file.
 * This includes cases where the file is corrupted, cannot be created, or has invalid format.
 *
 * @author Brandon Tan
 */
public class CorruptedFileException extends OmniException {
    /**
     * Constructs a CorruptedFileException with the specified detail message.
     *
     * @param message The detail message explaining the file corruption issue.
     */
    public CorruptedFileException(String message) {
        super(message);
    }
}
