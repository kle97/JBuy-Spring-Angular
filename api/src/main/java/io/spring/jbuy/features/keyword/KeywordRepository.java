package io.spring.jbuy.features.keyword;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.QueryHint;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.annotations.QueryHints.CACHEABLE;

public interface KeywordRepository extends PagingAndSortingRepository<Keyword, UUID> {

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Optional<Keyword> findById(UUID id);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Page<Keyword> findAll(Pageable pageable);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    boolean existsByName(String name);
}
