package kontopoulos.rest.models.reservation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "courts")
@Getter
public class CourtEntity {
    @Id
    private Long id;

    private String courtType;
}
