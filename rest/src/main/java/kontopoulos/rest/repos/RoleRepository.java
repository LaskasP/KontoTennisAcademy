package kontopoulos.rest.repos;


import kontopoulos.rest.models.security.entity.RoleEntity;
import kontopoulos.rest.models.security.lov.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findFirstByAppUserRole(AppUserRole appUserRole);
}