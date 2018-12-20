package messages;

/**
 * Project: micro16-compiler
 * Document: ErrorMessages.java
 * Author: Diego Krupitza
 * Created: 12.12.18
 */
public enum ErrorMessages {
    FILE_NOTEXIST("The file you want to access does not exists!");

    private String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.getMessage();
    }

    public String getMessage() {
        return message;
    }
}
