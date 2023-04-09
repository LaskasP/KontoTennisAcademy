package kontopoulos.rest.services.refreshtoken.impl;

import kontopoulos.rest.exceptions.RefreshTokenException;
import kontopoulos.rest.models.security.UserDetailsImpl;
import kontopoulos.rest.models.security.entity.AppUserEntity;
import kontopoulos.rest.models.security.entity.RefreshTokenEntity;
import kontopoulos.rest.models.security.rest.response.JWTPairHeadersWrapper;
import kontopoulos.rest.repos.RefreshTokenRepository;
import kontopoulos.rest.repos.UserRepository;
import kontopoulos.rest.services.refreshtoken.RefreshTokenService;
import kontopoulos.rest.utils.JWTProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static kontopoulos.rest.models.security.SecurityConstant.JWT_REFRESH_TOKEN_EXPIRATION_MS;

@AllArgsConstructor
@Transactional
@Slf4j
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    public static final String REFRESH_TOKEN_WAS_EXPIRED = "Refresh token was expired.";
    public static final String REFRESH_TOKEN_NOT_FOUND = "Refresh token not found.";
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JWTProvider jwtProvider;

    @Override
    public JWTPairHeadersWrapper generateNewJWTTokenPair(String authorizationRefreshToken) throws RefreshTokenException {
        log.info("Begin generateNewJWTTokenPair");
        RefreshTokenEntity refreshTokenEntity = findByToken(authorizationRefreshToken);
        JWTPairHeadersWrapper jwtPairHeadersWrapper;
        if (refreshTokenEntity != null) {
            verifyExpiration(refreshTokenEntity);
            AppUserEntity appUserEntity = refreshTokenEntity.getAppUserEntity();
            UserDetailsImpl userDetails = new UserDetailsImpl(appUserEntity);
            String accessToken = jwtProvider.generateJWT(userDetails);
            refreshTokenRepository.delete(refreshTokenEntity);
            String newRefreshToken = createRefreshToken(appUserEntity.getUsername());
            jwtPairHeadersWrapper = new JWTPairHeadersWrapper(accessToken, newRefreshToken);
        } else {
            throw new RefreshTokenException(REFRESH_TOKEN_NOT_FOUND);
        }
        log.info("End generateNewJWTTokenPair");
        return jwtPairHeadersWrapper;
    }

    @Override
    public String createRefreshToken(String username) {
        log.info("Begin createRefreshToken");
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setAppUserEntity(userRepository.findByUsername(username));
        refreshTokenEntity.setExpiryDate(LocalDateTime.now().plus(JWT_REFRESH_TOKEN_EXPIRATION_MS, ChronoUnit.MILLIS));
        refreshTokenEntity.setToken(UUID.randomUUID().toString());
        refreshTokenEntity = refreshTokenRepository.save(refreshTokenEntity);
        log.info("End createRefreshToken");
        return refreshTokenEntity.getToken();
    }

    @Override
    public int deleteByUserId(String username) {
        log.info("Begin deleteByUserId");
        log.debug("Delete refresh token for username: " + username);
        return refreshTokenRepository.deleteByAppUserEntity(userRepository.findByUsername(username));
    }

    private RefreshTokenEntity findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    private RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) throws RefreshTokenException {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException(REFRESH_TOKEN_WAS_EXPIRED);
        }
        return token;
    }
}
