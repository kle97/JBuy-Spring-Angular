package io.spring.jbuy.features.authority;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class AuthorityRequest {

    @Schema(example = "ROLE_ADMIN")
    @NotBlank
    String role;
}
