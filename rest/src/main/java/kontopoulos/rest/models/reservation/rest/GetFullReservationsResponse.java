package kontopoulos.rest.models.reservation.rest;

import lombok.Data;

import java.util.List;

@Data
public class GetFullReservationsResponse {

    private List<GetFullReservation> fullReservationsList;

    public GetFullReservationsResponse(List<GetFullReservation> fullReservationsList) {
        this.fullReservationsList = fullReservationsList;
    }
}
