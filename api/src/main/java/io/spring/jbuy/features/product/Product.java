package io.spring.jbuy.features.product;

import io.spring.jbuy.common.entity.DateAuditWithID;
import io.spring.jbuy.features.cart_item.CartItem;
import io.spring.jbuy.features.category.Category;
import io.spring.jbuy.features.order_product.OrderProduct;
import io.spring.jbuy.features.product.brigde.ProductAttributeBinder;
import io.spring.jbuy.features.product_attribute.ProductAttribute;
import io.spring.jbuy.features.review.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.engine.backend.types.Aggregable;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.PropertyBinderRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.PropertyBinding;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "product")
@Indexed
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"listOfProductAttribute", "listOfCategory"})
public class Product extends DateAuditWithID {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    @GenericField(projectable = Projectable.YES)
    private UUID id;

    @Builder.Default
    @PropertyBinding(binder = @PropertyBinderRef(type = ProductAttributeBinder.class))
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<ProductAttribute> listOfProductAttribute = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<CartItem> listOfCartItem = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private List<OrderProduct> listOfOrderProduct = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Review> listOfReview = new ArrayList<>();

    @Builder.Default
    @IndexedEmbedded
    @ManyToMany
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Category> listOfCategory = new HashSet<>();

    @KeywordField(projectable = Projectable.YES, aggregable = Aggregable.YES)
    @FullTextField(name = "autocompleteBrand", analyzer = "autocomplete_indexing", searchAnalyzer = "autocomplete_search")
    private String brand;

    // custom analyzer "english" defined in common/search/CustomLuceneAnalysisConfigurer
    @Column(nullable = false) @NotNull
    @FullTextField(analyzer = "english", projectable = Projectable.YES)
    private String name;

    @FullTextField(analyzer = "english")
    @Column(length = 3000)
    private String description;

    @GenericField(projectable = Projectable.YES)
    @Column(nullable = false) @NotNull @Min(0)
    private Double regularPrice;

    @GenericField(projectable = Projectable.YES, sortable = Sortable.YES, aggregable = Aggregable.YES)
    @Column(nullable = false) @NotNull @Min(0)
    private Double discountPrice;

    @GenericField(projectable = Projectable.YES)
    @Min(0)
    private Integer stock;

    @GenericField(projectable = Projectable.YES)
    @Column(length = 3000)
    private String images;

    @Column(length = 3000)
    private String thumbnails;

    @GenericField(projectable = Projectable.YES, sortable = Sortable.YES, aggregable = Aggregable.YES)
    private Double averageRating;

    @GenericField(projectable = Projectable.YES, sortable = Sortable.YES)
    private Integer ratingCount;

    @GenericField(projectable = Projectable.YES, aggregable = Aggregable.YES)
    private Boolean discounted;

    public static class ProductBuilder {
        public String toString() {
            return "Product.ProductBuilder(brand=" + this.brand
                    + ", name=" + this.name + ", description=" + this.description
                    + ", regularPrice=" + this.regularPrice + ", discountPrice=" + this.discountPrice
                    + ", stock=" + this.stock + ", images=" + this.images
                    + ", thumbnails=" + this.thumbnails + ", averageRating=" + this.averageRating
                    + ", ratingCount=" + this.ratingCount + ", discounted=" + this.discounted + ")";
        }
    }

    public UUID getId() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        return this.id;
    }
}
