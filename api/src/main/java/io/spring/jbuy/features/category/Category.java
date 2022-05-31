package io.spring.jbuy.features.category;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.spring.jbuy.common.annotation.Default;
import io.spring.jbuy.common.entity.DateAuditWithID;
import io.spring.jbuy.features.attribute_type.AttributeType;
import io.spring.jbuy.features.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.engine.backend.types.Aggregable;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category")
@Indexed
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter @Setter @NoArgsConstructor
public class Category extends DateAuditWithID {

    @ManyToMany(mappedBy = "listOfCategory")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonBackReference
    private Set<Product> listOfProduct = new HashSet<>();

    @ManyToMany(mappedBy = "listOfCategory")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonBackReference
    private Set<AttributeType> listOfAttributeType = new HashSet<>();

    @NotNull
    @Column(unique = true, nullable = false)
    @FullTextField(name = "autocompleteCategory", analyzer = "autocomplete_indexing", searchAnalyzer = "autocomplete_search")
    @KeywordField(projectable = Projectable.YES, aggregable = Aggregable.YES)
    @KeywordField(name = "normalizedCategory", normalizer = "category")
    private String name;

    @Default
    public Category(String name) {
        this.name = name;
    }

    @PreRemove
    private void removeCategoriesFromAttributeTypesAndProducts() {
        for (AttributeType attributeType : this.getListOfAttributeType()) {
            attributeType.getListOfCategory().remove(this);
        }

        for (Product product : this.getListOfProduct()) {
            product.getListOfCategory().remove(this);
        }
    }
}
