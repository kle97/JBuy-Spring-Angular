package io.spring.jbuy.features.authority;

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
import javax.validation.groups.Default;
import java.net.URI;
import java.util.UUID;

@RestController @RequestMapping("/api/v1/admin/authorities")
@Tag(name = "authority-admin", description = "authority API for admin")
@Validated @PreAuthorize("hasRole(@Role.ADMIN)")
@RequiredArgsConstructor @Slf4j
public class AuthorityAdminController {

    private final AuthorityService authorityService;

    @GetMapping("/{authorityId}")
    @Operation(summary = "Find authority by id", tags = "authority-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<AuthorityResponse> getAuthority(@PathVariable UUID authorityId) {
        return ResponseEntity.ok().body(authorityService.getAuthorityResponseById(authorityId));
    }

    @GetMapping("")
    @Operation(summary = "Find all instances of authority as pages", tags = "authority-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<Page<AuthorityResponse>> getAuthorityPageable(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(authorityService.getAuthorityResponsePageable(pageable));
    }

    @PostMapping("")
    @Operation(summary = "Add an new authority", tags = "authority-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<AuthorityResponse> createAuthority(@RequestBody
                                                             @Validated({ValidationGroup.onCreate.class, Default.class})
                                                             @JsonView(BaseView.Create.class)
                                                                     AuthorityRequest authorityRequest) {
        AuthorityResponse response = authorityService.createAuthority(authorityRequest);
        String authorityId = String.valueOf(response.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{authorityId}")
                .buildAndExpand(authorityId).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{authorityId}")
    @Operation(summary = "Update an authority by id", tags = "authority-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<AuthorityResponse> updateAuthority(@PathVariable UUID authorityId,
                                                             @RequestBody @Valid @JsonView(BaseView.Update.class)
                                                                     AuthorityRequest authorityRequest) {
        AuthorityResponse response = authorityService.updateAuthority(authorityId,
                                                                      authorityRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{authorityId}")
    @Operation(summary = "Delete an authority by id", tags = "authority-admin")
    public ResponseEntity<Void> deleteAuthority(@PathVariable UUID authorityId) {
        authorityService.deleteById(authorityId);
        return ResponseEntity.noContent().build();
    }

}
