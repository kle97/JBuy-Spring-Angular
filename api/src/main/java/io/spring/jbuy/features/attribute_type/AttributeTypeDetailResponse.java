package io.spring.jbuy.features.attribute_type;

import io.spring.jbuy.features.attribute.AttributeResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class AttributeTypeDetailResponse {

    @Schema(example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id;

    @Schema(example = "Chipset")
    String name;

    List<AttributeResponse> listOfAttribute;
}
