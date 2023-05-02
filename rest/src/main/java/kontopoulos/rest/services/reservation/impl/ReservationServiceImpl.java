package kontopoulos.rest.services.reservation.impl;

import kontopoulos.rest.exceptions.AppUserNotFoundException;
import kontopoulos.rest.exceptions.InvalidRequestException;
import kontopoulos.rest.models.common.rest.AppUserResponse;
import kontopoulos.rest.models.reservation.entity.CourtEntity;
import kontopoulos.rest.models.reservation.entity.ReservationEntity;
import kontopoulos.rest.models.reservation.rest.*;
import kontopoulos.rest.models.security.entity.AppUserEntity;
import kontopoulos.rest.repos.AppUserRepository;
import kontopoulos.rest.repos.CourtRepository;
import kontopoulos.rest.repos.ReservationRepository;
import kontopoulos.rest.services.reservation.ReservationService;
import kontopoulos.rest.utils.AuthenticationFacade;
import kontopoulos.rest.utils.TimeIntervalHelper;
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
    public static final String RESERVATION_ID_DOES_NOT_EXISTS = "ReservationId does not exists";
    public static final String RESERVATION_NOT_AVAILABLE_FOR_SECOND_PLAYER = "Reservation not available for second player";
    public static final String YOU_CANNOT_ADD_YOUR_SELF_AS_SECOND_PLAYER = "You cannot add your self as second player";
    public static final String SORT_BY_RESERVATION_DATE = "reservationDate";
    private final ReservationRepository reservationRepository;
    private final AppUserRepository appUserRepository;
    private final CourtRepository courtRepository;
    private final TimeIntervalHelper timeIntervalHelper;
    private final AuthenticationFacade authenticationFacade;
    private final ModelMapper modelMapper;

    @Override
    public CreateReservationResponse createReservation(CreateReservationRequest createReservationRequest) throws InvalidRequestException, AppUserNotFoundException {
        validateUsername(createReservationRequest.getUsername());
        AppUserEntity appUserEntity = appUserRepository.findByUsername(createReservationRequest.getUsername());
        if (appUserEntity == null) throw new AppUserNotFoundException();
        validateTimeIntervals(createReservationRequest.getReservationStartTime(), createReservationRequest.getReservationEndTime());
        CourtEntity courtEntity = courtRepository.findFirstByCourtType(createReservationRequest.getCourtEnum().toString().toLowerCase());
        ReservationEntity reservationEntity = convertCreateReservationRequestToReservationEntity(createReservationRequest, appUserEntity, courtEntity);
        ReservationEntity insertedReservationEntity = reservationRepository.save(reservationEntity);
        return convertReservationEntityToCreateReservationResponse(insertedReservationEntity);
    }

    @Override
    public List<GetFullReservation> getPageFullOfReservations(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(SORT_BY_RESERVATION_DATE));
        List<ReservationEntity> reservationEntityList = reservationRepository.findByReservationDateAfter(LocalDate.now().minusDays(1), pageable);
        return convertEntityListToFullResponseList(reservationEntityList);
    }

    @Override
    public List<GetReservation> getPageOfReservations(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(SORT_BY_RESERVATION_DATE));
        List<ReservationEntity> reservationEntityList = reservationRepository.findByReservationDateAfter(LocalDate.now().minusDays(1), pageable);
        return convertEntityListToResponseList(reservationEntityList);
    }

    private List<GetReservation> convertEntityListToResponseList(List<ReservationEntity> reservationEntityList) {
        List<GetReservation> getReservationList = new ArrayList<>();
        for (ReservationEntity reservationEntity : reservationEntityList) {
            GetReservation getReservation = modelMapper.map(reservationEntity, GetReservation.class);
            getReservation.setCourtType(reservationEntity.getCourtEntity().getCourtType());
            getReservation.setUsername(reservationEntity.getAppUserEntity().getUsername());
            getReservation.setProfileImageUrl(reservationEntity.getAppUserEntity().getProfileImageUrl());
            getReservationList.add(getReservation);
        }
        return getReservationList;
    }

    private List<GetFullReservation> convertEntityListToFullResponseList(List<ReservationEntity> reservationEntityList) {
        List<GetFullReservation> getFullReservationList = new ArrayList<>();
        for (ReservationEntity reservationEntity : reservationEntityList) {
            GetFullReservation getFullReservation = modelMapper.map(reservationEntity, GetFullReservation.class);
            getFullReservation.setCourtType(reservationEntity.getCourtEntity().getCourtType());
            getFullReservation.setPlayer(modelMapper.map(reservationEntity.getAppUserEntity(), AppUserResponse.class));
            if (reservationEntity.getSecondAppUserEntity() != null) {
                getFullReservation.setSecondPlayer(modelMapper.map(reservationEntity.getSecondAppUserEntity(), AppUserResponse.class));
            }
            getFullReservationList.add(getFullReservation);
        }
        return getFullReservationList;
    }

    @Override
    public List<GetAppUserReservationResponse> getAppUserReservations(String username) throws InvalidRequestException, AppUserNotFoundException {
        validateUsername(username);
        AppUserEntity appUserEntity = appUserRepository.findByUsername(username);
        if (appUserEntity == null) throw new AppUserNotFoundException();
        List<ReservationEntity> reservationEntityList = reservationRepository.findByAppUserEntityOrSecondAppUserEntity(appUserEntity, appUserEntity);
        return convertReservationEntityListToGetAppUserReservationResponseList(reservationEntityList);
    }

    @Override
    public void deleteAppUserReservation(String username, Long id) throws InvalidRequestException {
        validateUsername(username);
        reservationRepository.deleteById(id);
    }

    @Override
    public InsertPlayerToReservationResponse insertSecondPlayerToReservation(InsertPlayerToReservationRequest insertPlayerToReservationRequest) throws InvalidRequestException, AppUserNotFoundException {
        validateUsername(insertPlayerToReservationRequest.getUsername());
        AppUserEntity secondPlayer = appUserRepository.findByUsername(insertPlayerToReservationRequest.getUsername());
        if (secondPlayer == null) throw new AppUserNotFoundException();
        ReservationEntity reservationEntity = reservationRepository.findById(insertPlayerToReservationRequest.getReservationId()).orElseThrow(() -> new InvalidRequestException(RESERVATION_ID_DOES_NOT_EXISTS));
        if (secondPlayer.getUsername().equalsIgnoreCase(reservationEntity.getAppUserEntity().getUsername()))
            throw new InvalidRequestException(YOU_CANNOT_ADD_YOUR_SELF_AS_SECOND_PLAYER);
        boolean isReservationNotOpenForSecondPlayer = !reservationEntity.getAvailableForSecondPlayer() || reservationEntity.getSecondAppUserEntity() != null;
        if (isReservationNotOpenForSecondPlayer)
            throw new InvalidRequestException(RESERVATION_NOT_AVAILABLE_FOR_SECOND_PLAYER);
        reservationEntity.setSecondAppUserEntity(secondPlayer);
        reservationEntity.setAvailableForSecondPlayer(false);
        ReservationEntity updatedReservationEntity = reservationRepository.save(reservationEntity);
        return convertUpdatedEntityToInsertPlayerToReservationResponse(updatedReservationEntity);
    }

    private InsertPlayerToReservationResponse convertUpdatedEntityToInsertPlayerToReservationResponse(ReservationEntity updatedReservationEntity) {
        InsertPlayerToReservationResponse insertPlayerToReservationResponse = modelMapper.map(updatedReservationEntity, InsertPlayerToReservationResponse.class);
        insertPlayerToReservationResponse.setFirstPlayerUsername(updatedReservationEntity.getAppUserEntity().getUsername());
        insertPlayerToReservationResponse.setSecondPlayerUsername(updatedReservationEntity.getSecondAppUserEntity().getUsername());
        insertPlayerToReservationResponse.setCourtType(updatedReservationEntity.getCourtEntity().getCourtType());
        return insertPlayerToReservationResponse;
    }

    private List<GetAppUserReservationResponse> convertReservationEntityListToGetAppUserReservationResponseList(List<ReservationEntity> reservationEntityList) {
        List<GetAppUserReservationResponse> getAppUserReservationResponseList = new ArrayList<>();
        for (ReservationEntity reservationEntity : reservationEntityList) {
            GetAppUserReservationResponse getAppUserReservationResponseItem = modelMapper.map(reservationEntity, GetAppUserReservationResponse.class);
            getAppUserReservationResponseItem.setCourtType(reservationEntity.getCourtEntity().getCourtType());
            getAppUserReservationResponseItem.setPlayerUsername(reservationEntity.getAppUserEntity().getUsername());
            if (reservationEntity.getSecondAppUserEntity() != null) {
                getAppUserReservationResponseItem.setSecondPlayerUsername(reservationEntity.getSecondAppUserEntity().getUsername());
            }
            getAppUserReservationResponseList.add(getAppUserReservationResponseItem);
        }
        return getAppUserReservationResponseList;
    }

    private void validateTimeIntervals(LocalTime reservationStartTime, LocalTime reservationEndTime) throws InvalidRequestException {
        boolean isReservationTimeInvalid = (reservationStartTime.compareTo(reservationEndTime) >= 0) || (reservationStartTime.until(reservationEndTime, ChronoUnit.HOURS) < 1) || !timeIntervalHelper.isTimeWithinPossibleValues(reservationStartTime) || !timeIntervalHelper.isTimeWithinPossibleValues(reservationEndTime);
        if (isReservationTimeInvalid) {
            throw new InvalidRequestException(RESERVATION_TIMES_ARE_NOT_VALID);
        }
    }

    private void validateUsername(String username) throws InvalidRequestException {
        if (!authenticationFacade.getAuthentication().getName().equalsIgnoreCase(username))
            throw new InvalidRequestException(PROVIDED_USERNAME_DOES_NOT_MATCH_AUTHENTICATED_USER);
    }

    private CreateReservationResponse convertReservationEntityToCreateReservationResponse(ReservationEntity insertedReservationEntity) {
        CreateReservationResponse createReservationResponse = modelMapper.map(insertedReservationEntity, CreateReservationResponse.class);
        createReservationResponse.setCourtType(insertedReservationEntity.getCourtEntity().getCourtType());
        return createReservationResponse;
    }

    private ReservationEntity convertCreateReservationRequestToReservationEntity(CreateReservationRequest createReservationRequest, AppUserEntity appUserEntity, CourtEntity courtEntity) {
        ReservationEntity reservationEntity = modelMapper.map(createReservationRequest, ReservationEntity.class);
        reservationEntity.setAppUserEntity(appUserEntity);
        reservationEntity.setCourtEntity(courtEntity);
        return reservationEntity;
    }
}
