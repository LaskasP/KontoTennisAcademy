package kontopoulos.rest.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {
        log.error(ex.getClass().toString());
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now(), List.of("Generic Application Exception"), request.getDescription(false)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({MissingRequestHeaderException.class, HttpMessageNotReadableException.class, InvalidRequestException.class})
    public ResponseEntity<ErrorMessage> generalBadRequestExceptionHandler(Exception ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), List.of(ex.getMessage()), request.getDescription(false)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException ex, WebRequest request) {
        String message = NestedExceptionUtils.getMostSpecificCause(ex).getMessage();
        log.error(message);
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), List.of(message), request.getDescription(false)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> constrainViolationExceptionHandler(ConstraintViolationException ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), List.of(ex.getMessage()), request.getDescription(false)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ErrorMessage> RefreshTokenExpiredExceptionHandler(RefreshTokenException ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.FORBIDDEN.value(), LocalDateTime.now(), List.of(ex.getMessage()), request.getDescription(false)), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex, WebRequest request) {
        log.error(ex.getMessage());
        List<String> errorMessages = new ArrayList<>();
        List<ObjectError> errorList = ex.getBindingResult().getAllErrors();
        for (ObjectError error : errorList) {
            errorMessages.add(error.getDefaultMessage());
        }
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), errorMessages, request.getDescription(false)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessage> badCredentialsExceptionHandler(BadCredentialsException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), List.of(ex.getMessage()), request.getDescription(false)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> accessDeniedExceptionHandler(AccessDeniedException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.FORBIDDEN.value(), LocalDateTime.now(), List.of(ex.getMessage()), request.getDescription(false)), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorMessage> expiredJwtExceptionHandler(ExpiredJwtException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now(), List.of(ex.getMessage()), request.getDescription(false)), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorMessage> lockedExceptionHandler(LockedException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now(), List.of(ex.getMessage()), request.getDescription(false)), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({EmailExistsException.class, EmailNotFoundException.class})
    public ResponseEntity<ErrorMessage> emailExceptionHandler(Exception ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), List.of(ex.getMessage()), request.getDescription(false)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UsernameExistsException.class, AppUserNotFoundException.class})
    public ResponseEntity<ErrorMessage> appUserExceptionHandler(Exception ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), List.of(ex.getMessage()), request.getDescription(false)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ErrorMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), List.of(ex.getMessage()), request.getDescription(false)), HttpStatus.NOT_FOUND);
    }
}