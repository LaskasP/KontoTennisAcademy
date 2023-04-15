package kontopoulos.rest.repos;

import kontopoulos.rest.models.common.TimeIntervalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeIntervalRepository extends JpaRepository<TimeIntervalEntity, Long> {
    public List<TimeIntervalEntity> findAllByTimeValueBetween(LocalDate startTime, LocalDate endTime);
}
