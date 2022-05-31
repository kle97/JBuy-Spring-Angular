package io.spring.jbuy.features.user;

import lombok.Value;

import javax.validation.constraints.NotEmpty;
import java.util.Set;
import java.util.UUID;

@Value
public class AuthorityChangeRequest {

    @NotEmpty
    Set<UUID> listOfAuthorityId;
}
