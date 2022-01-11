package io.spring.jbuy.features.product;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface ProductRepository extends PagingAndSortingRepository<Product, UUID> {

}
