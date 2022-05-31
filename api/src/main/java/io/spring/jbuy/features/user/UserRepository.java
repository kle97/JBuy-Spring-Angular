package io.spring.jbuy.features.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.QueryHint;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.annotations.QueryHints.CACHEABLE;

public interface UserRepository extends PagingAndSortingRepository<User, UUID> {

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Optional<User> findById(UUID id);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Page<User> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"listOfAuthority"})
    Optional<User> findByEmail(String email);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    boolean existsByEmail(String email);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    @EntityGraph(attributePaths = {"listOfAuthority"})
    Optional<User> findWithAuthoritiesById(UUID id);
}
