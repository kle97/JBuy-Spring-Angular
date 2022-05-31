package io.spring.jbuy.features.product;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.validator.ValidationGroup;
import io.spring.jbuy.common.view.BaseView;
import io.spring.jbuy.features.product.dto.ProductDetailResponse;
import io.spring.jbuy.features.product.dto.ProductRequest;
import io.spring.jbuy.features.product.dto.ProductResponse;
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

@RestController @RequestMapping("/api/v1/admin/products")
@Tag(name = "product-admin", description = "product API for admin")
@Validated @PreAuthorize("hasRole(@Role.ADMIN)")
@RequiredArgsConstructor @Slf4j
public class ProductAdminController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    @Operation(summary = "Find product by id", tags = "product-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<ProductResponse> getProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok().body(productService.getProductResponseById(productId));
    }

    @GetMapping("/{productId}/detail")
    @Operation(summary = "Find product with detail by id", tags = "product-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<ProductDetailResponse> getProductDetail(@PathVariable UUID productId) {
        return ResponseEntity.ok().body(productService.getProductDetailResponseById(productId));
    }

    @GetMapping("")
    @Operation(summary = "Find all instances of product as pages", tags = "product-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<Page<ProductResponse>> getProductPageable(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(productService.getProductResponsePageable(pageable));
    }

    @PostMapping("")
    @Operation(summary = "Add a new product", tags = "product-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<ProductResponse> createProduct(@RequestBody
                                                         @Validated({ValidationGroup.onCreate.class, Default.class})
                                                         @JsonView(BaseView.Create.class)
                                                                 ProductRequest productRequest) {
        ProductResponse response = productService.createProduct(productRequest);
        String productId = String.valueOf(response.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{productId}")
                .buildAndExpand(productId).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update an product by id", tags = "product-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable UUID productId,
                                                         @RequestBody @Valid @JsonView(BaseView.Update.class)
                                                                 ProductRequest productRequest) {
        ProductResponse response = productService.updateProduct(productId, productRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete an product by id", tags = "product-admin")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
        productService.deleteById(productId);
        return ResponseEntity.noContent().build();
    }
}
