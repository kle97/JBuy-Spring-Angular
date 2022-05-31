package io.spring.jbuy.features.user;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.view.BaseView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController @RequestMapping("/api/v1/users")
@Tag(name = "user", description = "user API")
@Validated
@RequiredArgsConstructor @Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    @Operation(summary = "Find user by id", tags = "user", security = {@SecurityRequirement(name = "httpBasic")})
    @PreAuthorize("hasRole(@Role.ADMIN) or @webSecurity.hasCustomerId(#userId)")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId) {
        return ResponseEntity.ok().body(userService.getUserResponseById(userId));
    }

    @PostMapping("/email-check")
    @Operation(summary = "Check if email already exists", tags = "user")
    public ResponseEntity<Boolean> isEmailAlreadyExisted(@RequestBody @Valid EmailCheckRequest emailCheckRequest) {
        return ResponseEntity.ok().body(userService.isEmailAlreadyExisted(emailCheckRequest.getEmail()));
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update a user by id", tags = "user", security = {@SecurityRequirement(name = "httpBasic")})
    @PreAuthorize("hasRole(@Role.ADMIN) or @webSecurity.hasCustomerId(#userId)")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID userId,
                                                   @RequestBody @Valid @JsonView(BaseView.Update.class)
                                                           UserRequest userRequest) {
        UserResponse response = userService.updateUser(userId, userRequest);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(path = "/{userId}/change-password")
    @Operation(summary = "Change a user's password", tags = "user", security = {@SecurityRequirement(name = "httpBasic")})
    @PreAuthorize("hasRole(@Role.ADMIN) or @webSecurity.hasCustomerId(#userId)")
    public ResponseEntity<Void> changeUserPassword(@PathVariable UUID userId,
                                                   @RequestBody @NotNull PasswordChangeRequest passwordChangeRequest) {
        userService.changeUserPassword(userId, passwordChangeRequest);
        return ResponseEntity.ok().build();
    }
}
