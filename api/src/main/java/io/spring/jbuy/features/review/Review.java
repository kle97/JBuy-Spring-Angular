package io.spring.jbuy.features.review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.spring.jbuy.common.annotation.Default;
import io.spring.jbuy.common.entity.DateAudit;
import io.spring.jbuy.features.product.Product;
import io.spring.jbuy.features.user.User;
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
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter @Setter @NoArgsConstructor
@ToString(exclude = {"user", "product"})
public class Review extends DateAudit {

    @EmbeddedId
    private ReviewId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
    @JsonBackReference
    private Product product;

    @NotBlank
    private String title;

    @NotBlank
    @Column(length = 3000)
    private String comment;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, updatable = false)
    private LocalDateTime reviewDate;

    @Default
    public Review(User user, Product product, String title, String comment, Integer rating, LocalDateTime reviewDate) {
        this.user = user;
        this.product = product;
        this.id = new ReviewId(user.getId(), product.getId());
        this.title = title;
        this.comment = comment;
        this.rating = rating;
        this.reviewDate = reviewDate;
    }
}
