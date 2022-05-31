package io.spring.jbuy.features.review;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.view.BaseView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class ReviewResponse {

    @Schema(example = "123e4567-e89b-12d3-a456-426614174000")
    @JsonView(BaseView.Create.class)
    UUID userId;

    @Schema(example = "Nathaniel")
    String firstName;

    @Schema(example = "Doak")
    String lastName;

    @Schema(example = "298170db-d327-4554-aae2-47016d2132e2")
    @JsonView(BaseView.Create.class)
    UUID productId;

    @Schema(example = "Good product!")
    String title;

    @Schema(example = "My comment blah blah blah")
    String comment;

    @Schema(example = "5")
    Integer rating;

    @Schema(example = "2021-12-10")
    LocalDateTime reviewDate;

    @Schema(example = "LG 38GL950G 38\" WQHD+ (3840 x 1600) 175Hz HDMI DP G-Sync HDR Curved IPS LED Gaming Monitor")
    String name;

    @Schema(example = "https://90a1c75758623581b3f8-5c119c3de181c9857fcb2784776b17ef.ssl.cf2.rackcdn.com" +
            "/640961_313940_01_front_thumbnail.jpg|https://90a1c75758623581b3f8-5c119c3de181c9857fcb2784776b17ef" +
            ".ssl.cf2.rackcdn.com/640961_313940_02_front_thumbnail.jpg")
    String images;

    @Schema(example = "1199.96")
    Double price;
}
