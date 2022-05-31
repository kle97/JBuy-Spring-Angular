package io.spring.jbuy.features.attribute_type;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.view.BaseView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@Value
public class AttributeTypeRequest {

    @JsonView(BaseView.Create.class)
    List<UUID> listOfCategory;

    @Schema(example = "Chipset")
    @NotBlank
    String name;
}
