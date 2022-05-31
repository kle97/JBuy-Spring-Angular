package io.spring.jbuy.features.address;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.validator.ValidationGroup;
import io.spring.jbuy.common.view.BaseView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Value
public class AddressRequest {

    @Schema(example = "298170db-d327-4554-aae2-47016d2132e2")
    @JsonView(BaseView.Create.class)
    @NotNull(groups = ValidationGroup.onCreate.class)
    UUID userId;

    @Schema(example = "123 Main St")
    @NotBlank
    String addressLine1;

    @Schema(example = "Apt #13")
    String addressLine2;

    @Schema(example = "Los Angeles")
    @NotBlank
    String city;

    @Schema(example = "CA")
    @NotBlank
    String state;

    @Schema(example = "90120")
    @NotBlank
    String postalCode;

    @Schema(example = "true")
    @NotNull
    Boolean primaryAddress;
}
