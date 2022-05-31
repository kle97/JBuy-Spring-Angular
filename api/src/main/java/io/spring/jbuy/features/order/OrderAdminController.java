package io.spring.jbuy.features.order;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.validator.ValidationGroup;
import io.spring.jbuy.common.view.BaseView;
import io.spring.jbuy.features.order_product.BuyNowOrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.groups.Default;
import java.net.URI;
import java.util.UUID;

@RestController @RequestMapping("/api/v1/admin/orders")
@Tag(name = "order-admin", description = "order API for admin")
@Validated @PreAuthorize("hasRole(@Role.ADMIN)")
@RequiredArgsConstructor @Slf4j
public class OrderAdminController {

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    @Operation(summary = "Find order by id", tags = "order-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok().body(orderService.getOrderResponseById(orderId));
    }

    @GetMapping("/{userId}/page")
    @Operation(summary = "Find all instances of order as pages by user id", tags = "order",
            security = {@SecurityRequirement(name = "httpBasic")})
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<Page<OrderResponse>> getOrderPageableByUserId(@PathVariable UUID userId,
                                                                              @ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(orderService.getOrderResponsePageableByUserId(userId, pageable));
    }

    @GetMapping("")
    @Operation(summary = "Find all instances of order as pages", tags = "order-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<Page<OrderResponse>> getOrderPageable(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(orderService.getOrderResponsePageable(pageable));
    }

    @PostMapping("")
    @Operation(summary = "Add a new order", tags = "order-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<OrderResponse> createOrder(@RequestBody
                                                     @Validated({ValidationGroup.onCreate.class, Default.class})
                                                     @JsonView(BaseView.Create.class)
                                                             OrderRequest orderRequest) {
        OrderResponse response = orderService.createOrder(orderRequest);
        String orderId = String.valueOf(response.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{orderId}")
                .buildAndExpand(orderId).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/buy-now")
    @Operation(summary = "Add a new buy now order", tags = "order-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<OrderResponse> createBuyNowOrder(@RequestBody
                                                           @Validated({ValidationGroup.onCreate.class, Default.class})
                                                           @JsonView(BaseView.Create.class)
                                                                   BuyNowOrderRequest buyNowOrderRequest) {
        OrderResponse response = orderService.createBuyNowOrder(buyNowOrderRequest);
        String orderId = String.valueOf(response.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{orderId}")
                .buildAndExpand(orderId).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "Delete an order by id", tags = "order-admin")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderId) {
        orderService.deleteById(orderId);
        return ResponseEntity.noContent().build();
    }
}
