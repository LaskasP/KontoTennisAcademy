package kontopoulos.rest.services.reservation.impl;

import kontopoulos.rest.exceptions.AppUserNotFoundException;
import kontopoulos.rest.models.common.TimeIntervalEntity;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    private final AppUserRepository appUserRepository;

    private final TimeIntervalRepository timeIntervalRepository;

    private final CourtRepository courtRepository;

    @Override
    public void createReservation(ReservationRequest reservationRequest) throws Exception {
        AppUserEntity appUserEntity = appUserRepository.findByUsername(reservationRequest.getUsername());
        if (appUserEntity == null) throw new AppUserNotFoundException("Username cannot be found.");
        Set<TimeIntervalEntity> timeIntervalEntities = timeIntervalRepository.findAllByTimeValueIsBetweenStartTimeAndEndTime(reservationRequest.getReservationStartTime(), reservationRequest.getReservationEndTime());
        //Todo: Validate timeIntervals
        if (timeIntervalEntities == null) throw new Exception("TimeIntervals not found.");
        CourtEntity courtEntity = courtRepository.findFirstByCourtType(reservationRequest.getCourtEnum().toString().toLowerCase());
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setAppUserEntity(appUserEntity);
        reservationEntity.setReservationDate(reservationRequest.getReservationDate());
        reservationEntity.setCourtEntity(courtEntity);
        reservationEntity.setTimeIntervalEntities(timeIntervalEntities);
        reservationRepository.save(reservationEntity);
    }
}
