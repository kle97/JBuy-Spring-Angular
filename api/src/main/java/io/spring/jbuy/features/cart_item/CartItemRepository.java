package io.spring.jbuy.features.cart_item;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.annotations.QueryHints.CACHEABLE;

public interface CartItemRepository extends PagingAndSortingRepository<CartItem, UUID> {

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Optional<CartItem> findById_UserIdAndId_ProductId(UUID userId, UUID productId);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    boolean existsById_UserIdAndId_ProductId(UUID userId, UUID productId);

    void deleteById_UserIdAndId_ProductId(UUID userId, UUID productId);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    List<CartItem> findAllById_UserId(UUID userId);

    void deleteAllById_UserId(UUID userId);
}
