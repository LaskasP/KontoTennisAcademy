package kontopoulos.rest.services.reservation;

import kontopoulos.rest.models.reservation.rest.ReservationRequest;

public interface ReservationService {
    void createReservation(ReservationRequest reservationRequest) throws Exception;
}
