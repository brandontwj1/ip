package omni.exceptions;

/**
 * Exception thrown when a user provides invalid arguments for a command.
 * This includes cases where required parameters are missing, have incorrect format,
 * or contain invalid values.
 *
 * @author Brandon Tan
 */
public class InvalidArgumentException extends OmniException {
    /**
     * Constructs an InvalidArgumentException with the specified detail message.
     *
     * @param message The detail message explaining the invalid argument issue.
     */
    public InvalidArgumentException(String message) {
        super(message);
    }
}
