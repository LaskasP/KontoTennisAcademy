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

import java.util.Date;
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
        return jwtPairHeadersWrapper;
    }

    private RefreshTokenEntity findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public String createRefreshToken(String username) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setAppUserEntity(userRepository.findByUsername(username));
        refreshTokenEntity.setExpiryDate(new Date(new Date().getTime() + JWT_REFRESH_TOKEN_EXPIRATION_MS));
        refreshTokenEntity.setToken(UUID.randomUUID().toString());
        refreshTokenEntity = refreshTokenRepository.save(refreshTokenEntity);
        return refreshTokenEntity.getToken();
    }

    private RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) throws RefreshTokenException {
        if (token.getExpiryDate().compareTo(new Date()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException(REFRESH_TOKEN_WAS_EXPIRED);
        }
        return token;
    }

    @Override
    public int deleteByUserId(String username) {
        return refreshTokenRepository.deleteByAppUserEntity(userRepository.findByUsername(username));
    }
}
