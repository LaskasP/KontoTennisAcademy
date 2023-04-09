package kontopoulos.rest.listeners;

import kontopoulos.rest.services.attempt.AttemptService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;

@Configuration
@AllArgsConstructor
public class AuthenticationFailureListener {
    private AttemptService attemptService;

    @EventListener
    public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof String username) {
            attemptService.addAppUserToAttemptCache(username);
        }
    }
}