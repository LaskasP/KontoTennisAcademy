package kontopoulos.rest.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static kontopoulos.rest.models.security.SecurityConstant.*;

@Slf4j
@Component
public class JWTProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateJWT(UserDetails userDetails) {
        final SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder().setIssuer(JWT_ISSUER).setSubject(JWT_SUBJECT)
                .claim(USERNAME, userDetails.getUsername())
                .claim(AUTHORITIES, populateAuthorities(userDetails.getAuthorities()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS)
                )
                .signWith(key).compact();
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> authorities = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            authorities.add(authority.getAuthority());
        }
        return String.join(DELIMITER, authorities);
    }

    public Authentication getAuthentication(String jwt, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken auth = null;
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            String username = String.valueOf(claims.get(USERNAME));
            if (StringUtils.isEmpty(username)) throw new JwtException(TOKEN_CANNOT_BE_VERIFIED);
            String authorities = (String) claims.get(AUTHORITIES);
            auth = new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
            auth.setDetails(request);
        } catch (Exception e) {
            log.error("Cannot validate token: ", e);
        }
        return auth;
    }
}