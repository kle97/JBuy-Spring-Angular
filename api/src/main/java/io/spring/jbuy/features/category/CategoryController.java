package io.spring.jbuy.features.category;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api/v1/categories")
@Tag(name = "category", description = "category API")
@Validated
@RequiredArgsConstructor @Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("")
    @Operation(summary = "Find all instances of category as pages", tags = "category")
    public ResponseEntity<Page<CategoryResponse>> getCategoryPageable(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(categoryService.getCategoryResponsePageable(pageable));
    }

}
