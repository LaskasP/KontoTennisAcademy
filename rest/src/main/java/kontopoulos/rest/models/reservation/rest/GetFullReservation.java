package kontopoulos.rest.models.reservation.rest;

import kontopoulos.rest.models.common.rest.AppUserResponse;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class GetFullReservation {
    private Long id;
    private AppUserResponse player;
    private String CourtType;
    private LocalDate reservationDate;
    private LocalTime reservationStartTime;
    private LocalTime reservationEndTime;
    private boolean availableForSecondPlayer;
    private AppUserResponse secondPlayer;
}
