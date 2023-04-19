package kontopoulos.rest.repos;

import kontopoulos.rest.models.common.entity.TimeIntervalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Set;

@Repository
public interface TimeIntervalRepository extends JpaRepository<TimeIntervalEntity, Long> {
    @Query("select t from TimeIntervalEntity t where t.timeValue >= :startTime and t.timeValue < :endTime")
    Set<TimeIntervalEntity> findAllByTimeValueIsBetweenStartTimeAndEndTime(LocalTime startTime, LocalTime endTime);
}
