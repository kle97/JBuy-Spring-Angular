package io.spring.jbuy.features.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data @NoArgsConstructor @AllArgsConstructor
public class EmailCheckRequest {

    @Email
    @NotBlank
    @Schema(example = "nathanieldoak@email.com")
    private String email;
}
