package kontopoulos.rest.models.security.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "application_users")
public class AppUserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id"))
    private Set<RoleEntity> roleEntities;
    //private Collection<String> authorities = new HashSet<>();
    private String profileImageUrl;
    private LocalDateTime lastLoginDate;
    private LocalDateTime lastLoginDateDisplay;
    private LocalDateTime joinDate;
    private boolean isActive;
    private boolean isNotLocked;

    public void addRole(RoleEntity roleEntity) {
        this.roleEntities.add(roleEntity);
    }

    public void removeRole(RoleEntity roleEntity) {
        this.roleEntities.remove(roleEntity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AppUserEntity appUserEntity = (AppUserEntity) o;
        return id != null && Objects.equals(id, appUserEntity.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}