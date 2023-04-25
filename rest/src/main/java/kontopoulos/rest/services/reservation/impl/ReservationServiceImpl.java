package kontopoulos.rest.services.reservation.impl;

import kontopoulos.rest.exceptions.AppUserNotFoundException;
import kontopoulos.rest.exceptions.InvalidRequestException;
import kontopoulos.rest.models.common.rest.AppUserResponse;
import kontopoulos.rest.models.reservation.entity.CourtEntity;
import kontopoulos.rest.models.reservation.entity.ReservationEntity;
import kontopoulos.rest.models.reservation.rest.CreateReservationRequest;
import kontopoulos.rest.models.reservation.rest.CreateReservationResponse;
import kontopoulos.rest.models.reservation.rest.GetAppUserReservationResponse;
import kontopoulos.rest.models.reservation.rest.GetFullReservation;
import kontopoulos.rest.models.security.AuthenticationFacade;
import kontopoulos.rest.models.security.entity.AppUserEntity;
import kontopoulos.rest.repos.AppUserRepository;
import kontopoulos.rest.repos.CourtRepository;
import kontopoulos.rest.repos.ReservationRepository;
import kontopoulos.rest.services.reservation.ReservationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    public static final String RESERVATION_TIMES_ARE_NOT_VALID = "reservationStartTime and reservationEndTime are not valid";
    public static final String PROVIDED_USERNAME_DOES_NOT_MATCH_AUTHENTICATED_USER = "Provided username does not match authenticated user";
    public static final int PAGE_SIZE = 65;
    private final ReservationRepository reservationRepository;
    private final AppUserRepository appUserRepository;
    private final CourtRepository courtRepository;
    private final AuthenticationFacade authenticationFacade;
    private final ModelMapper modelMapper;

    @Override
    public CreateReservationResponse createReservation(CreateReservationRequest createReservationRequest) throws InvalidRequestException, AppUserNotFoundException {
        validateUsername(createReservationRequest.getUsername());
        AppUserEntity appUserEntity = appUserRepository.findByUsername(createReservationRequest.getUsername());
        if (appUserEntity == null) throw new AppUserNotFoundException();
        validateTimeIntervals(createReservationRequest.getReservationStartTime(), createReservationRequest.getReservationEndTime());
        CourtEntity courtEntity = courtRepository.findFirstByCourtType(createReservationRequest.getCourtEnum().toString().toLowerCase());
        ReservationEntity reservationEntity = convertToEntity(createReservationRequest, appUserEntity, courtEntity);
        ReservationEntity insertedReservationEntity = reservationRepository.save(reservationEntity);
        return convertToDto(insertedReservationEntity);
    }

    @Override
    public List<GetFullReservation> getPageFullReservations(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("reservationDate"));
        List<ReservationEntity> reservationEntityList = reservationRepository.findByReservationDateAfter(LocalDate.now().minusDays(1), pageable);
        return convertEntityListToFullResponseList(reservationEntityList);
    }

    private List<GetFullReservation> convertEntityListToFullResponseList(List<ReservationEntity> reservationEntityList) {
        List<GetFullReservation> getFullReservationList = new ArrayList<>();
        for (ReservationEntity reservationEntity : reservationEntityList) {
            GetFullReservation getFullReservation = modelMapper.map(reservationEntity, GetFullReservation.class);
            getFullReservation.setCourtType(reservationEntity.getCourtEntity().getCourtType());
            getFullReservation.setAppUserResponse(modelMapper.map(reservationEntity.getAppUserEntity(), AppUserResponse.class));
            getFullReservationList.add(getFullReservation);
        }
        return getFullReservationList;
    }

    @Override
    public List<GetAppUserReservationResponse> getAppUserReservations(String username) throws InvalidRequestException, AppUserNotFoundException {
        validateUsername(username);
        AppUserEntity appUserEntity = appUserRepository.findByUsername(username);
        if (appUserEntity == null) throw new AppUserNotFoundException();
        List<ReservationEntity> reservationEntityList = reservationRepository.findByAppUserEntity(appUserEntity);
        return convertEntityListToResponseList(reservationEntityList);
    }

    @Override
    public void deleteAppUserReservation(String username, Long id) throws InvalidRequestException {
        validateUsername(username);
        reservationRepository.deleteById(id);
    }

    private List<GetAppUserReservationResponse> convertEntityListToResponseList(List<ReservationEntity> reservationEntityList) {
        List<GetAppUserReservationResponse> getAppUserReservationResponseList = new ArrayList<>();
        for (ReservationEntity reservationEntity : reservationEntityList) {
            GetAppUserReservationResponse getAppUserReservationResponseItem = modelMapper.map(reservationEntity, GetAppUserReservationResponse.class);
            getAppUserReservationResponseItem.setCourtType(reservationEntity.getCourtEntity().getCourtType());
            getAppUserReservationResponseList.add(getAppUserReservationResponseItem);
        }
        return getAppUserReservationResponseList;
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

    private CreateReservationResponse convertToDto(ReservationEntity insertedReservationEntity) {
        CreateReservationResponse createReservationResponse = modelMapper.map(insertedReservationEntity, CreateReservationResponse.class);
        createReservationResponse.setCourtType(insertedReservationEntity.getCourtEntity().getCourtType());
        return createReservationResponse;
    }

    private ReservationEntity convertToEntity(CreateReservationRequest createReservationRequest, AppUserEntity appUserEntity, CourtEntity courtEntity) {
        ReservationEntity reservationEntity = modelMapper.map(createReservationRequest, ReservationEntity.class);
        reservationEntity.setAppUserEntity(appUserEntity);
        reservationEntity.setCourtEntity(courtEntity);
        return reservationEntity;
    }
}
