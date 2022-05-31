package io.spring.jbuy.features.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class PasswordChangeRequest {

    @Schema(example = "My current password")
    @NotBlank
    String currentPassword;

    @Schema(example = "My new password")
    @NotBlank
    String newPassword;
}
