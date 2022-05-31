package io.spring.jbuy.features.product_attribute;

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

@RestController @RequestMapping("/api/v1/admin/product-attributes")
@Tag(name = "productAttribute-admin", description = "product attribute API for admin")
@Validated @PreAuthorize("hasRole(@Role.ADMIN)")
@RequiredArgsConstructor @Slf4j
public class ProductAttributeAdminController {

    private final ProductAttributeService productAttributeService;

    @GetMapping("/{productId}/attributes/{attributeId}")
    @Operation(summary = "Find product attribute by product id and attribute id", tags = "productAttribute-admin")
    public ResponseEntity<ProductAttributeResponse> getProductAttribute(@PathVariable UUID productId,
                                                                        @PathVariable UUID attributeId) {
        return ResponseEntity.ok()
                .body(productAttributeService.getProductAttributeResponseById(productId, attributeId));
    }

    @GetMapping("")
    @Operation(summary = "Find all instances of product attribute as pages", tags = "productAttribute-admin")
    public ResponseEntity<Page<ProductAttributeResponse>> getProductAttributePageable(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(productAttributeService.getProductAttributeResponsePageable(pageable));
    }

    @PostMapping("")
    @Operation(summary = "Add a new product attribute", tags = "productAttribute-admin")
    public ResponseEntity<ProductAttributeResponse> createProductAttribute(@RequestBody
                                                                           @Validated({ValidationGroup.onCreate.class, Default.class})
                                                                           @JsonView(BaseView.Create.class)
                                                                                   ProductAttributeRequest productAttributeRequest) {
        ProductAttributeResponse response = productAttributeService.createProductAttribute(productAttributeRequest);
        String productId = String.valueOf(productAttributeRequest.getProductId());
        String attributeId = String.valueOf(productAttributeRequest.getAttributeId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{productId}/attributes/{attributeId}")
                .buildAndExpand(productId, attributeId).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{productId}/attributes/{attributeId}")
    @Operation(summary = "Update a product attribute by product id and attribute id", tags = "productAttribute-admin")
    public ResponseEntity<ProductAttributeResponse> updateProductAttribute(@PathVariable UUID productId,
                                                                           @PathVariable UUID attributeId,
                                                                           @RequestBody @Valid @JsonView(BaseView.Update.class)
                                                                                   ProductAttributeRequest productAttributeRequest) {
        ProductAttributeResponse response = productAttributeService.updateProductAttribute(productId, attributeId,
                                                                                           productAttributeRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{productId}/attributes/{attributeId}")
    @Operation(summary = "Delete a product attribute by id", tags = "productAttribute-admin")
    public ResponseEntity<Void> deleteProductAttribute(@PathVariable UUID productId,
                                                       @PathVariable UUID attributeId) {
        productAttributeService.deleteByProductIdAndAttributeId(productId, attributeId);
        return ResponseEntity.noContent().build();
    }
}
