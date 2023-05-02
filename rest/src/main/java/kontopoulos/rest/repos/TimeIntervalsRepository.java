package kontopoulos.rest.repos;

import kontopoulos.rest.models.common.entity.TimeIntervalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeIntervalsRepository extends JpaRepository<TimeIntervalEntity, Long> {
}
