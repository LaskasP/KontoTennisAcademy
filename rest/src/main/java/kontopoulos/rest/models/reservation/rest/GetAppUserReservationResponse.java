package kontopoulos.rest.models.reservation.rest;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class GetAppUserReservationResponse {
    private Long id;
    private LocalDate reservationDate;
    private String playerUsername;
    private String courtType;
    private LocalTime reservationStartTime;
    private LocalTime reservationEndTime;
    private Boolean availableForSecondPlayer;
    private String secondPlayerUsername;
}
