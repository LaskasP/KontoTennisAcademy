package kontopoulos.rest.models.reservation.entity;

import jakarta.persistence.*;
import kontopoulos.rest.models.common.entity.TimeIntervalEntity;
import kontopoulos.rest.models.security.entity.AppUserEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "reservations")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUserEntity appUserEntity;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "court_id", referencedColumnName = "id")
    private CourtEntity courtEntity;

    private LocalDate reservationDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "reservations_time_intervals",
            joinColumns = @JoinColumn(
                    name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "time_interval_id"))
    private Set<TimeIntervalEntity> timeIntervalEntities;
}
