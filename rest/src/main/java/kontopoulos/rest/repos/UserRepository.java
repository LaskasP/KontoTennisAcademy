package kontopoulos.rest.repos;


import kontopoulos.rest.models.security.entity.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUserEntity, Long> {
    AppUserEntity findByUsername(String username);

    AppUserEntity findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}