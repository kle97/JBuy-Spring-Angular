package io.spring.jbuy.features.user;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.validator.ValidationGroup;
import io.spring.jbuy.common.view.BaseView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.net.URI;
import java.util.UUID;

@RestController @RequestMapping("/api/v1/admin/users")
@Tag(name = "user-admin", description = "user API for admin")
@Validated @PreAuthorize("hasRole(@Role.ADMIN)")
@RequiredArgsConstructor @Slf4j
public class UserAdminController {

    private final UserService userService;

    @GetMapping("/{userId}")
    @Operation(summary = "Find user by id", tags = "user-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId) {
        return ResponseEntity.ok().body(userService.getUserResponseById(userId));
    }

    @GetMapping("")
    @Operation(summary = "Find all instances of user as pages", tags = "user-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<Page<UserResponse>> getUserPageable(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(userService.getUserResponsePageable(pageable));
    }

    @PostMapping("")
    @Operation(summary = "Add a new user", tags = "user-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<UserResponse> createUser(@RequestBody
                                                   @Validated({ValidationGroup.onCreate.class, Default.class})
                                                   @JsonView(BaseView.Create.class) UserRequest userRequest) {
        UserResponse response = userService.createUser(userRequest, true);
        String userId = String.valueOf(response.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{userId}")
                .buildAndExpand(userId).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update an user by id", tags = "user-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID userId,
                                                   @RequestBody @Valid @JsonView(BaseView.Update.class)
                                                           UserRequest userRequest) {
        UserResponse response = userService.updateUser(userId, userRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete an user by id", tags = "user-admin")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/{userId}/activate")
    @Operation(summary = "Activate a user", tags = "user-admin")
    public ResponseEntity<Void> activateUser(@PathVariable UUID userId) {
        userService.setUserEnabled(userId, true);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/{userId}/deactivate")
    @Operation(summary = "Deactivate a user", tags = "user-admin")
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID userId) {
        userService.setUserEnabled(userId, false);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/{userId}/change-password")
    @Operation(summary = "Change a user's password", tags = "user-admin")
    public ResponseEntity<Void> changeUserPassword(@PathVariable UUID userId,
                                                   @RequestBody @NotNull PasswordChangeRequest passwordChangeRequest) {
        userService.changeUserPassword(userId, passwordChangeRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/{userId}/change-authorities")
    @Operation(summary = "Change a user's authorities", tags = "user-admin")
    public ResponseEntity<Void> changeUserAuthorities(@PathVariable UUID userId,
                                                      @RequestBody @NotNull AuthorityChangeRequest authorityChangeRequest) {
        userService.changeUserAuthorities(userId, authorityChangeRequest);
        return ResponseEntity.ok().build();
    }

}
