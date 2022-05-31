package io.spring.jbuy.features.attribute_type;

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
import java.util.List;
import java.util.UUID;

@RestController @RequestMapping("/api/v1/admin/attribute-types")
@Tag(name = "attributeType-admin", description = "attribute type API for admin")
@Validated @PreAuthorize("hasRole(@Role.ADMIN)")
@RequiredArgsConstructor @Slf4j
public class AttributeTypeAdminController {

    private final AttributeTypeService attributeTypeService;

    @GetMapping("/{attributeTypeId}")
    @Operation(summary = "Find attributeType by id", tags = "attributeType-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<AttributeTypeResponse> getAttributeType(@PathVariable UUID attributeTypeId) {
        return ResponseEntity.ok().body(attributeTypeService.getAttributeTypeResponseById(attributeTypeId));
    }

    @GetMapping("")
    @Operation(summary = "Find all instances of attribute type as pages", tags = "attributeType-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<Page<AttributeTypeResponse>> getAttributeTypePageable(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(attributeTypeService.getAttributeTypeResponsePageable(pageable));
    }

    @GetMapping("/categories/{categoryId}")
    @Operation(summary = "Find instances of attribute type by category id", tags = "attributeType-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<List<AttributeTypeDetailResponse>>
    getAttributeTypeDetailResponseByCategoryId(@PathVariable UUID categoryId) {
        return ResponseEntity.ok().body(attributeTypeService.getAttributeTypeDetailResponseByCategoryId(categoryId));
    }

    @PostMapping("")
    @Operation(summary = "Add a new attributeType", tags = "attributeType-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<AttributeTypeResponse> createAttributeType(@RequestBody
                                                                     @Validated({ValidationGroup.onCreate.class, Default.class})
                                                                     @JsonView(BaseView.Create.class)
                                                                             AttributeTypeRequest attributeTypeRequest) {
        AttributeTypeResponse response = attributeTypeService.createAttributeType(attributeTypeRequest);
        String attributeTypeId = String.valueOf(response.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{attributeTypeId}")
                .buildAndExpand(attributeTypeId).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{attributeTypeId}")
    @Operation(summary = "Update an attributeType by id", tags = "attributeType-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<AttributeTypeResponse> updateAttributeType(@PathVariable UUID attributeTypeId,
                                                                     @RequestBody @Valid @JsonView(BaseView.Update.class)
                                                                             AttributeTypeRequest attributeTypeRequest) {
        AttributeTypeResponse response = attributeTypeService.updateAttributeType(attributeTypeId,
                                                                                  attributeTypeRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{attributeTypeId}")
    @Operation(summary = "Delete an attributeType by id", tags = "attributeType-admin")
    public ResponseEntity<Void> deleteAttributeType(@PathVariable UUID attributeTypeId) {
        attributeTypeService.deleteById(attributeTypeId);
        return ResponseEntity.noContent().build();
    }

}
