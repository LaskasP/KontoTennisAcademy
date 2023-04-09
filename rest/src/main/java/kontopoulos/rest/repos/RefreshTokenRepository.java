package kontopoulos.rest.repos;


import kontopoulos.rest.models.security.entity.AppUserEntity;
import kontopoulos.rest.models.security.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    RefreshTokenEntity findByToken(String token);

    @Modifying
    int deleteByAppUserEntity(AppUserEntity appUserEntity);
}
