package kontopoulos.rest.configs;

import kontopoulos.rest.models.security.entity.AppUserEntity;
import kontopoulos.rest.models.security.entity.RoleEntity;
import kontopoulos.rest.models.security.lov.AppUserRole;
import kontopoulos.rest.repos.RoleRepository;
import kontopoulos.rest.repos.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.Set;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private boolean alreadySetup = false;

    public DataLoader(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(@Nonnull ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        RoleEntity userRole = createRoleIfNotFound(AppUserRole.ROLE_USER);
        RoleEntity adminRole = createRoleIfNotFound(AppUserRole.ROLE_ADMIN);
        AppUserEntity user = new AppUserEntity();
        user.setFirstName("Fanis");
        user.setLastName("Kontopoulos");
        user.setUsername("FanisKonto");
        user.setPassword(passwordEncoder.encode("IRullzU!"));
        user.setEmail("fanis@kontopoulosacademy.com");
        user.setRoleEntities(Set.of(userRole, adminRole));
        user.setActive(true);
        user.setNotLocked(false);
        userRepository.save(user);
        alreadySetup = true;
    }

    @Transactional
    public RoleEntity createRoleIfNotFound(AppUserRole appUserRole) {
        RoleEntity role = roleRepository.findFirstByAppUserRole(appUserRole);
        if (role == null) {
            role = new RoleEntity();
            role.setAppUserRole(appUserRole);
            roleRepository.save(role);
        }
        return role;
    }
}
