package io.spring.jbuy.features.review;

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

@RestController @RequestMapping("/api/v1/admin/reviews")
@Tag(name = "review-admin", description = "review API for admin")
@Validated @PreAuthorize("hasRole(@Role.ADMIN)")
@RequiredArgsConstructor @Slf4j
public class ReviewAdminController {

    private final ReviewService reviewService;

    @GetMapping("/{userId}/products/{productId}")
    @Operation(summary = "Find review by user id and product id", tags = "review-admin")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable UUID userId,
                                                    @PathVariable UUID productId) {
        return ResponseEntity.ok()
                .body(reviewService.getReviewResponseById(userId, productId));
    }

    @GetMapping("")
    @Operation(summary = "Find all instances of review as pages", tags = "review-admin")
    public ResponseEntity<Page<ReviewResponse>> getReviewPageable(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(reviewService.getReviewResponsePageable(pageable));
    }

    @PostMapping("")
    @Operation(summary = "Add a new review", tags = "review-admin")
    public ResponseEntity<ReviewResponse> createReview(@RequestBody
                                                       @Validated({ValidationGroup.onCreate.class, Default.class})
                                                       @JsonView(BaseView.Create.class)
                                                               ReviewRequest reviewRequest) {
        ReviewResponse response = reviewService.createReview(reviewRequest);
        String userId = String.valueOf(reviewRequest.getUserId());
        String productId = String.valueOf(reviewRequest.getProductId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{userId}/products/{productId}")
                .buildAndExpand(userId, productId).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{userId}/products/{productId}")
    @Operation(summary = "Update a review by user id and product id", tags = "review-admin")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable UUID userId,
                                                       @PathVariable UUID productId,
                                                       @RequestBody @Valid @JsonView(BaseView.Update.class)
                                                               ReviewRequest reviewRequest) {
        ReviewResponse response = reviewService.updateReview(userId, productId,
                                                             reviewRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{userId}/products/{productId}")
    @Operation(summary = "Delete a review by id", tags = "review-admin")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID userId,
                                             @PathVariable UUID productId) {
        reviewService.deleteByUserIdAndProductId(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
