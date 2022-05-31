package io.spring.jbuy.common.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@MappedSuperclass
public abstract class ID implements Serializable {

    private static final long serialVersionUID = 257512386974142L;

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @Version
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long version;

    public UUID getId() {
        initUUID();
        return this.id;
    }

    @PrePersist
    private void prePersist() {
        initUUID();
    }

    private void initUUID() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
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
        return this.getId() != null && other.getId() != null
                && this.getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getId()); // for nullable object
    }
}

