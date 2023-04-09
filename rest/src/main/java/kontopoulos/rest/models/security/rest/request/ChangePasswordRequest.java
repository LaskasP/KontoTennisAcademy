package kontopoulos.rest.models.security.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class ChangePasswordRequest {
    @NotBlank(message = "Username cannot be blank.")
    @Size(max = 25)
    private String username;
    @NotBlank(message = "Password cannot be blank.")
    @Size(max = 200)
    private String password;
    @NotBlank(message = "New password cannot be blank.")
    @Size(max = 200)
    private String newPassword;
}