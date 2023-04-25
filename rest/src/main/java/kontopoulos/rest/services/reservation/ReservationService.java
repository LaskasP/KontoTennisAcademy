package kontopoulos.rest.services.reservation;

import kontopoulos.rest.exceptions.AppUserNotFoundException;
import kontopoulos.rest.exceptions.InvalidRequestException;
import kontopoulos.rest.models.reservation.rest.CreateReservationRequest;
import kontopoulos.rest.models.reservation.rest.CreateReservationResponse;
import kontopoulos.rest.models.reservation.rest.GetAppUserReservationResponse;
import kontopoulos.rest.models.reservation.rest.GetFullReservation;

import java.util.List;

public interface ReservationService {
    CreateReservationResponse createReservation(CreateReservationRequest createReservationRequest) throws InvalidRequestException, AppUserNotFoundException;

    List<GetFullReservation> getPageFullReservations(int page);

    List<GetAppUserReservationResponse> getAppUserReservations(String username) throws InvalidRequestException, AppUserNotFoundException;

    void deleteAppUserReservation(String username, Long id) throws InvalidRequestException;
}
