package io.spring.jbuy.features.customer;

import io.spring.jbuy.common.annotation.Default;
import io.spring.jbuy.features.user.User;
import io.spring.jbuy.features.user.UserProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Customer")
@Getter @Setter @NoArgsConstructor
@ToString(callSuper = true, exclude = {})
public class Customer extends UserProfile {

    private Integer totalCartItemQuantity;

    @Default
    public Customer(User user) {
        super(user);
        this.totalCartItemQuantity = 0;
    }

    public void addCartItemQuantity(int quantity) {
        if (this.totalCartItemQuantity + quantity < 0) {
            this.totalCartItemQuantity = 0;
        } else {
            this.totalCartItemQuantity += quantity;
        }
    }

    public void removeCartItemQuantity(int quantity) {
        if (this.totalCartItemQuantity - quantity < 0) {
            this.totalCartItemQuantity = 0;
        } else {
            this.totalCartItemQuantity -= quantity;
        }
    }
}
