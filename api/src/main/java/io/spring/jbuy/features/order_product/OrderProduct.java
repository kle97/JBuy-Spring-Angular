package io.spring.jbuy.features.order_product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.spring.jbuy.common.annotation.Default;
import io.spring.jbuy.common.entity.DateAudit;
import io.spring.jbuy.features.order.Order;
import io.spring.jbuy.features.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Entity
@Immutable
@Table(name = "order_product")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Getter @NoArgsConstructor
@ToString(exclude = {"order", "product"})
public class OrderProduct extends DateAudit {

    @EmbeddedId
    private OrderProductId id;

    @MapsId("orderId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private Product product;

    @Column(nullable = false, updatable = false)
    private String name;

    @Column(updatable = false, length = 3000)
    private String images;

    @Column(nullable = false, updatable = false)
    @Min(1)
    private Integer quantity;

    @Column(nullable = false, updatable = false)
    private Double price;

    @Default
    public OrderProduct(Order order, Product product, String name,
                        String images, Integer quantity, Double price) {
        this.order = order;
        this.product = product;
        this.id = new OrderProductId(order.getId(), product.getId());
        this.quantity = quantity;
        this.name = name;
        this.images = images;
        this.price = price;
    }
}
