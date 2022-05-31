package io.spring.jbuy.features.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.QueryHint;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.annotations.QueryHints.CACHEABLE;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, UUID> {

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Optional<Customer> findById(UUID id);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Page<Customer> findAll(Pageable pageable);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    @Query("SELECT c.totalCartItemQuantity FROM Customer c WHERE c.id = :userId")
    Integer getTotalCartItemQuantityByUserId(@Param("userId") UUID userId);
}
