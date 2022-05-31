package io.spring.jbuy.features.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class CategoryRequest {

    @Schema(example = "Monitor")
    @NotBlank
    String name;
}
