package kontopoulos.rest.models.security.entity;

import jakarta.persistence.*;
import lombok.*;


import java.util.Date;

@Entity(name = "application_user_refresh_tokens")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "application_user_id", referencedColumnName = "id")
    private AppUserEntity appUserEntity;
    @Column(nullable = false, unique = true)
    private String token;
    @Column(nullable = false)
    private Date expiryDate;
}
