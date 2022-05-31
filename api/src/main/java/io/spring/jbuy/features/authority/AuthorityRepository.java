package io.spring.jbuy.features.authority;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.QueryHint;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.annotations.QueryHints.CACHEABLE;

public interface AuthorityRepository extends PagingAndSortingRepository<Authority, UUID> {

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Optional<Authority> findById(UUID id);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Optional<Authority> findByRole(String role);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    boolean existsByRole(String role);
}
