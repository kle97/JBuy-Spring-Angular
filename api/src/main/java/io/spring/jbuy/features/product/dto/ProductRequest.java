package io.spring.jbuy.features.product.dto;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.view.BaseView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Value
public class ProductRequest {

    @JsonView(BaseView.Create.class)
    List<UUID> listOfCategory;

    @Schema(example = "Dell")
    @NotBlank
    String brand;

    @Schema(example = "LG 38GL950G 38\" WQHD+ (3840 x 1600) 175Hz HDMI DP G-Sync HDR Curved IPS LED Gaming Monitor")
    @NotBlank
    String name;

    @Schema(example = "See your way to victory with the innovative 38GL950G-B UltraGear gaming monitor, " +
            "providing the most crisp visuals and the sharpest clarity. You can experience breath-taking " +
            "immersion on a Nano IPS display with a 1ms response time.")
    @Size(max = 3000)
    String description;

    @Schema(example = "1599.97")
    @NotNull
    Double regularPrice;

    @Schema(example = "1199.96")
    @NotNull
    Double discountPrice;

    @Schema(example = "5")
    @NotNull @Min(0)
    Integer stock;

    @Schema(example = "https://90a1c75758623581b3f8-5c119c3de181c9857fcb2784776b17ef.ssl.cf2.rackcdn.com" +
            "/640961_313940_01_front_thumbnail.jpg|https://90a1c75758623581b3f8-5c119c3de181c9857fcb2784776b17ef" +
            ".ssl.cf2.rackcdn.com/640961_313940_02_front_thumbnail.jpg")
    @Size(max = 3000)
    String images;

    @Schema(example = "https://90a1c75758623581b3f8-5c119c3de181c9857fcb2784776b17ef.ssl.cf2.rackcdn.com" +
            "/640961_313940_01_front_mini.jpg|https://90a1c75758623581b3f8-5c119c3de181c9857fcb2784776b17ef" +
            ".ssl.cf2.rackcdn.com/640961_313940_02_front_mini.jpg")
    @Size(max = 3000)
    String thumbnails;
}
