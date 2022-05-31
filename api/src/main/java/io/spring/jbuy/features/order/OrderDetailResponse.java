package io.spring.jbuy.features.order;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.view.BaseView;
import io.spring.jbuy.features.order_product.OrderProduct;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Value
public class OrderDetailResponse {

    @Schema(example = "123e4567-e89b-12d3-a456-426614174000")
    @JsonView(BaseView.Admin.class)
    UUID id;

    @Schema(example = "97837862-f3ad-4540-bd9b-d654ae48248b")
    @JsonView(BaseView.Admin.class)
    UUID userId;

    @Schema(example = "123 Main St, Los Angeles, CA 90120")
    String shippingAddress;

    @Schema(example = "123 Main St, Los Angeles, CA 90120")
    String billingAddress;

    @Schema(example = "1298.96")
    Double total;

    @Schema(example = "Complete")
    String orderStatus;

    @Schema(example = "2021-12-10")
    LocalDateTime orderDate;

    List<OrderProduct> listOfOrderProduct;
}
