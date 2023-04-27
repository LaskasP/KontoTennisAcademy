package kontopoulos.rest.models.reservation.entity;

import jakarta.persistence.*;
import kontopoulos.rest.models.security.entity.AppUserEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "reservations")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    private AppUserEntity appUserEntity;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "court_id", referencedColumnName = "id")
    private CourtEntity courtEntity;

    private Boolean availableForSecondPlayer;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "second_player_id", referencedColumnName = "id")
    private AppUserEntity secondAppUserEntity = null;

    private LocalDate reservationDate;

    private LocalTime reservationStartTime;

    private LocalTime reservationEndTime;
}
