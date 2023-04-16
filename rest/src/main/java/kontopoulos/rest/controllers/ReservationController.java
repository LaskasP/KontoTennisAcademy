package kontopoulos.rest.controllers;

import jakarta.validation.Valid;
import kontopoulos.rest.models.reservation.rest.ReservationRequest;
import kontopoulos.rest.models.security.rest.response.RegisterResponse;
import kontopoulos.rest.services.reservation.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservation")
@AllArgsConstructor
@Validated
public class ReservationController {

    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<RegisterResponse> createReservation(@Valid @RequestBody ReservationRequest reservationRequest) throws Exception {
        reservationService.createReservation(reservationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
