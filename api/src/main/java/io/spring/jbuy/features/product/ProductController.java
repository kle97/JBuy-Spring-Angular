package io.spring.jbuy.features.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController @RequestMapping("/api/v1/products")
@Tag(name = "product", description = "product API")
@RequiredArgsConstructor @Slf4j
public class ProductController {
    private final ProductService productService;

    @GetMapping("")
    @Operation(summary = "Find product by text", tags = "product")
    public List<Product> searchProduct(String searchText) {
        return productService.searchProduct(searchText);
    }
}
