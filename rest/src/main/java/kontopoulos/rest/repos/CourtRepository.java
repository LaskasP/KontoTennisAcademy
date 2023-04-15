package kontopoulos.rest.repos;

import kontopoulos.rest.models.reservation.entity.CourtEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtRepository extends JpaRepository<CourtEntity, Long> {
    public CourtEntity findFirstByCourtType(String courtType);
}
