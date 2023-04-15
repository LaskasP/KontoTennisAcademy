package kontopoulos.rest.models.reservation.rest;

import kontopoulos.rest.models.reservation.lov.CourtEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservationResponse {
    private String username;
    private LocalDate registrationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private CourtEnum courtType;
}
