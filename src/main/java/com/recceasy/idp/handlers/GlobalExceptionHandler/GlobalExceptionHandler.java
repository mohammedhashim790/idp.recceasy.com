package com.recceasy.idp.handlers.GlobalExceptionHandler;

import com.recceasy.idp.handlers.ExceptionHandlers.password.PasswordMissingLowercaseException;
import com.recceasy.idp.handlers.ExceptionHandlers.password.PasswordMissingSpecialCharacterException;
import com.recceasy.idp.handlers.ExceptionHandlers.password.PasswordMissingUppercaseException;
import com.recceasy.idp.handlers.ExceptionHandlers.username.InvalidEmailException;
import com.recceasy.idp.handlers.ExceptionHandlers.password.PasswordTooShortException;
import com.recceasy.idp.handlers.ExceptionHandlers.auth.AuthenticationException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> buildResponse(Exception ex, WebRequest webRequest, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("message", ex.getMessage());
        response.put("details", webRequest.getDescription(false));
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler({PasswordTooShortException.class, PasswordMissingUppercaseException.class, PasswordMissingLowercaseException.class, PasswordMissingSpecialCharacterException.class, InvalidEmailException.class})
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(Exception ex, WebRequest webRequest) {
        return buildResponse(ex, webRequest, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleInternalServerException(Exception ex, WebRequest webRequest) {
        return buildResponse(ex, webRequest, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthException(AuthenticationException ex, WebRequest webRequest) {
        return buildResponse(ex, webRequest, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({
            BadRequestException.class,
            HttpClientErrorException.BadRequest.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<Map<String, Object>> handleBadRequestException(Exception ex, WebRequest webRequest) {
        return buildResponse(ex, webRequest, HttpStatus.BAD_REQUEST);
    }


}
