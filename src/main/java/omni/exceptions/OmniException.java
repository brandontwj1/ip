package omni.exceptions;

/**
 * Base exception class for the Omni task management application.
 * All custom exceptions in the Omni application extend from this class.
 *
 * @author Brandon Tan
 */
public class OmniException extends Exception {

    /**
     * Constructs an OmniException with the specified detail message.
     *
     * @param message The detail message explaining the exception.
     */
    public OmniException(String message) {
        super(message);
    }

    /**
     * Returns the user-friendly error message for this exception.
     *
     * @return The error message to be displayed to the user.
     */
    public String getUserMessage() {
        return this.getMessage();
    }
}
