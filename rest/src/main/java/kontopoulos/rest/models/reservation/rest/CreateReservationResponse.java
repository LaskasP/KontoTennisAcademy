package kontopoulos.rest.models.reservation.rest;

import kontopoulos.rest.models.common.rest.AppUserResponse;
import kontopoulos.rest.models.reservation.lov.CourtEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateReservationResponse {
    private Long id;
    private LocalDate reservationDate;
    private CourtEnum courtType;
    private AppUserResponse appUserResponse;
    private LocalTime reservationStartTime;
    private LocalTime reservationEndTime;
}
