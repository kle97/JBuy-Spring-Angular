package io.spring.jbuy.features.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.QueryHint;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.annotations.QueryHints.CACHEABLE;

public interface OrderRepository extends PagingAndSortingRepository<Order, UUID> {

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Optional<Order> findById(UUID id);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Page<Order> findAll(Pageable pageable);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Page<Order> findAllByUser_Id(UUID userId, Pageable pageable);

//    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
//    @EntityGraph(attributePaths = {"listOfOrderProduct"})
//    Page<Order> findWithOrderProductsByUser_Id(UUID userId, Pageable pageable);
}
