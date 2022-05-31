package io.spring.jbuy.features.attribute_type;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.annotations.QueryHints.CACHEABLE;

public interface AttributeTypeRepository extends PagingAndSortingRepository<AttributeType, UUID> {

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Optional<AttributeType> findById(UUID id);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Page<AttributeType> findAll(Pageable pageable);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    boolean existsByName(String name);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    List<AttributeType> findByListOfCategory_Id(UUID categoryId);
}
