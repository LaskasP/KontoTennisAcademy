package kontopoulos.rest.models.reservation.rest;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class GetFullReservation {
    private Long id;
    private GetFullReservationAppUserResponse player;
    private String courtType;
    private LocalDate reservationDate;
    private LocalTime reservationStartTime;
    private LocalTime reservationEndTime;
    private boolean availableForSecondPlayer;
    private GetFullReservationAppUserResponse secondPlayer;
}
