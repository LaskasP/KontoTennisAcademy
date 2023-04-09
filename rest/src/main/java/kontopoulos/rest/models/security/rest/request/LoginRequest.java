package kontopoulos.rest.models.security.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class LoginRequest {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 5, max = 25)
    private String username;
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 200)
    private String password;
}