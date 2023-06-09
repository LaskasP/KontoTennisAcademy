package kontopoulos.rest.models.security.rest.response;

import kontopoulos.rest.models.security.lov.AppUserRoleEnum;

import java.util.List;

public class RegisterResponse {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private List<AppUserRoleEnum> roles;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<AppUserRoleEnum> getRoles() {
        return roles;
    }

    public void setRoles(List<AppUserRoleEnum> roles) {
        this.roles = roles;
    }
}