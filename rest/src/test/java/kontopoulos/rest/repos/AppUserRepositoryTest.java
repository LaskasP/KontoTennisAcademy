package kontopoulos.rest.repos;

import kontopoulos.rest.models.security.entity.AppUserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;

    private AppUserEntity testAppUserEntity;

    @BeforeEach
    void setUpAppUserEntityInDB() {
        final String username = "Skouna";
        final String firstName = "Petros";
        final String lastName = "Laskas";
        final String email = "skouma@gmail.com";
        final String password = "lol";
        final String profileImageUrl = "lol.gr";
        final LocalDateTime lastLoginDate = LocalDateTime.now();
        final LocalDateTime lastLoginDateDisplay = LocalDateTime.now();
        final LocalDateTime joinDate = LocalDateTime.now();
        final boolean isActive = true;
        final boolean isNotLocked = true;
        testAppUserEntity = new AppUserEntity();
        testAppUserEntity.setUsername(username);
        testAppUserEntity.setFirstName(firstName);
        testAppUserEntity.setLastName(lastName);
        testAppUserEntity.setEmail(email);
        testAppUserEntity.setPassword(password);
        testAppUserEntity.setProfileImageUrl(profileImageUrl);
        testAppUserEntity.setLastLoginDate(lastLoginDate);
        testAppUserEntity.setLastLoginDateDisplay(lastLoginDateDisplay);
        testAppUserEntity.setJoinDate(joinDate);
        testAppUserEntity.setActive(isActive);
        testAppUserEntity.setNotLocked(isNotLocked);
        appUserRepository.save(testAppUserEntity);
    }

    @AfterEach
    void deleteAppUserEntity() {
        appUserRepository.deleteAll();
    }

    @Test
    void whenFindByUsernameIsCalled_givenTestAppUserEntityUsername_thenAppUserIsReturned() {
        AppUserEntity expected = appUserRepository.findByUsername(testAppUserEntity.getUsername());
        assertEquals(expected, testAppUserEntity);
    }

}