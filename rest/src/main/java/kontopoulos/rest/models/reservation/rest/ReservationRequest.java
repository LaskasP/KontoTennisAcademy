package kontopoulos.rest.models.reservation.rest;

import jakarta.validation.constraints.NotBlank;
import kontopoulos.rest.models.reservation.lov.CourtEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservationRequest {
    @NotBlank(message = "username cannot be blank.")
    private String username;
    @NotBlank(message = "courtEnum cannot be blank.")
    private CourtEnum courtEnum;
    @NotBlank(message = "reservationDate cannot be blank.")
    private LocalDate reservationDate;
    @NotBlank(message = "reservationStartTime cannot be blank.")
    private LocalTime reservationStartTime;
    @NotBlank(message = "reservationEndTime cannot be blank.")
    private LocalTime reservationEndTime;
}
