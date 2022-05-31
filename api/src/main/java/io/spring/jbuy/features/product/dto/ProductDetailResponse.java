package io.spring.jbuy.features.product.dto;

import io.spring.jbuy.features.category.CategoryResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class ProductDetailResponse {

    @Schema(example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id;

    @Schema(example = "Dell")
    String brand;

    @Schema(example = "LG 38GL950G 38\" WQHD+ (3840 x 1600) 175Hz HDMI DP G-Sync HDR Curved IPS LED Gaming Monitor")
    String name;

    @Schema(example = "See your way to victory with the innovative 38GL950G-B UltraGear gaming monitor, " +
            "providing the most crisp visuals and the sharpest clarity. You can experience breath-taking " +
            "immersion on a Nano IPS display with a 1ms response time.")
    String description;

    @Schema(example = "1599.97")
    Double regularPrice;

    @Schema(example = "1199.96")
    Double discountPrice;

    @Schema(example = "5")
    Integer stock;

    @Schema(example = "https://90a1c75758623581b3f8-5c119c3de181c9857fcb2784776b17ef.ssl.cf2.rackcdn.com" +
            "/640961_313940_01_front_thumbnail.jpg|https://90a1c75758623581b3f8-5c119c3de181c9857fcb2784776b17ef" +
            ".ssl.cf2.rackcdn.com/640961_313940_02_front_thumbnail.jpg")
    String images;

    @Schema(example = "https://90a1c75758623581b3f8-5c119c3de181c9857fcb2784776b17ef.ssl.cf2.rackcdn.com" +
            "/640961_313940_01_front_mini.jpg|https://90a1c75758623581b3f8-5c119c3de181c9857fcb2784776b17ef" +
            ".ssl.cf2.rackcdn.com/640961_313940_02_front_mini.jpg")
    String thumbnails;

    @Schema(example = "4.2")
    Double averageRating;

    @Schema(example = "55")
    Integer ratingCount;

    List<CategoryResponse> listOfCategory;
}
