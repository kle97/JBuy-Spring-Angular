package io.spring.jbuy.features.authority;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.util.UUID;

@Value
public class AuthorityResponse {

    @Schema(example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id;

    @Schema(example = "ROLE_ADMIN")
    String role;
}
