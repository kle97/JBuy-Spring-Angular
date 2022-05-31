package io.spring.jbuy.features.keyword;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class KeywordRequest {

    @Schema(example = "intel processor")
    @NotBlank
    String name;
}
