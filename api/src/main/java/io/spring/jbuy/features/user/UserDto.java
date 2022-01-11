package io.spring.jbuy.features.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
public class UserDto {

    @Schema(example = "nathanieldoak")
    String username;

    @Schema(example = "Nathaniel")
    String firstName;

    @Schema(example = "Doak")
    String lastName;
}
