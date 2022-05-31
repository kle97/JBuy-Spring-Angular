package io.spring.jbuy.features.attribute;

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

@RestController @RequestMapping("/api/v1/admin/attributes")
@Tag(name = "attribute-admin", description = "attribute API for admin")
@Validated @PreAuthorize("hasRole(@Role.ADMIN)")
@RequiredArgsConstructor @Slf4j
public class AttributeAdminController {

    private final AttributeService attributeService;

    @GetMapping("/{attributeId}")
    @Operation(summary = "Find attribute by id", tags = "attribute-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<AttributeResponse> getAttribute(@PathVariable UUID attributeId) {
        return ResponseEntity.ok().body(attributeService.getAttributeResponseById(attributeId));
    }

    @GetMapping("")
    @Operation(summary = "Find all instances of attribute as pages", tags = "attribute-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<Page<AttributeResponse>> getAttributePageable(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(attributeService.getAttributeResponsePageable(pageable));
    }

    @PostMapping("")
    @Operation(summary = "Add a new attribute", tags = "attribute-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<AttributeResponse> createAttribute(@RequestBody
                                                             @Validated({ValidationGroup.onCreate.class, Default.class})
                                                             @JsonView(BaseView.Create.class)
                                                                     AttributeRequest attributeRequest) {
        AttributeResponse response = attributeService.createAttribute(attributeRequest);
        String attributeId = String.valueOf(response.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{attributeId}")
                .buildAndExpand(attributeId).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{attributeId}")
    @Operation(summary = "Update an attribute by id", tags = "attribute-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<AttributeResponse> updateAttribute(@PathVariable UUID attributeId,
                                                             @RequestBody @Valid @JsonView(BaseView.Update.class)
                                                                     AttributeRequest attributeRequest) {
        AttributeResponse response = attributeService.updateAttribute(attributeId, attributeRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{attributeId}")
    @Operation(summary = "Delete an attribute by id", tags = "attribute-admin")
    public ResponseEntity<Void> deleteAttribute(@PathVariable UUID attributeId) {
        attributeService.deleteById(attributeId);
        return ResponseEntity.noContent().build();
    }

}
