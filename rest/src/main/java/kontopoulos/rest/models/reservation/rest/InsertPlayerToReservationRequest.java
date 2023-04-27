package kontopoulos.rest.models.reservation.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InsertPlayerToReservationRequest {
    @NotBlank(message = "username cannot be blank")
    private String username;
    @NotNull(message = "reservationId cannot be null")
    private Long reservationId;
}
