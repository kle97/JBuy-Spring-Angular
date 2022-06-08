package io.spring.jbuy.features.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
public class ProductAttributeFacet {

    @Schema(example = "Full Height")
    String value;

    @Schema(example = "5")
    Long amount;

    @Schema(example = "false")
    Boolean checked;
}
