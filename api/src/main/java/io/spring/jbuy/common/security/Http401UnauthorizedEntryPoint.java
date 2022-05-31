package io.spring.jbuy.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class Http401UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException ex) throws IOException {

        String errorMessage = ex.getMessage();
        if (errorMessage.contains("Bad credentials")) {
            errorMessage = "Incorrect email or password. Please try again.";
        }
//        log.error("{} - {}", errorMessage, request.getRequestURI());

        response.sendError(HttpStatus.UNAUTHORIZED.value(), errorMessage);
    }
}
