package io.spring.jbuy.features.order_product;

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
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"order_id", "product_id"})})
public class OrderProductId implements Serializable {

    private static final long serialVersionUID = 87395123164L;

    private UUID orderId;

    private UUID productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderProductId)) {
            return false;
        }
        OrderProductId that = (OrderProductId) o;
        return Objects.equals(productId, that.productId) && Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, productId);
    }
}
