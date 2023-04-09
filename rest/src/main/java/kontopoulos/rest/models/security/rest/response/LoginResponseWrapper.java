package kontopoulos.rest.models.security.rest.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginResponseWrapper {
    String httpHeader;
    LoginResponse loginResponse;

    public String getHttpHeader() {
        return httpHeader;
    }

    public void setHttpHeader(String httpHeader) {
        this.httpHeader = httpHeader;
    }

    public LoginResponse getLoginResponse() {
        return loginResponse;
    }

    public void setLoginResponse(LoginResponse loginResponse) {
        this.loginResponse = loginResponse;
    }
}