package io.spring.jbuy.features.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.QueryHint;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.annotations.QueryHints.CACHEABLE;

public interface ReviewRepository extends PagingAndSortingRepository<Review, UUID> {

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Optional<Review> findById_UserIdAndId_ProductId(UUID userId, UUID productId);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    boolean existsById_UserIdAndId_ProductId(UUID userId, UUID productId);

    void deleteById_UserIdAndId_ProductId(UUID userId, UUID productId);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Page<Review> findAllByUser_Id(UUID userId, Pageable pageable);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Page<Review> findAllByProduct_Id(UUID productId, Pageable pageable);
}
