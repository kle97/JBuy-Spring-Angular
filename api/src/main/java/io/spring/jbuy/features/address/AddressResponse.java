package io.spring.jbuy.features.address;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.view.BaseView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.util.UUID;

@Value
public class AddressResponse {

    @Schema(example = "123e4567-e89b-12d3-a456-426614174000")
    @JsonView(BaseView.Admin.class)
    UUID id;

    @Schema(example = "298170db-d327-4554-aae2-47016d2132e2")
    @JsonView(BaseView.Admin.class)
    UUID userId;

    @Schema(example = "123 Main St")
    String addressLine1;

    @Schema(example = "Apt #13")
    String addressLine2;

    @Schema(example = "Los Angeles")
    String city;

    @Schema(example = "CA")
    String state;

    @Schema(example = "90120")
    String postalCode;

    @Schema(example = "true")
    Boolean primaryAddress;
}
