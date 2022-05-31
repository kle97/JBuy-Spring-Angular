package io.spring.jbuy.features.attribute;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.spring.jbuy.common.annotation.Default;
import io.spring.jbuy.common.entity.DateAuditWithID;
import io.spring.jbuy.features.attribute_type.AttributeType;
import io.spring.jbuy.features.product_attribute.ProductAttribute;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "attribute")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter @Setter @NoArgsConstructor
public class Attribute extends DateAuditWithID {

    @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonBackReference
    private Set<ProductAttribute> listOfProductAttribute = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attribute_type_id", nullable = false)
    private AttributeType attributeType;

    @NotNull
    private String name;

    @Default
    public Attribute(AttributeType attributeType, String name) {
        this.attributeType = attributeType;
        this.name = name;
    }
}
