package io.spring.jbuy.features.keyword;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.view.BaseView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.util.UUID;

@Value
public class KeywordResponse {

    @Schema(example = "123e4567-e89b-12d3-a456-426614174000")
    @JsonView(BaseView.Admin.class)
    UUID id;

    @Schema(example = "intel processor")
    String name;
}
