package kontopoulos.rest.models.common.rest;

import lombok.Data;

@Data
public class AppUserResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String profileImageUrl;
}
