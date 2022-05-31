package io.spring.jbuy.features.order;

import io.spring.jbuy.common.annotation.Default;
import io.spring.jbuy.common.entity.DateAuditWithID;
import io.spring.jbuy.features.order_product.OrderProduct;
import io.spring.jbuy.features.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter @Setter @NoArgsConstructor
public class Order extends DateAuditWithID {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private List<OrderProduct> listOfOrderProduct = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private Double total;

    @Column(nullable = false, length = 20)
    private String orderStatus;

    @NotBlank
    private String shippingAddressLine1;

    private String shippingAddressLine2;

    @NotBlank
    private String shippingCity;

    @NotBlank
    private String shippingState;

    @NotBlank
    private String shippingPostalCode;

    @Column(nullable = false, updatable = false)
    private LocalDateTime orderDate;

    @Default
    public Order(User user, Double total, String orderStatus, LocalDateTime orderDate) {
        this.user = user;
        this.total = total;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
    }
}
