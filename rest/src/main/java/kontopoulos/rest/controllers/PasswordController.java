package kontopoulos.rest.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kontopoulos.rest.exceptions.AppUserNotFoundException;
import kontopoulos.rest.models.security.rest.request.ChangePasswordRequest;
import kontopoulos.rest.services.appuser.AppUserService;
import kontopoulos.rest.services.email.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password")
@AllArgsConstructor
@Validated
public class PasswordController {
    public static final String PROVIDE_A_VALID_EMAIL_ADDRESS = "Provide a valid Email address";
    public static final String EMAIL_CANNOT_BE_NULL = "Email cannot be null";
    private EmailService emailService;

    private AppUserService appUserService;

    @GetMapping("/reset")
    public void resetPassword(@RequestParam @Email(message = PROVIDE_A_VALID_EMAIL_ADDRESS) @NotBlank(message = EMAIL_CANNOT_BE_NULL) String email) throws AppUserNotFoundException {
        emailService.sendResetPasswordMail(email);
    }

    @PutMapping("/change")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        appUserService.changePassword(changePasswordRequest);
        return ResponseEntity.noContent().build();
    }
}
