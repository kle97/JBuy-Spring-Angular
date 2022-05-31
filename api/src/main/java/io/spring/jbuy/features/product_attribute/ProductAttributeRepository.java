package io.spring.jbuy.features.product_attribute;

import io.spring.jbuy.features.attribute.Attribute_;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.annotations.QueryHints.CACHEABLE;

public interface ProductAttributeRepository extends PagingAndSortingRepository<ProductAttribute, ProductAttributeId> {

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    Optional<ProductAttribute> findById_ProductIdAndId_AttributeId(UUID productId, UUID attributeId);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    @EntityGraph(
            attributePaths = {
                    ProductAttribute_.ATTRIBUTE,
                    ProductAttribute_.ATTRIBUTE + "." + Attribute_.ATTRIBUTE_TYPE,
            })
    List<ProductAttribute> findAllWithRelationshipByProduct_IdOrderByCreatedAt(UUID productId);

    @QueryHints(@QueryHint(name = CACHEABLE, value = "true"))
    boolean existsById_ProductIdAndId_AttributeId(UUID productId, UUID attributeId);

    void deleteById_ProductIdAndId_AttributeId(UUID productId, UUID attributeId);
}
