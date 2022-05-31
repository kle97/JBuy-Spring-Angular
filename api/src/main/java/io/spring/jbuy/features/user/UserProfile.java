package io.spring.jbuy.features.user;

import io.spring.jbuy.common.entity.DateAudit;
import io.spring.jbuy.common.entity.ID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "user_profile")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_profile_type")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter @Setter @NoArgsConstructor
@ToString(exclude = {"user"})
public class UserProfile extends DateAudit implements Serializable {
    private static final long serialVersionUID = 92461720176821L;

    @Id
    @Setter(value = AccessLevel.NONE)
    private UUID id;

    @Version
    @Setter(value = AccessLevel.NONE)
    private Long version;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;

    public UserProfile(User user) {
        if (user != null) {
            this.user = user;
            this.id = user.getId();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ID)) {
            return false;
        }

        ID other = (ID) obj;
        return this.getId() != null && other.getId() != null && this.getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId());
    }
}
