package omni.exceptions;

/**
 * Exception thrown when a user enters a command that is not recognized by the application.
 * This occurs when the command does not match any of the valid commands supported by Omni.
 *
 * @author Brandon Tan
 */
public class UnknownCommandException extends OmniException {
    /**
     * Constructs an UnknownCommandException with the specified detail message.
     *
     * @param message The detail message explaining the unknown command issue.
     */
    public UnknownCommandException(String message) {
        super(message);
    }
}
