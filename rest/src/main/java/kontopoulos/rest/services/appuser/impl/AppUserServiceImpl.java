package kontopoulos.rest.services.appuser.impl;

import kontopoulos.rest.exceptions.EmailExistsException;
import kontopoulos.rest.exceptions.UsernameExistsException;
import kontopoulos.rest.models.security.UserDetailsImpl;
import kontopoulos.rest.models.security.entity.AppUserEntity;
import kontopoulos.rest.models.security.entity.RoleEntity;
import kontopoulos.rest.models.security.lov.AppUserRole;
import kontopoulos.rest.models.security.rest.request.ChangePasswordRequest;
import kontopoulos.rest.models.security.rest.request.LoginRequest;
import kontopoulos.rest.models.security.rest.request.RegisterRequest;
import kontopoulos.rest.models.security.rest.response.LoginResponse;
import kontopoulos.rest.models.security.rest.response.LoginResponseWrapper;
import kontopoulos.rest.models.security.rest.response.RegisterResponse;
import kontopoulos.rest.models.security.rest.response.RegisterResponseWrapper;
import kontopoulos.rest.repos.RoleRepository;
import kontopoulos.rest.repos.UserRepository;
import kontopoulos.rest.services.appuser.AppUserService;
import kontopoulos.rest.utils.JWTProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImpl implements AppUserService {
    public static final String ACCOUNT_ALREADY_EXISTS_WITH_USERNAME = "Account already exists with Username: ";
    public static final String ACCOUNT_ALREADY_EXISTS_WITH_EMAIL = "Account already exists with Email: ";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;

    @Override
    public RegisterResponseWrapper registerAppUser(RegisterRequest registerRequest) throws UsernameExistsException, EmailExistsException {
        log.info("Begin registerAppUser");
        log.debug("Begin Saving appUserRequest with username: " + registerRequest.getUsername());
        validateNewUsernameAndEmailDoesNotExist(registerRequest.getUsername(), registerRequest.getEmail());
        AppUserEntity newAppUserEntity = createNewAppUserEntity(registerRequest);
        UserDetailsImpl userDetails = new UserDetailsImpl(newAppUserEntity);
        String httpHeader = jwtProvider.generateJWT(userDetails);
        RegisterResponse registerResponse = modelMapper.map(userRepository.save(newAppUserEntity), RegisterResponse.class);
        List<AppUserRole> roles = extractAppUserRoles(newAppUserEntity);
        registerResponse.setRoles(roles);
        log.debug("End Saved newAppUser with username: " + newAppUserEntity.getUsername());
        log.info("End registerAppUser");
        return new RegisterResponseWrapper(httpHeader, registerResponse);
    }

    @Override
    public LoginResponseWrapper loginAppUser(LoginRequest loginRequest) {
        log.info("Begin loginAppUser");
        authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        AppUserEntity appUserEntity = getAppUserByUsername(loginRequest.getUsername());
        UserDetailsImpl userDetails = new UserDetailsImpl(appUserEntity);
        String httpHeader = jwtProvider.generateJWT(userDetails);
        LoginResponse loginResponse = modelMapper.map(appUserEntity, LoginResponse.class);
        List<AppUserRole> roles = extractAppUserRoles(appUserEntity);
        loginResponse.setRoles(roles);
        log.info("End loginAppUser");
        return new LoginResponseWrapper(httpHeader, loginResponse);
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        log.info("Begin changePassword");
        authenticate(changePasswordRequest.getUsername(), changePasswordRequest.getPassword());
        AppUserEntity appUserEntity = getAppUserByUsername(changePasswordRequest.getUsername());
        if (appUserEntity != null) {
            appUserEntity.setPassword(getEncodedPassword(changePasswordRequest.getNewPassword()));
            userRepository.save(appUserEntity);
        }
        log.info("End changePassword");
    }

    private static List<AppUserRole> extractAppUserRoles(AppUserEntity appUserEntity) {
        List<AppUserRole> roles = new ArrayList<>();
        for (RoleEntity roleEntity : appUserEntity.getRoleEntities()) {
            roles.add(roleEntity.getAppUserRole());
        }
        return roles;
    }

    private AppUserEntity createNewAppUserEntity(RegisterRequest registerRequest) {
        AppUserEntity newAppUserEntity = modelMapper.map(registerRequest, AppUserEntity.class);
        newAppUserEntity.setRoleEntities(Set.of(roleRepository.findFirstByAppUserRole(AppUserRole.ROLE_USER)));
        newAppUserEntity.setJoinDate(LocalDateTime.now());
        newAppUserEntity.setActive(true);
        newAppUserEntity.setNotLocked(true);
        newAppUserEntity.setPassword(getEncodedPassword(registerRequest.getPassword()));
        newAppUserEntity.setProfileImageUrl(getTempImg());
        return newAppUserEntity;
    }

    private String getTempImg() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/image/temp").toUriString();
    }

    private String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private void validateNewUsernameAndEmailDoesNotExist(String newUsername, String newEmail) throws UsernameExistsException, EmailExistsException {
        if (newUsername != null) {
            AppUserEntity appUserEntityByNewUsername = getAppUserByUsername(newUsername);
            if (appUserEntityByNewUsername != null) {
                throw new UsernameExistsException(ACCOUNT_ALREADY_EXISTS_WITH_USERNAME + newUsername);
            }
        }
        if (newEmail != null) {
            AppUserEntity appUserEntityByNewEmail = getAppUserByEmail(newEmail);
            if (appUserEntityByNewEmail != null) {
                throw new EmailExistsException(ACCOUNT_ALREADY_EXISTS_WITH_EMAIL + newEmail);
            }
        }
    }

    private AppUserEntity getAppUserByUsername(String username) {
        log.debug("Get user with username: " + username);
        return userRepository.findByUsername(username);
    }

    private AppUserEntity getAppUserByEmail(String email) {
        log.debug("Get user with username: " + email);
        return userRepository.findByEmail(email);
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}