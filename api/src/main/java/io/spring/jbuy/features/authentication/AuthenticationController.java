package io.spring.jbuy.features.authentication;

import io.spring.jbuy.features.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController @RequestMapping("/api/v1/auth")
@Tag(name = "authentication", description = "authentication API")
@RequiredArgsConstructor @Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("/csrf")
    @Operation(summary = "Noop endpoint for getting CSRF token", tags = "authentication")
    public ResponseEntity<Void> getCsrfToken() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Authenticated endpoint for test", tags = "authentication",
            security = {@SecurityRequirement(name = "httpBasic")})
    public ResponseEntity<Map<String, String>> test() {
        return ResponseEntity.ok().body(Map.of("message", "Hello, this is private content"));
    }

    @PostMapping("/login")
    @Operation(summary = "Login a user by email and password", tags = "authentication",
            security = {@SecurityRequirement(name = "httpBasic")})
    public ResponseEntity<UserResponse> login() {
        return ResponseEntity.ok().body(authenticationService.login());
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout a user by access token", tags = "authentication")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }

}
