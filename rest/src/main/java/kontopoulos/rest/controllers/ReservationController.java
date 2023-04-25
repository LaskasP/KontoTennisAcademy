package kontopoulos.rest.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kontopoulos.rest.exceptions.AppUserNotFoundException;
import kontopoulos.rest.exceptions.InvalidRequestException;
import kontopoulos.rest.models.reservation.rest.CreateReservationRequest;
import kontopoulos.rest.models.reservation.rest.CreateReservationResponse;
import kontopoulos.rest.models.reservation.rest.GetAppUserReservationResponse;
import kontopoulos.rest.models.reservation.rest.GetFullReservation;
import kontopoulos.rest.services.reservation.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
    public static final String USERNAME_ID_PATH = "/{username}/{id}";
    public static final String SECURED_ROLE_ADMIN = "ROLE_ADMIN";
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<CreateReservationResponse> createReservation(@Valid @RequestBody CreateReservationRequest createReservationRequest) throws InvalidRequestException, AppUserNotFoundException {
        CreateReservationResponse createReservationResponse = reservationService.createReservation(createReservationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createReservationResponse);
    }

    @GetMapping(USERNAME_PATH)
    public ResponseEntity<List<GetAppUserReservationResponse>> getAppUserReservations(@PathVariable(value = USERNAME) @NotBlank String username) throws InvalidRequestException, AppUserNotFoundException {
        List<GetAppUserReservationResponse> GetAppUserReservationResponseList = reservationService.getAppUserReservations(username);
        return ResponseEntity.ok(GetAppUserReservationResponseList);
    }

    @DeleteMapping(USERNAME_ID_PATH)
    public ResponseEntity<Void> deleteAppUserReservation(@PathVariable(value = USERNAME) @NotBlank String username, @PathVariable @NotBlank Long id) throws InvalidRequestException, AppUserNotFoundException {
        reservationService.deleteAppUserReservation(username, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Secured(SECURED_ROLE_ADMIN)
    public ResponseEntity<List<GetFullReservation>> getReservations(@NotNull(message = "page cannot be null") int page) {
        List<GetFullReservation> getFullReservationList = reservationService.getPageFullReservations(page);
        return ResponseEntity.ok(getFullReservationList);
    }
}
