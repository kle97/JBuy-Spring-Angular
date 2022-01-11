package io.spring.jbuy.features.user;

import lombok.NonNull;
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
    Optional<User> findById(@NonNull UUID id);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Page<User> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"authorities"})
    Optional<User> findByUsername(String username);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    @EntityGraph(attributePaths = {"authorities"})
    Optional<User> findWithAuthoritiesById(UUID id);
}
