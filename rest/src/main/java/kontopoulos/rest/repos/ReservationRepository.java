package kontopoulos.rest.repos;

import kontopoulos.rest.models.reservation.entity.ReservationEntity;
import kontopoulos.rest.models.security.entity.AppUserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    List<ReservationEntity> findByAppUserEntityOrSecondAppUserEntity(AppUserEntity appUserEntity, AppUserEntity secondAppUserEntity);

    List<ReservationEntity> findByReservationDateAfter(LocalDate currentDate, Pageable page);
}
