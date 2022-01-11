package io.spring.jbuy.features.product;

import io.spring.jbuy.common.entity.DateAuditWithID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "product")
@Indexed
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {})
public class Product extends DateAuditWithID {

    @NotNull
    @Column(unique = true, nullable = false)
    @KeywordField
    private String sku;

    @NotNull
//    @FullTextField(analyzer = "english")
    @FullTextField
    private String name;

    //    @FullTextField(analyzer = "english")
    @FullTextField
    @Column(length = 3000)
    private String description;

    private Double regularPrice;

    private Double discountPrice;

    private Integer quantity;

}
