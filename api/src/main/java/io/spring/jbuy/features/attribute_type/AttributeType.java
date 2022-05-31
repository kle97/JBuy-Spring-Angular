package io.spring.jbuy.features.attribute_type;

import io.spring.jbuy.common.annotation.Default;
import io.spring.jbuy.common.entity.DateAuditWithID;
import io.spring.jbuy.features.attribute.Attribute;
import io.spring.jbuy.features.category.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "attribute_type")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter @Setter @NoArgsConstructor
public class AttributeType extends DateAuditWithID {

    @OneToMany(mappedBy = "attributeType", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Attribute> listOfAttribute = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "attribute_type_category",
            joinColumns = @JoinColumn(name = "attribute_type_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Category> listOfCategory = new HashSet<>();

    @NotNull
    @Column(unique = true, nullable = false)
    private String name;

    @Default
    public AttributeType(String name) {
        this.name = name;
    }
}
