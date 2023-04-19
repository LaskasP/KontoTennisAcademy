package kontopoulos.rest.models.reservation.rest;

import kontopoulos.rest.models.common.rest.AppUserResponse;
import kontopoulos.rest.models.common.rest.TimeIntervalResponse;
import kontopoulos.rest.models.reservation.lov.CourtEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationResponse {
    private Long id;
    private LocalDate reservationDate;
    private CourtEnum courtType;
    private AppUserResponse appUserResponse;
    private TimeIntervalResponse timeIntervalResponse;
}
