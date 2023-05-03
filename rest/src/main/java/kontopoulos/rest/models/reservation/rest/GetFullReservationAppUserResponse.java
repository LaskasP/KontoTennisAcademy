package kontopoulos.rest.models.reservation.rest;

import lombok.Data;

@Data
public class GetFullReservationAppUserResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String profileImageUrl;
}
