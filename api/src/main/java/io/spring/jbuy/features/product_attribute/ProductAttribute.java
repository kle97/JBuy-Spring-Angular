package io.spring.jbuy.features.product_attribute;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.spring.jbuy.common.annotation.Default;
import io.spring.jbuy.common.entity.DateAudit;
import io.spring.jbuy.features.attribute.Attribute;
import io.spring.jbuy.features.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "product_attribute")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter @Setter @NoArgsConstructor
@ToString(exclude = {"product", "attribute"})
public class ProductAttribute extends DateAudit {

    @EmbeddedId
    private ProductAttributeId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private Product product;

    @MapsId("attributeId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attribute_id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private Attribute attribute;

    @Column(length = 2000)
    private String value;

    @Default
    public ProductAttribute(Product product, Attribute attribute, String value) {
        this.product = product;
        this.attribute = attribute;
        this.id = new ProductAttributeId(product.getId(), attribute.getId());
        this.value = value;
    }
}
