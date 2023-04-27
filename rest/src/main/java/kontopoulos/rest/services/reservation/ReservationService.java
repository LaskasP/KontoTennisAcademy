package kontopoulos.rest.services.reservation;

import kontopoulos.rest.exceptions.AppUserNotFoundException;
import kontopoulos.rest.exceptions.InvalidRequestException;
import kontopoulos.rest.models.reservation.rest.*;

import java.util.List;

public interface ReservationService {
    CreateReservationResponse createReservation(CreateReservationRequest createReservationRequest) throws InvalidRequestException, AppUserNotFoundException;

    List<GetFullReservation> getPageFullOfReservations(int page);

    List<GetAppUserReservationResponse> getAppUserReservations(String username) throws InvalidRequestException, AppUserNotFoundException;

    void deleteAppUserReservation(String username, Long id) throws InvalidRequestException;

    InsertPlayerToReservationResponse insertSecondPlayerToReservation(InsertPlayerToReservationRequest insertPlayerToReservationRequest) throws InvalidRequestException, AppUserNotFoundException;
}
