package io.spring.jbuy.features.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.QueryHint;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.annotations.QueryHints.CACHEABLE;

public interface ProductRepository extends PagingAndSortingRepository<Product, UUID> {

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Optional<Product> findById(UUID id);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Page<Product> findAll(Pageable pageable);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    @EntityGraph(
            attributePaths = {
                    Product_.LIST_OF_CATEGORY,
            })
    Optional<Product> findWithRelationshipsById(UUID id);
}
