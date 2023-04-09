package kontopoulos.rest.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static kontopoulos.rest.models.security.SecurityConstant.FORBIDDEN_MESSAGE;

@Slf4j
@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg2) throws IOException {
        log.error("Unauthorized error - {}", arg2.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("timeStamp", new Date().toString());
        body.put("message", FORBIDDEN_MESSAGE);
        body.put("path", request.getServletPath());
        final ObjectMapper mapper = new ObjectMapper();
        final OutputStream outputStream = response.getOutputStream();
        mapper.writeValue(outputStream, body);
        outputStream.flush();
    }
}