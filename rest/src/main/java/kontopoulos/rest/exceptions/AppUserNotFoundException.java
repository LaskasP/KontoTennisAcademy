package kontopoulos.rest.exceptions;

public class AppUserNotFoundException extends Exception {

    private static final String APP_USER_CANNOT_BE_FOUND = "Application user cannot be found.";

    public AppUserNotFoundException(String message) {
        super(message);
    }

    public AppUserNotFoundException() {
        super(APP_USER_CANNOT_BE_FOUND);
    }
}