package io.spring.jbuy.features.product_attribute;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter @NoArgsConstructor @AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"product_id", "attribute_id"})})
public class ProductAttributeId implements Serializable {
    private static final long serialVersionUID = 142021587407L;

    private UUID productId;

    private UUID attributeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductAttributeId)) {
            return false;
        }
        ProductAttributeId that = (ProductAttributeId) o;
        return Objects.equals(productId, that.productId) && Objects.equals(attributeId, that.attributeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, attributeId);
    }
}
