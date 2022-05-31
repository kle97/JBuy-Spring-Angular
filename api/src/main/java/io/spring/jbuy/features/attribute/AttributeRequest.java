package io.spring.jbuy.features.attribute;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.validator.ValidationGroup;
import io.spring.jbuy.common.view.BaseView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Value
public class AttributeRequest {

    @Schema(example = "298170db-d327-4554-aae2-47016d2132e2")
    @JsonView(BaseView.Create.class)
    @NotNull(groups = ValidationGroup.onCreate.class)
    UUID attributeTypeId;

    @Schema(example = "Form Factor")
    @NotBlank
    String name;
}
