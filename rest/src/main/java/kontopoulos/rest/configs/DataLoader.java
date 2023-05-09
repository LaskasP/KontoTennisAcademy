package kontopoulos.rest.configs;

import kontopoulos.rest.models.security.entity.AppUserEntity;
import kontopoulos.rest.models.security.entity.RoleEntity;
import kontopoulos.rest.models.security.lov.AppUserRoleEnum;
import kontopoulos.rest.repos.AppUserRepository;
import kontopoulos.rest.repos.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.Set;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private boolean alreadySetup = false;

    @Value("${admin.pass}")
    private String password;

    @Value("${admin.email}")
    private String email;

    public DataLoader(AppUserRepository appUserRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(@Nonnull ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        RoleEntity userRole = createRoleIfNotFound(AppUserRoleEnum.ROLE_USER);
        RoleEntity adminRole = createRoleIfNotFound(AppUserRoleEnum.ROLE_ADMIN);
        RoleEntity sysAdminRole = createRoleIfNotFound(AppUserRoleEnum.ROLE_SYSTEM_ADMIN);
        AppUserEntity user = new AppUserEntity();
        user.setFirstName("Sys");
        user.setLastName("Admin");
        user.setUsername("sysAdmin");
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRoleEntities(Set.of(userRole, adminRole, sysAdminRole));
        user.setActive(true);
        user.setNotLocked(false);
        appUserRepository.save(user);
        alreadySetup = true;
    }

    @Transactional
    public RoleEntity createRoleIfNotFound(AppUserRoleEnum appUserRoleEnum) {
        RoleEntity role = roleRepository.findFirstByAppUserRoleEnum(appUserRoleEnum);
        if (role == null) {
            role = new RoleEntity();
            role.setAppUserRoleEnum(appUserRoleEnum);
            roleRepository.save(role);
        }
        return role;
    }
}
