package io.spring.jbuy.features.review;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.validator.ValidationGroup;
import io.spring.jbuy.common.view.BaseView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

import javax.validation.Valid;
import javax.validation.groups.Default;
import java.util.UUID;

@RestController @RequestMapping("/api/v1/reviews")
@Tag(name = "review", description = "review API")
@Validated
@RequiredArgsConstructor @Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{userId}/products/{productId}")
    @Operation(summary = "Find review by user id and product id", tags = "review",
            security = {@SecurityRequirement(name = "httpBasic")})
    @PreAuthorize("hasRole(@Role.ADMIN) or @webSecurity.hasUserId(#userId)")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable UUID userId,
                                                    @PathVariable UUID productId) {
        return ResponseEntity.ok()
                .body(reviewService.getReviewResponseById(userId, productId));
    }

    @GetMapping("/users/{userId}/page")
    @Operation(summary = "Find all instances of review as pages by user id", tags = "review",
            security = {@SecurityRequirement(name = "httpBasic")})
    @PreAuthorize("hasRole(@Role.ADMIN) or @webSecurity.hasUserId(#userId)")
    public ResponseEntity<Page<ReviewResponse>> getReviewPageableByUserId(@PathVariable UUID userId,
                                                                          @ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(reviewService.getReviewResponsePageableByUserId(userId, pageable));
    }

    @GetMapping("/products/{productId}/page")
    @Operation(summary = "Find all instances of review as pages by product id", tags = "review",
            security = {@SecurityRequirement(name = "httpBasic")})
    public ResponseEntity<Page<ReviewResponse>> getReviewPageableByProductId(@PathVariable UUID productId,
                                                                             @ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(reviewService.getReviewResponsePageableByProductId(productId, pageable));
    }

    @PostMapping("")
    @Operation(summary = "Add a new review", tags = "review", security = {@SecurityRequirement(name = "httpBasic")})
    @PreAuthorize("hasRole(@Role.ADMIN) or @webSecurity.hasUserId(#reviewRequest.userId)")
    public ResponseEntity<ReviewResponse> createReview(@RequestBody
                                                       @Validated({ValidationGroup.onCreate.class, Default.class})
                                                       @JsonView(BaseView.Create.class)
                                                               ReviewRequest reviewRequest) {
        ReviewResponse response = reviewService.createReview(reviewRequest);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{userId}/products/{productId}")
    @Operation(summary = "Update a review by user id and product id", tags = "review",
            security = {@SecurityRequirement(name = "httpBasic")})
    @PreAuthorize("hasRole(@Role.ADMIN) or @webSecurity.hasUserId(#userId)")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable UUID userId,
                                                       @PathVariable UUID productId,
                                                       @RequestBody @Valid @JsonView(BaseView.Update.class)
                                                               ReviewRequest reviewRequest) {
        ReviewResponse response = reviewService.updateReview(userId, productId, reviewRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{userId}/products/{productId}")
    @Operation(summary = "Delete a review by id", tags = "review",
            security = {@SecurityRequirement(name = "httpBasic")})
    @PreAuthorize("hasRole(@Role.ADMIN) or @webSecurity.hasUserId(#userId)")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID userId,
                                             @PathVariable UUID productId) {
        reviewService.deleteByUserIdAndProductId(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
