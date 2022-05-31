package io.spring.jbuy.features.cart_item;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.validator.ValidationGroup;
import io.spring.jbuy.common.view.BaseView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Value
public class CartItemRequest {

    @Schema(example = "123e4567-e89b-12d3-a456-426614174000")
    @JsonView(BaseView.Create.class)
    @NotNull(groups = ValidationGroup.onCreate.class)
    UUID userId;

    @Schema(example = "298170db-d327-4554-aae2-47016d2132e2")
    @JsonView(BaseView.Create.class)
    @NotNull(groups = ValidationGroup.onCreate.class)
    UUID productId;

    @Schema(example = "1")
    @NotNull @Min(1)
    Integer quantity;
}
