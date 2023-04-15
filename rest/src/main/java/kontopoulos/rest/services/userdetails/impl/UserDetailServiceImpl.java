package kontopoulos.rest.services.userdetails.impl;

import kontopoulos.rest.models.security.UserDetailsImpl;
import kontopoulos.rest.models.security.entity.AppUserEntity;
import kontopoulos.rest.repos.AppUserRepository;
import kontopoulos.rest.services.attempt.AttemptService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private AppUserRepository appUserRepository;

    private AttemptService attemptService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUserEntity appUserEntity = appUserRepository.findByUsername(username);
        if (appUserEntity == null) {
            log.error("User Not Found with username: " + username);
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }
        validateLoginAttempts(appUserEntity);
        appUserEntity.setLastLoginDateDisplay(appUserEntity.getLastLoginDate());
        appUserEntity.setLastLoginDate(LocalDateTime.now());
        appUserRepository.save(appUserEntity);
        log.debug("User returned with username: " + username);
        return new UserDetailsImpl(appUserEntity);
    }

    private void validateLoginAttempts(AppUserEntity appUserEntity) {
        final boolean hasExceedMaxAttempts = attemptService.hasExceededMaxAttempts(appUserEntity.getUsername());
        appUserEntity.setNotLocked(!hasExceedMaxAttempts);
    }
}