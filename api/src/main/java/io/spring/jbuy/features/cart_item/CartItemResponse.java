package io.spring.jbuy.features.cart_item;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.view.BaseView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.util.UUID;

@Value
public class CartItemResponse {

    @Schema(example = "123e4567-e89b-12d3-a456-426614174000")
    @JsonView(BaseView.Admin.class)
    UUID userId;

    @Schema(example = "298170db-d327-4554-aae2-47016d2132e2")
    UUID productId;

    @Schema(example = "LG 38GL950G 38\" WQHD+ (3840 x 1600) 175Hz HDMI DP G-Sync HDR Curved IPS LED Gaming Monitor")
    String name;

    @Schema(example = "1599.97")
    Double regularPrice;

    @Schema(example = "1199.96")
    Double discountPrice;

    @Schema(example = "5")
    Integer stock;

    @Schema(example = "https://90a1c75758623581b3f8-5c119c3de181c9857fcb2784776b17ef.ssl.cf2.rackcdn.com" +
            "/640961_313940_01_front_mini.jpg|https://90a1c75758623581b3f8-5c119c3de181c9857fcb2784776b17ef" +
            ".ssl.cf2.rackcdn.com/640961_313940_02_front_mini.jpg")
    String images;

    @Schema(example = "1")
    Integer quantity;
}
