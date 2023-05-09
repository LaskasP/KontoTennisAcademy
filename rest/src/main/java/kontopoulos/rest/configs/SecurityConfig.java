package kontopoulos.rest.configs;

import kontopoulos.rest.filters.JWTValidatorFilter;
import kontopoulos.rest.utils.JWTAuthenticationEntryPoint;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

import static kontopoulos.rest.models.security.SecurityConstant.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Configuration
@AllArgsConstructor
@EnableMethodSecurity(prePostEnabled = false, securedEnabled = true)
public class SecurityConfig {

    private final JWTValidatorFilter jwtValidatorFilter;
    private final UserDetailsService userDetailsService;
    private final JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors().configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            //config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
            config.setAllowedOrigins(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowCredentials(true);
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setExposedHeaders(List.of(AUTHORIZATION));
            config.setMaxAge(3600L);
            return config;
        });
        http.authorizeHttpRequests().requestMatchers(PUBLIC_URLS).permitAll();
        http.authorizeHttpRequests().requestMatchers(SYSTEM_ADMIN_URLS).hasAuthority(SECURED_ROLE_SYSTEM_ADMIN);
        http.authorizeHttpRequests().anyRequest().authenticated();
        http.authenticationProvider(authenticationProvider());
        http.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
        http.addFilterBefore(jwtValidatorFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

