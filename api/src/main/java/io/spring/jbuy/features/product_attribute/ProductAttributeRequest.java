package io.spring.jbuy.features.product_attribute;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.validator.ValidationGroup;
import io.spring.jbuy.common.view.BaseView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Value
public class ProductAttributeRequest {

    @Schema(example = "123e4567-e89b-12d3-a456-426614174000")
    @JsonView(BaseView.Create.class)
    @NotNull(groups = ValidationGroup.onCreate.class)
    UUID productId;

    @Schema(example = "298170db-d327-4554-aae2-47016d2132e2")
    @JsonView(BaseView.Create.class)
    @NotNull(groups = ValidationGroup.onCreate.class)
    UUID attributeId;

    @Schema(example = "Full Height")
    @Size(max = 2000)
    String value;
}
