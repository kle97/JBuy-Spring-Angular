package io.spring.jbuy.features.order_product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController @RequestMapping("/api/v1/order-products")
@Tag(name = "orderProduct", description = "order product API")
@Validated
@RequiredArgsConstructor @Slf4j
public class OrderProductController {

    private final OrderProductService orderProductService;

    @GetMapping("/{orderId}/page")
    @Operation(summary = "Find all instances of order product as pages by order id", tags = "orderProduct")
    @PreAuthorize("hasRole(@Role.ADMIN) or @orderService.doesOrderBelongToCurrentUser(#orderId)")
    public ResponseEntity<Page<OrderProductResponse>> getOrderProductPageableByOrderId(
            @PathVariable UUID orderId,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(orderProductService.getOrderProductResponsePageableByOrderId(orderId, pageable));
    }
}
