package kontopoulos.rest.services.reservation.impl;

import kontopoulos.rest.exceptions.AppUserNotFoundException;
import kontopoulos.rest.exceptions.InvalidRequestException;
import kontopoulos.rest.models.reservation.entity.CourtEntity;
import kontopoulos.rest.models.reservation.entity.ReservationEntity;
import kontopoulos.rest.models.reservation.rest.CreateReservationRequest;
import kontopoulos.rest.models.security.AuthenticationFacade;
import kontopoulos.rest.models.security.entity.AppUserEntity;
import kontopoulos.rest.repos.AppUserRepository;
import kontopoulos.rest.repos.CourtRepository;
import kontopoulos.rest.repos.ReservationRepository;
import kontopoulos.rest.services.reservation.ReservationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    public static final String TIME_INTERVALS_NOT_FOUND = "TimeIntervals not found.";
    public static final String RESERVATION_TIMES_ARE_NOT_VALID = "reservationStartTime and reservationEndtime are not valid";
    public static final String PROVIDED_USERNAME_DOES_NOT_MATCH_AUTHENTICATED_USER = "Provided username does not match authenticated user";
    public static final int PAGE_SIZE = 65;
    private final ReservationRepository reservationRepository;
    private final AppUserRepository appUserRepository;
    private final CourtRepository courtRepository;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public void createReservation(CreateReservationRequest createReservationRequest) throws Exception {
        validateUsername(createReservationRequest.getUsername());
        AppUserEntity appUserEntity = appUserRepository.findByUsername(createReservationRequest.getUsername());
        if (appUserEntity == null) throw new AppUserNotFoundException();
        validateTimeIntervals(createReservationRequest.getReservationStartTime(), createReservationRequest.getReservationEndTime());
        CourtEntity courtEntity = courtRepository.findFirstByCourtType(createReservationRequest.getCourtEnum().toString().toLowerCase());
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setAppUserEntity(appUserEntity);
        reservationEntity.setReservationDate(createReservationRequest.getReservationDate());
        reservationEntity.setCourtEntity(courtEntity);
        reservationEntity.setReservationStartTime(createReservationRequest.getReservationStartTime());
        reservationEntity.setReservationEndTime(createReservationRequest.getReservationEndTime());
        reservationRepository.save(reservationEntity);
    }

    @Override
    public List<ReservationEntity> getNextReservations(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("reservationDate"));
        return reservationRepository.findByReservationDateAfter(LocalDate.now().minusDays(1), pageable);
    }

    private void validateTimeIntervals(LocalTime reservationStartTime, LocalTime reservationEndTime) throws InvalidRequestException {
        boolean isReservationTimeInvalid = (reservationStartTime.compareTo(reservationEndTime) >= 0) || (reservationStartTime.until(reservationEndTime, ChronoUnit.HOURS) < 1);
        if (isReservationTimeInvalid) {
            throw new InvalidRequestException(RESERVATION_TIMES_ARE_NOT_VALID);
        }
    }

    private void validateUsername(String username) throws InvalidRequestException {
        if (!authenticationFacade.getAuthentication().getName().equalsIgnoreCase(username))
            throw new InvalidRequestException(PROVIDED_USERNAME_DOES_NOT_MATCH_AUTHENTICATED_USER);
    }
}
