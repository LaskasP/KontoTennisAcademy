package kontopoulos.rest.exceptions;

public class UsernameExistsException extends Exception {
    public UsernameExistsException(String message) {
        super(message);
    }
}