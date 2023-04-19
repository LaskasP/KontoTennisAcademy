package kontopoulos.rest.services.reservation.impl;

import kontopoulos.rest.exceptions.AppUserNotFoundException;
import kontopoulos.rest.exceptions.InvalidRequestException;
import kontopoulos.rest.models.common.entity.TimeIntervalEntity;
import kontopoulos.rest.models.reservation.entity.CourtEntity;
import kontopoulos.rest.models.reservation.entity.ReservationEntity;
import kontopoulos.rest.models.reservation.rest.ReservationRequest;
import kontopoulos.rest.models.security.entity.AppUserEntity;
import kontopoulos.rest.repos.AppUserRepository;
import kontopoulos.rest.repos.CourtRepository;
import kontopoulos.rest.repos.ReservationRepository;
import kontopoulos.rest.repos.TimeIntervalRepository;
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
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    public static final String TIME_INTERVALS_NOT_FOUND = "TimeIntervals not found.";
    public static final String RESERVATION_TIMES_ARE_NOT_VALID = "reservationStartTime and reservationEndtime are not valid";
    public static final int PAGE_SIZE = 65;

    private final ReservationRepository reservationRepository;

    private final AppUserRepository appUserRepository;

    private final TimeIntervalRepository timeIntervalRepository;

    private final CourtRepository courtRepository;

    @Override
    public void createReservation(ReservationRequest reservationRequest) throws Exception {
        AppUserEntity appUserEntity = appUserRepository.findByUsername(reservationRequest.getUsername());
        if (appUserEntity == null) throw new AppUserNotFoundException();
        validateTimeIntervals(reservationRequest.getReservationStartTime(), reservationRequest.getReservationEndTime());
        Set<TimeIntervalEntity> timeIntervalEntities = timeIntervalRepository.findAllByTimeValueIsBetweenStartTimeAndEndTime(reservationRequest.getReservationStartTime(), reservationRequest.getReservationEndTime());
        if (timeIntervalEntities == null) throw new InvalidRequestException(TIME_INTERVALS_NOT_FOUND);
        CourtEntity courtEntity = courtRepository.findFirstByCourtType(reservationRequest.getCourtEnum().toString().toLowerCase());
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setAppUserEntity(appUserEntity);
        reservationEntity.setReservationDate(reservationRequest.getReservationDate());
        reservationEntity.setCourtEntity(courtEntity);
        reservationEntity.setTimeIntervalEntities(timeIntervalEntities);
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

}
