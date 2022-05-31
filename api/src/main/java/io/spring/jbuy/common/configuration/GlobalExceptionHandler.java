package io.spring.jbuy.common.configuration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.spring.jbuy.common.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public void handleAuthenticationException(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException ex) throws Exception {
        handleExceptionInternal(request, response, HttpStatus.UNAUTHORIZED, ex);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public void handleBadCredentialsException(HttpServletRequest request,
                                              HttpServletResponse response,
                                              BadCredentialsException ex) throws Exception {
        String message = "Incorrect email or password!";
        handleExceptionInternal(request, response, HttpStatus.UNAUTHORIZED, message, ex);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public void handleResourceNotFoundException(HttpServletRequest request,
                                                HttpServletResponse response,
                                                ResourceNotFoundException ex) throws Exception {
        handleExceptionInternal(request, response, HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   ConstraintViolationException ex) throws Exception {
        handleExceptionInternal(request, response, HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(HttpServletRequest request,
                                                                        MethodArgumentNotValidException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());

        StringBuilder message = new StringBuilder();
        message.append("[");
        String comma = "";
        for (ObjectError error : ex.getAllErrors()) {
            message.append(comma);
            message.append("{");
            message.append(error.getObjectName());
            if (FieldError.class.isAssignableFrom(error.getClass())) {
                message.append(".");
                message.append(((FieldError) error).getField());
            }
            message.append(" for code ");
            message.append(error.getCode());
            message.append(": ");
            message.append(error.getDefaultMessage());
            message.append("}");
            comma = ", ";
        }
        message.append("]");

        body.put("message", message);
        body.put("path", request.getRequestURI());

        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public void handleInvalidFormatException(HttpServletRequest request,
                                             HttpServletResponse response,
                                             InvalidFormatException ex) throws Exception {
        handleExceptionInternal(request, response, HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(ValidationException.class)
    public void handleValidationException(HttpServletRequest request,
                                          HttpServletResponse response,
                                          ValidationException ex) throws Exception {
        handleExceptionInternal(request, response, HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void handleHttpMessageNotReadableException(HttpServletRequest request,
                                                      HttpServletResponse response,
                                                      HttpMessageNotReadableException ex) throws Exception {
        handleExceptionInternal(request, response, HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(JsonParseException.class)
    public void handleJsonParseException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         JsonParseException ex) throws Exception {
        handleExceptionInternal(request, response, HttpStatus.BAD_REQUEST, ex);
    }

    private void handleExceptionInternal(HttpServletRequest request, HttpServletResponse response,
                                         HttpStatus status, Exception ex) throws IOException {
        handleExceptionInternal(request, response, status, "", ex);
    }

    private void handleExceptionInternal(HttpServletRequest request, HttpServletResponse response,
                                         HttpStatus status, String extraMessage,
                                         Exception ex) throws IOException {
        if (extraMessage.length() > 0) {
            log.error(ex.getClass().getName() + "- {} - {}", extraMessage, request.getRequestURI());
            response.sendError(status.value(), extraMessage);
            return;
        }

        log.error(ex.getClass().getName() + "- {} - {}", ex.getMessage(), request.getRequestURI());
        response.sendError(status.value(), ex.getMessage());
    }
}
