package io.spring.jbuy.features.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
public class ProductDto {

    @Schema(example = "068304")
    String sku;

    @Schema(example = "LG 38GL950G 38\" WQHD+ (3840 x 1600) 175Hz HDMI DP G-Sync HDR Curved IPS LED Gaming Monitor")
    String name;

    @Schema(example = "See your way to victory with the innovative 38GL950G-B UltraGear gaming monitor, " +
            "providing the most crisp visuals and the sharpest clarity. You can experience breath-taking " +
            "immersion on a Nano IPS display with a 1ms response time.")
    String description;

    @Schema(example = "1,599.97")
    Double regularPrice;

    @Schema(example = "1,199.96")
    Double discountPrice;

    @Schema(example = "5")
    Integer quantity;
}
