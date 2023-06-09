package kontopoulos.rest.models.reservation.rest;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateReservationResponse {
    private LocalDate reservationDate;
    private String courtType;
    private LocalTime reservationStartTime;
    private LocalTime reservationEndTime;
}
