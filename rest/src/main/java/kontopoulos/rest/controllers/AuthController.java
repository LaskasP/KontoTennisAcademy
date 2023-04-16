package kontopoulos.rest.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import kontopoulos.rest.exceptions.EmailExistsException;
import kontopoulos.rest.exceptions.RefreshTokenException;
import kontopoulos.rest.exceptions.UsernameExistsException;
import kontopoulos.rest.models.security.rest.request.LoginRequest;
import kontopoulos.rest.models.security.rest.request.RegisterRequest;
import kontopoulos.rest.models.security.rest.response.*;
import kontopoulos.rest.services.appuser.AppUserService;
import kontopoulos.rest.services.refreshtoken.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static kontopoulos.rest.models.security.SecurityConstant.REFRESH_AUTHORIZATION_HEADER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Validated
public class AuthController {

    private static final String REFRESH_TOKEN_CANNOT_BE_NULL = "Refresh token cannot be null.";
    private static final String USERNAME_CANNOT_BE_NULL = "Username cannot be null.";
    private static final String REGISTRATION = "/registration";
    private static final String LOGIN = "/login";
    private static final String REFRESHMENT = "/refreshment";
    private static final String LOGOUT = "/logout";
    private AppUserService appUserService;
    private RefreshTokenService refreshTokenService;

    @PostMapping(REGISTRATION)
    public ResponseEntity<RegisterResponse> signUpAppUser(@Valid @RequestBody RegisterRequest registerRequest) throws UsernameExistsException, EmailExistsException {
        RegisterResponseWrapper registerResponseWrapper = appUserService.registerAppUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).header(AUTHORIZATION, registerResponseWrapper.getHttpHeader()).header(REFRESH_AUTHORIZATION_HEADER, refreshTokenService.createRefreshToken(registerRequest.getUsername())).body(registerResponseWrapper.getRegisterResponse());
    }

    @PostMapping(LOGIN)
    public ResponseEntity<LoginResponse> loginAppUser(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponseWrapper loginResponseWrapper = appUserService.loginAppUser(loginRequest);
        return ResponseEntity.ok().header(AUTHORIZATION, loginResponseWrapper.getHttpHeader()).header(REFRESH_AUTHORIZATION_HEADER, refreshTokenService.createRefreshToken(loginRequest.getUsername())).body(loginResponseWrapper.getLoginResponse());
    }

    @GetMapping(REFRESHMENT)
    public ResponseEntity<Void> refreshToken(@RequestHeader(REFRESH_AUTHORIZATION_HEADER) @NotBlank(message = REFRESH_TOKEN_CANNOT_BE_NULL) String authorizationRefreshToken) throws RefreshTokenException {
        JWTPairHeadersWrapper jwtPairHeadersWrapper = refreshTokenService.generateNewJWTTokenPair(authorizationRefreshToken);
        return ResponseEntity.ok().header(AUTHORIZATION, jwtPairHeadersWrapper.getAccessToken()).header(REFRESH_AUTHORIZATION_HEADER, jwtPairHeadersWrapper.getRefreshToken()).build();
    }

    @GetMapping(LOGOUT)
    public ResponseEntity<Void> signOutAppUser(@RequestParam @NotBlank(message = USERNAME_CANNOT_BE_NULL) String username) {
        refreshTokenService.deleteByUserId(username);
        return ResponseEntity.ok().build();
    }
}