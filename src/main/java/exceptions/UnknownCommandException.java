package exceptions;

public class UnknownCommandException extends OmniException {
    public UnknownCommandException(String message) {
        super(message);
    }
}
