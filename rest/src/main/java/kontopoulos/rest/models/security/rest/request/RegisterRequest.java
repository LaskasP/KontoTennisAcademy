package kontopoulos.rest.models.security.rest.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    @NotBlank(message = "Username must be between 5 and 25 characters long.")
    @Size(min = 5, max = 25)
    private String username;
    @NotBlank(message = "Password must be greater than 6 characters long.")
    @Size(min = 6, max = 200)
    private String password;
    @NotBlank(message = "Email cannot be blank.")
    @Size(max = 60)
    @Email
    private String email;
}
