package io.spring.jbuy.features.order_product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.QueryHint;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.annotations.QueryHints.CACHEABLE;

public interface OrderProductRepository extends PagingAndSortingRepository<OrderProduct, UUID> {

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Optional<OrderProduct> findById_OrderIdAndId_ProductId(UUID orderId, UUID productId);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Page<OrderProduct> findAll(Pageable pageable);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Page<OrderProduct> findAllById_OrderId(UUID orderId, Pageable pageable);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    boolean existsById_OrderIdAndId_ProductId(UUID orderId, UUID productId);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    boolean existsByOrder_User_IdAndId_ProductId(UUID userId, UUID productId);

    void deleteById_OrderIdAndId_ProductId(UUID orderId, UUID productId);
}
