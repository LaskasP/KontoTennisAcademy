package kontopoulos.rest.listeners;

import kontopoulos.rest.models.security.UserDetailsImpl;
import kontopoulos.rest.services.attempt.AttemptService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthenticationSuccessListener {
    private AttemptService attemptService;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl user) {
            attemptService.evictAppUserFromAttemptCache(user.getUsername());
        }
    }
}