package kontopoulos.rest.models.security;

import kontopoulos.rest.models.security.entity.AppUserEntity;
import kontopoulos.rest.models.security.entity.RoleEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private final AppUserEntity appUserEntity;

    public UserDetailsImpl(AppUserEntity appUserEntity) {
        this.appUserEntity = appUserEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (RoleEntity roleEntity : appUserEntity.getRoleEntities()) {
            authorities.add(new SimpleGrantedAuthority(roleEntity.getAppUserRole().toString()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return appUserEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return appUserEntity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return appUserEntity.isNotLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return appUserEntity.isActive();
    }
}