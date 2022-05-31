package io.spring.jbuy.features.admin;

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
@DiscriminatorValue("Admin")
@Getter @Setter @NoArgsConstructor
@ToString(callSuper = true)
public class Admin extends UserProfile {

    private String secretKey;

    @Default
    public Admin(User user, String secretKey) {
        super(user);
        this.secretKey = secretKey;
    }
}
