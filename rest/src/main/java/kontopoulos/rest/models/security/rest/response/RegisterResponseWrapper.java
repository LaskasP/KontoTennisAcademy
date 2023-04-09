package kontopoulos.rest.models.security.rest.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RegisterResponseWrapper {
    String httpHeader;
    RegisterResponse registerResponse;

    public String getHttpHeader() {
        return httpHeader;
    }

    public void setHttpHeader(String httpHeader) {
        this.httpHeader = httpHeader;
    }

    public RegisterResponse getRegisterResponse() {
        return registerResponse;
    }

    public void setRegisterResponse(RegisterResponse registerResponse) {
        this.registerResponse = registerResponse;
    }
}
