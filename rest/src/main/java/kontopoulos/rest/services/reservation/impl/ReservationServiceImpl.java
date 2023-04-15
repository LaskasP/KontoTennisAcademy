package kontopoulos.rest.services.reservation.impl;

import kontopoulos.rest.exceptions.AppUserNotFoundException;
import kontopoulos.rest.models.reservation.rest.ReservationRequest;
import kontopoulos.rest.models.security.entity.AppUserEntity;
import kontopoulos.rest.repos.AppUserRepository;
import kontopoulos.rest.repos.ReservationRepository;
import kontopoulos.rest.repos.TimeIntervalRepository;
import kontopoulos.rest.services.reservation.ReservationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private ReservationRepository reservationRepository;

    private AppUserRepository appUserRepository;

    private TimeIntervalRepository timeIntervalRepository;

    public void createReservation(ReservationRequest reservationRequest) throws AppUserNotFoundException {
        AppUserEntity appUserEntity = appUserRepository.findByUsername(reservationRequest.getUsername());
        if (appUserEntity == null) throw new AppUserNotFoundException("Username cannot be found.");
//        List<TimeIntervalEntity> timeIntervalEntities = timeIntervalRepository.findAllByTimeValueBetween();
    }
}
