package kontopoulos.rest.models.common;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "time_intervals")
public class TimeIntervalEntity {
    @Id
    private Long id;

    private LocalDate timeValue;
}
