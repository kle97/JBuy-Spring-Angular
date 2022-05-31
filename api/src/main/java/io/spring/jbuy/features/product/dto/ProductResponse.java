package io.spring.jbuy.features.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.util.UUID;

@Value
public class ProductResponse {

    @Schema(example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id;

    @Schema(example = "Dell")
    String brand;

    @Schema(example = "LG 38GL950G 38\" WQHD+ (3840 x 1600) 175Hz HDMI DP G-Sync HDR Curved IPS LED Gaming Monitor")
    String name;

    @Schema(example = "1599.97")
    Double regularPrice;

    @Schema(example = "1199.96")
    Double discountPrice;

    @Schema(example = "5")
    Integer stock;

    @Schema(example = "https://90a1c75758623581b3f8-5c119c3de181c9857fcb2784776b17ef" +
            ".ssl.cf2.rackcdn.com/640961_313940_01_front_thumbnail.jpg")
    String images;

    @Schema(example = "4.2")
    Double averageRating;

    @Schema(example = "55")
    Integer ratingCount;
}
