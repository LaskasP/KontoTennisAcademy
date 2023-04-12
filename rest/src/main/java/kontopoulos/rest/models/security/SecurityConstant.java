package kontopoulos.rest.models.security;

public class SecurityConstant {
    public static final String JWT_SUBJECT = "Jwt-Token";
    public static final String USERNAME = "username";
    public static final String AUTHORITIES = "authorities";
    public static final String DELIMITER = ",";
    public static final long JWT_EXPIRATION_MS = 1800000L;
    public static final long JWT_REFRESH_TOKEN_EXPIRATION_MS = 8640000000L; //100 days
    public static final String JWT_ISSUER = "skouma";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String FORBIDDEN_MESSAGE = "You need to login first";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this resource";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = {"/auth/login", "/auth/registration", "/auth/refreshment", "/password/reset", "/user/image/**", "/error", "/api-docs/**", "/swagger-ui/**"};
    public static final String REFRESH_AUTHORIZATION_HEADER = "Authorization-Refresh";
}