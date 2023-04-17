package kontopoulos.rest.models.reservation.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kontopoulos.rest.models.reservation.lov.CourtEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservationRequest {
    @NotBlank(message = "username cannot be blank.")
    private String username;
    @NotNull(message = "courtEnum cannot be blank.")
    private CourtEnum courtEnum;
    @NotNull(message = "reservationDate cannot be blank.")
    private LocalDate reservationDate;
    @NotNull(message = "reservationStartTime cannot be blank.")
    private LocalTime reservationStartTime;
    @NotNull(message = "reservationEndTime cannot be blank.")
    private LocalTime reservationEndTime;
}
