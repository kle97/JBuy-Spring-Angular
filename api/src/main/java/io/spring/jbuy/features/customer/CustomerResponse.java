package io.spring.jbuy.features.customer;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.validator.ValidationGroup;
import io.spring.jbuy.common.view.BaseView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Value
public class CustomerResponse {

    @Schema(example = "123e4567-e89b-12d3-a456-426614174000")
    @JsonView(BaseView.Admin.class)
    UUID id;

    @Schema(example = "97837862-f3ad-4540-bd9b-d654ae48248b")
    @NotNull(groups = ValidationGroup.onCreate.class)
    @JsonView(BaseView.Admin.class)
    UUID userId;

    @Schema(example = "nathanieldoak@email.com")
    String email;

    @Schema(example = "Nathaniel")
    String firstName;

    @Schema(example = "Doak")
    String lastName;
}
