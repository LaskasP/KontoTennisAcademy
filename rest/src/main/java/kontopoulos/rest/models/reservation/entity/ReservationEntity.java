package kontopoulos.rest.models.reservation.entity;

import jakarta.persistence.*;
import kontopoulos.rest.models.common.TimeIntervalEntity;
import kontopoulos.rest.models.security.entity.AppUserEntity;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Table(name = "reservations")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "application_users_id")
    private AppUserEntity appUserEntity;

    private Long courtId;

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
