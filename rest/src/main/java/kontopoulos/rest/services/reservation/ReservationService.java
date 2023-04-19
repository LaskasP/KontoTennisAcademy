package kontopoulos.rest.services.reservation;

import kontopoulos.rest.models.reservation.entity.ReservationEntity;
import kontopoulos.rest.models.reservation.rest.CreateReservationRequest;

import java.util.List;

public interface ReservationService {
    void createReservation(CreateReservationRequest createReservationRequest) throws Exception;

    List<ReservationEntity> getNextReservations(int page);
}
