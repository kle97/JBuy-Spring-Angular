package io.spring.jbuy.features.product_attribute;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController @RequestMapping("/api/v1/product-attributes")
@Tag(name = "productAttribute", description = "product attribute API")
@Validated
@RequiredArgsConstructor @Slf4j
public class ProductAttributeController {

    private final ProductAttributeService productAttributeService;

    @GetMapping("/{productId}")
    @Operation(summary = "Find all product attributes by product id", tags = "productAttribute")
    public ResponseEntity<List<ProductAttributeResponse>> getProductAttribute(@PathVariable UUID productId) {
        return ResponseEntity.ok().body(productAttributeService.getAllProductAttributesByProductId(productId));
    }
}
