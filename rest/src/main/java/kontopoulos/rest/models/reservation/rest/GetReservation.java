package kontopoulos.rest.models.reservation.rest;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class GetReservation {
    private Long id;
    private String username;
    private String profileImageUrl;
    private String courtType;
    private LocalDate reservationDate;
    private LocalTime reservationStartTime;
    private LocalTime reservationEndTime;
    private boolean availableForSecondPlayer;
}
