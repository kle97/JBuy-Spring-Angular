package io.spring.jbuy.features.review;

import io.spring.jbuy.common.exception.ResourceNotFoundException;
import io.spring.jbuy.features.authentication.CustomUserDetailsService;
import io.spring.jbuy.features.order_product.OrderProductRepository;
import io.spring.jbuy.features.product.Product;
import io.spring.jbuy.features.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor @Slf4j
public class ReviewService {

    private final CustomUserDetailsService customUserDetailsService;
    private final OrderProductRepository orderProductRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Transactional(readOnly = true)
    public Review getReviewById(UUID userId, UUID productId) {
        return reviewRepository.findById_UserIdAndId_ProductId(userId, productId)
                .orElseThrow(() -> new ResourceNotFoundException(Review.class, User.class,
                                                                 userId, Product.class, productId));
    }

    @Transactional(readOnly = true)
    public ReviewResponse getReviewResponseById(UUID userId, UUID productId) {
        return reviewMapper.toReviewResponse(getReviewById(userId, productId));
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviewResponsePageable(Pageable pageable) {
        return reviewRepository.findAll(pageable).map(reviewMapper::toReviewResponse);
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviewResponsePageableByProductId(UUID productId, Pageable pageable) {
        return reviewRepository.findAllByProduct_Id(productId, pageable).map(reviewMapper::toReviewResponse);
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviewResponsePageableByUserId(UUID userId, Pageable pageable) {
        return reviewRepository.findAllByUser_Id(userId, pageable).map(reviewMapper::toReviewResponse);
    }

    @Transactional
    public ReviewResponse createReview(ReviewRequest reviewRequest) {
        validateReviewRequest(reviewRequest);
        Review transientReview = reviewMapper.toReview(reviewRequest);
        transientReview.setReviewDate(LocalDateTime.now());
        return reviewMapper.toReviewResponse(reviewRepository.save(transientReview));
    }

    @Transactional
    public ReviewResponse updateReview(UUID userId, UUID productId,
                                       ReviewRequest reviewRequest) {
        Review currentReview = getReviewById(userId, productId);
        currentReview.setReviewDate(LocalDateTime.now());
        return reviewMapper.toReviewResponse(reviewMapper.toExistingReview(reviewRequest, currentReview));
    }

    @Transactional
    public void deleteByUserIdAndProductId(UUID userId, UUID productId) {
        if (reviewRepository.existsById_UserIdAndId_ProductId(userId, productId)) {
            reviewRepository.deleteById_UserIdAndId_ProductId(userId, productId);
        } else {
            throw new ResourceNotFoundException(Review.class, User.class,
                                                userId, Product.class, productId);
        }
    }

    private void validateReviewRequest(ReviewRequest reviewRequest) {
        if (reviewRepository.existsById_UserIdAndId_ProductId(reviewRequest.getUserId(),
                                                              reviewRequest.getProductId())) {
            throw new ValidationException("Review for this product already exists!");
        }

        if (!customUserDetailsService.isAdmin()
                && !orderProductRepository.existsByOrder_User_IdAndId_ProductId(reviewRequest.getUserId(),
                                                                                reviewRequest.getProductId())) {
            throw new ValidationException("Purchase of product is required for reviewing!");
        }
    }
}
