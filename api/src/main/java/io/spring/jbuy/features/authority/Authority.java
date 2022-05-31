package io.spring.jbuy.features.authority;

import io.spring.jbuy.common.annotation.Default;
import io.spring.jbuy.common.entity.DateAuditWithID;
import io.spring.jbuy.features.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;

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
@Table(name = "authority")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter @Setter @NoArgsConstructor
@ToString(exclude = {"listOfUser"})
public class Authority extends DateAuditWithID implements GrantedAuthority {

    @NotNull
    @Column(unique = true)
    private String role;

    @ManyToMany(mappedBy = "listOfAuthority")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<User> listOfUser = new HashSet<>();

    @Default
    public Authority(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return this.role;
    }

    @PreRemove
    private void removeAuthoritiesFromUsers() {
        for (User user : this.getListOfUser()) {
            user.getListOfAuthority().remove(this);
        }
    }
}
