package io.spring.jbuy.features.product;

import io.spring.jbuy.features.product.dto.PageFacet;
import io.spring.jbuy.features.product.dto.ProductDetailResponse;
import io.spring.jbuy.features.product.dto.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController @RequestMapping("/api/v1/products")
@Tag(name = "product", description = "product API")
@Validated
@RequiredArgsConstructor @Slf4j
public class ProductController {
    private final ProductService productService;

    @GetMapping("/auto-complete")
    @Operation(summary = "Auto complete search text", tags = "product")
    public ResponseEntity<Set<String>> searchTextAutoComplete(@RequestParam(value = "searchText") String searchText) {
        return ResponseEntity.ok().body(productService.searchTextAutoComplete(searchText));
    }

    @GetMapping("/similar-products")
    @Operation(summary = "Find similar products by text", tags = "product")
    public ResponseEntity<Page<ProductResponse>> searchSimilarProduct(
            @ParameterObject Pageable pageable,
            @RequestParam(value = "searchText") String searchText) {

        return ResponseEntity.ok().body(productService.searchSimilarProduct(pageable, searchText));
    }

    @GetMapping("")
    @Operation(summary = "Find products by text", tags = "product")
    public ResponseEntity<PageFacet<ProductResponse>> searchProduct(
            @ParameterObject Pageable pageable,
            @RequestParam(value = "searchText") String searchText,
            @RequestParam(value = "dealFilter", required = false) Boolean dealFilter,
            @RequestParam(value = "categoryFilter", required = false) String categoryFilter,
            @RequestParam(value = "brandFilter", required = false) List<String> brandFilter,
            @RequestParam(value = "priceFilter", required = false) List<String> priceFilter,
            @RequestParam(value = "ratingFilter", required = false) Integer ratingFilter,
            @RequestParam(value = "attributeFilter", required = false) List<String> attributeFilter) {

        return ResponseEntity.ok().body(productService.searchProduct(pageable,
                                                                     searchText,
                                                                     dealFilter,
                                                                     categoryFilter,
                                                                     brandFilter,
                                                                     priceFilter,
                                                                     ratingFilter,
                                                                     attributeFilter));
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Find product with detail by id", tags = "product")
    public ProductDetailResponse getProductById(@PathVariable UUID productId) {
        return productService.getProductDetailResponseById(productId);
    }
}
