package io.spring.jbuy.features.product_attribute;

import io.spring.jbuy.features.attribute.AttributeResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
public class ProductAttributeResponse {

    AttributeResponse attribute;

    @Schema(example = "Full Height")
    String value;
}
