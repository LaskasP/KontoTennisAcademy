package kontopoulos.rest.models.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalTime;

import static kontopoulos.rest.models.common.TimeIntervalConstant.TIME_SLOT_COLUMN;

@Entity
@Getter
@Table(name = "time_intervals")
public class TimeIntervalEntity {

    @Id
    private Long id;
    @Column(name = TIME_SLOT_COLUMN)
    private LocalTime timeSlot;
}
