package kontopoulos.rest.models.security.rest.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JWTPairHeadersWrapper {
    String accessToken;

    String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
