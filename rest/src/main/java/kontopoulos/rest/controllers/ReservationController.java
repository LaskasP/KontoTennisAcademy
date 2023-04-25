package kontopoulos.rest.controllers;

import jakarta.validation.Valid;
import kontopoulos.rest.exceptions.AppUserNotFoundException;
import kontopoulos.rest.exceptions.InvalidRequestException;
import kontopoulos.rest.models.reservation.rest.CreateReservationRequest;
import kontopoulos.rest.models.reservation.rest.CreateReservationResponse;
import kontopoulos.rest.models.reservation.rest.GetAppUserReservationResponse;
import kontopoulos.rest.services.reservation.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
@AllArgsConstructor
@Validated
public class ReservationController {

    public static final String USERNAME_PATH = "/{username}";
    public static final String USERNAME = "username";
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<CreateReservationResponse> createReservation(@Valid @RequestBody CreateReservationRequest createReservationRequest) throws InvalidRequestException, AppUserNotFoundException {
        CreateReservationResponse createReservationResponse = reservationService.createReservation(createReservationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createReservationResponse);
    }

    @GetMapping(USERNAME_PATH)
    public ResponseEntity<List<GetAppUserReservationResponse>> getAppUserReservations(@PathVariable(value = USERNAME) String username) throws InvalidRequestException, AppUserNotFoundException {
        List<GetAppUserReservationResponse> GetAppUserReservationResponseList = reservationService.getAppUserReservations(username);
        return ResponseEntity.ok(GetAppUserReservationResponseList);
    }
}
