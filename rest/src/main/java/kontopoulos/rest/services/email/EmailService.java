package kontopoulos.rest.services.email;

import kontopoulos.rest.exceptions.AppUserNotFoundException;

public interface EmailService {
    void sendResetPasswordMail(String email) throws AppUserNotFoundException;
}