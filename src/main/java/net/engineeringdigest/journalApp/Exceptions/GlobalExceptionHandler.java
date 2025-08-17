package net.engineeringdigest.journalApp.Exceptions;

import net.engineeringdigest.journalApp.Dto.ApiResponse;
import org.springframework.http.HttpStatus;
import com.twilio.exception.ApiException;
import net.engineeringdigest.journalApp.Exceptions.UserAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentialsException(BadCredentialsException ex) {
        log.warn("Authentication failed: {}", ex.getMessage());
        ApiResponse<Object> response = new ApiResponse<>("error", "Invalid username or password.", null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        log.warn("Registration failed: {}", ex.getMessage());
        ApiResponse<Object> response = new ApiResponse<>("error", "Username is already taken.", null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage()
                ));
        log.warn("Validation failed for request: {}", errors);
        ApiResponse<Object> response = new ApiResponse<>("error", "Validation failed", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("Malformed JSON request: {}", ex.getMessage());
        ApiResponse<Object> response = new ApiResponse<>("error", "The request body is malformed or missing.", null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Illegal argument provided: {}", ex.getMessage());
        ApiResponse<Object> response = new ApiResponse<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JournalEntryNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleJournalEntryNotFoundException(JournalEntryNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        ApiResponse<Object> response = new ApiResponse<>("error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Object>> handleTwilioApiException(ApiException ex) {
        log.error("Twilio API Error - Code: {}, Message: {}", ex.getCode(), ex.getMessage());
        ApiResponse<Object> response = new ApiResponse<>(
                "error",
                "Could not send verification code. Please ensure the phone number is correct and includes the country code (e.g., +1, +91).",
                null
        );
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        log.error("An unexpected error occurred", ex);

        ApiResponse<Object> response = new ApiResponse<>(
                "error",
                "An unexpected internal server error occurred. Please try again later.",
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}