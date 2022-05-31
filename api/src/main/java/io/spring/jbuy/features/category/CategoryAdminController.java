package io.spring.jbuy.features.category;

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

@RestController @RequestMapping("/api/v1/admin/categories")
@Tag(name = "category-admin", description = "category API for admin")
@Validated @PreAuthorize("hasRole(@Role.ADMIN)")
@RequiredArgsConstructor @Slf4j
public class CategoryAdminController {

    private final CategoryService categoryService;

    @GetMapping("/{categoryId}")
    @Operation(summary = "Find category by id", tags = "category-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable UUID categoryId) {
        return ResponseEntity.ok().body(categoryService.getCategoryResponseById(categoryId));
    }

    @GetMapping("")
    @Operation(summary = "Find all instances of category as pages", tags = "category-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<Page<CategoryResponse>> getCategoryPageable(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(categoryService.getCategoryResponsePageable(pageable));
    }

    @PostMapping("")
    @Operation(summary = "Add a new category", tags = "category-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody
                                                           @Validated({ValidationGroup.onCreate.class, Default.class})
                                                           @JsonView(BaseView.Create.class)
                                                                   CategoryRequest categoryRequest) {
        CategoryResponse response = categoryService.createCategory(categoryRequest);
        String categoryId = String.valueOf(response.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{categoryId}")
                .buildAndExpand(categoryId).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "Update a category by id", tags = "category-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable UUID categoryId,
                                                           @RequestBody @Valid @JsonView(BaseView.Update.class)
                                                                   CategoryRequest categoryRequest) {
        CategoryResponse response = categoryService.updateCategory(categoryId,
                                                                   categoryRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "Delete a category by id", tags = "category-admin")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID categoryId) {
        categoryService.deleteById(categoryId);
        return ResponseEntity.noContent().build();
    }

}
