package omni.exceptions;

public class OmniException extends Exception {

    public OmniException(String message) {
        super(message);
    }

    public String getUserMessage() {
        return this.getMessage();
    }
}
