package io.spring.jbuy.features.order;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.validator.ValidationGroup;
import io.spring.jbuy.common.view.BaseView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Value
public class OrderRequest {

    @Schema(example = "97837862-f3ad-4540-bd9b-d654ae48248b")
    @JsonView(BaseView.Create.class)
    @NotNull(groups = ValidationGroup.onCreate.class)
    UUID userId;

    @Schema(example = "123 Main St")
    @NotBlank
    String shippingAddressLine1;

    @Schema(example = "Apt #13")
    String shippingAddressLine2;

    @Schema(example = "Los Angeles")
    @NotBlank
    String shippingCity;

    @Schema(example = "CA")
    @NotBlank
    String shippingState;

    @Schema(example = "90120")
    @NotBlank
    String shippingPostalCode;

    @Schema(example = "Complete")
    @JsonView(BaseView.Admin.class)
    String orderStatus;
}
