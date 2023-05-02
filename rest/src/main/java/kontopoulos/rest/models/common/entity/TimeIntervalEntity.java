package kontopoulos.rest.models.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalTime;

@Entity
@Getter
@Table(name = "time_intervals")
public class TimeIntervalEntity {
    @Id
    private Long id;
    @Column(name = "time_value")
    private LocalTime timeSlot;
}
