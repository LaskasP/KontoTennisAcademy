package kontopoulos.rest.services.appuser;

import kontopoulos.rest.exceptions.EmailExistsException;
import kontopoulos.rest.exceptions.UsernameExistsException;
import kontopoulos.rest.models.security.rest.request.ChangePasswordRequest;
import kontopoulos.rest.models.security.rest.request.LoginRequest;
import kontopoulos.rest.models.security.rest.request.RegisterRequest;
import kontopoulos.rest.models.security.rest.response.LoginResponseWrapper;
import kontopoulos.rest.models.security.rest.response.RegisterResponseWrapper;

public interface AppUserService {
    RegisterResponseWrapper registerAppUser(RegisterRequest registerRequest) throws UsernameExistsException, EmailExistsException;

    void changePassword(ChangePasswordRequest changePasswordRequest);

    LoginResponseWrapper loginAppUser(LoginRequest loginRequest);

}