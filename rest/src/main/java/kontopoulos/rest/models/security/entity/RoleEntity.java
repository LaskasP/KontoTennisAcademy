package kontopoulos.rest.models.security.entity;


import jakarta.persistence.*;
import kontopoulos.rest.models.security.lov.AppUserRole;
import lombok.*;

import static jakarta.persistence.GenerationType.AUTO;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "application_user_roles")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AppUserRole appUserRole;

}