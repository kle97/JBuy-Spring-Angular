package io.spring.jbuy.features.address;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.annotations.QueryHints.CACHEABLE;

public interface AddressRepository extends PagingAndSortingRepository<Address, UUID> {

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Optional<Address> findById(UUID id);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Page<Address> findAll(Pageable pageable);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Page<Address> findAllByUser_Id(UUID userId, Pageable pageable);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    List<Address> findAllByUser_Id(UUID userId);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    long countAllByUser_Id(UUID userId);
}
