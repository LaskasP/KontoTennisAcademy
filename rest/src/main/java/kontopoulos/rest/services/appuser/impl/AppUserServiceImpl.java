package kontopoulos.rest.services.appuser.impl;



import kontopoulos.rest.exceptions.AppUserNotFoundException;
import kontopoulos.rest.exceptions.EmailExistsException;
import kontopoulos.rest.exceptions.EmailNotFoundException;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImpl implements AppUserService {
    public static final String NO_ACCOUNT_FOUND_WITH_THIS_USERNAME = "No account found with this username: ";
    public static final String ACCOUNT_ALREADY_EXISTS_WITH_USERNAME = "Account already exists with Username: ";
    public static final String ACCOUNT_ALREADY_EXISTS_WITH_EMAIL = "Account already exists with Email: ";
    public static final String NO_ACCOUNT_FOUND_WITH_THIS_EMAIL = "No account found with this email:";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;

    @Override
    public RegisterResponseWrapper registerAppUser(RegisterRequest registerRequest) throws UsernameExistsException, EmailExistsException {
        log.info("registerAppUser");
        log.debug("Begin Saving appUserRequest with username: " + registerRequest.getUsername());
        validateNewUsernameAndEmail(registerRequest.getUsername(), registerRequest.getEmail());
        AppUserEntity newAppUserEntity = modelMapper.map(registerRequest, AppUserEntity.class);
        newAppUserEntity.setRoleEntities(Set.of(roleRepository.findFirstByAppUserRole(AppUserRole.ROLE_USER)));
        newAppUserEntity.setJoinDate(new Date());
        newAppUserEntity.setActive(true);
        newAppUserEntity.setNotLocked(true);
        newAppUserEntity.setPassword(getEncodedPassword(registerRequest.getPassword()));
        newAppUserEntity.setProfileImageUrl(getTempImg());
        log.debug("End Saved newAppUser with username: " + newAppUserEntity.getUsername());
        //return modelMapper.map(userRepository.save(newAppUserEntity), RegisterResponse.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(newAppUserEntity);
        String httpHeader = jwtProvider.generateJWT(userDetails);
        RegisterResponse registerResponse = modelMapper.map(userRepository.save(newAppUserEntity), RegisterResponse.class);
        List<AppUserRole> roles = new ArrayList<>();
        for (RoleEntity roleEntity : newAppUserEntity.getRoleEntities()) {
            roles.add(roleEntity.getAppUserRole());
        }
        registerResponse.setRoles(roles);
        return new RegisterResponseWrapper(httpHeader, registerResponse);
    }

    private String getTempImg() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/image/temp").toUriString();
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        authenticate(changePasswordRequest.getUsername(), changePasswordRequest.getPassword());
        AppUserEntity appUserEntity = getAppUserByUsername(changePasswordRequest.getUsername());
        if (appUserEntity != null) {
            appUserEntity.setPassword(getEncodedPassword(changePasswordRequest.getNewPassword()));
            userRepository.save(appUserEntity);
        }
    }

    @Override
    public LoginResponseWrapper loginAppUser(LoginRequest loginRequest) {
        authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        AppUserEntity appUserEntity = getAppUserByUsername(loginRequest.getUsername());
        UserDetailsImpl userDetails = new UserDetailsImpl(appUserEntity);
        String httpHeader = jwtProvider.generateJWT(userDetails);
        LoginResponse loginResponse = modelMapper.map(appUserEntity, LoginResponse.class);
        List<AppUserRole> roles = new ArrayList<>();
        for (RoleEntity roleEntity : appUserEntity.getRoleEntities()) {
            roles.add(roleEntity.getAppUserRole());
        }
        loginResponse.setRoles(roles);
        return new LoginResponseWrapper(httpHeader, loginResponse);
    }

    @Override
    public void givePremRole(String username) {
        AppUserEntity appUserEntity = getAppUserByUsername(username);
        appUserEntity.addRole(roleRepository.findFirstByAppUserRole(AppUserRole.ROLE_PREMIUM_USER));
        userRepository.save(appUserEntity);
    }

    private String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private void validateChangeOfUsername(String currentUsername, String newUsername) throws UsernameExistsException, AppUserNotFoundException {
        AppUserEntity appUserEntityByUsername = getAppUserByUsername(currentUsername);
        if (appUserEntityByUsername == null) {
            throw new AppUserNotFoundException(NO_ACCOUNT_FOUND_WITH_THIS_USERNAME + currentUsername);
        }
        if (getAppUserByUsername(newUsername) != null) {
            throw new UsernameExistsException(ACCOUNT_ALREADY_EXISTS_WITH_USERNAME + newUsername);
        }
        appUserEntityByUsername.setUsername(newUsername);
        userRepository.save(appUserEntityByUsername);
    }

    private void validateNewUsernameAndEmail(String newUsername, String newEmail) throws UsernameExistsException, EmailExistsException {
        AppUserEntity appUserEntityByNewUsername = getAppUserByUsername(newUsername);
        if (appUserEntityByNewUsername != null) {
            throw new UsernameExistsException(ACCOUNT_ALREADY_EXISTS_WITH_USERNAME + newUsername);
        }
        AppUserEntity appUserEntityByNewEmail = getAppUserByEmail(newEmail);
        if (appUserEntityByNewEmail != null) {
            throw new EmailExistsException(ACCOUNT_ALREADY_EXISTS_WITH_EMAIL + newEmail);
        }
    }

    private void validateCurrentUsername(String username, String email) throws AppUserNotFoundException, EmailNotFoundException {
        if (getAppUserByUsername(username) == null) {
            throw new AppUserNotFoundException(NO_ACCOUNT_FOUND_WITH_THIS_USERNAME + username);
        }
        if (getAppUserByEmail(email) == null) {
            throw new EmailNotFoundException(NO_ACCOUNT_FOUND_WITH_THIS_EMAIL + email);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AppUserEntity getAppUserByUsername(String username) {
        log.info("Get user with username: " + username);
        return userRepository.findByUsername(username);
    }

    @Override
    public AppUserEntity getAppUserByEmail(String email) {
        log.info("Get user with username: " + email);
        return userRepository.findByEmail(email);
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}