package kontopoulos.rest.services.reservation;

import kontopoulos.rest.models.reservation.entity.ReservationEntity;
import kontopoulos.rest.models.reservation.rest.ReservationRequest;

import java.util.List;

public interface ReservationService {
    void createReservation(ReservationRequest reservationRequest) throws Exception;

    List<ReservationEntity> getNextReservations(int page);
}
