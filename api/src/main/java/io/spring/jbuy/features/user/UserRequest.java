package io.spring.jbuy.features.user;

import io.spring.jbuy.common.validator.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
public class UserRequest {

    @Schema(example = "nathanieldoak@email.com")
    @NotBlank(groups = ValidationGroup.onCreate.class)
    @Email
    String email;

    @Schema(example = "My password")
    @NotBlank(groups = ValidationGroup.onCreate.class)
    String password;

    @Schema(example = "Nathaniel")
    String firstName;

    @Schema(example = "Doak")
    String lastName;
}
