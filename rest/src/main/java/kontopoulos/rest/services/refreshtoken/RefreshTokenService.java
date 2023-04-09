package kontopoulos.rest.services.refreshtoken;

import kontopoulos.rest.exceptions.RefreshTokenException;
import kontopoulos.rest.models.security.rest.response.JWTPairHeadersWrapper;

public interface RefreshTokenService {
    String createRefreshToken(String username);

    JWTPairHeadersWrapper generateNewJWTTokenPair(String authorizationRefreshToken) throws RefreshTokenException;

    int deleteByUserId(String username);
}
