package io.spring.jbuy.features.order_product;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.validator.ValidationGroup;
import io.spring.jbuy.common.view.BaseView;
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

@RestController @RequestMapping("/api/v1/admin/order-products")
@Tag(name = "orderProduct-admin", description = "order product API for admin")
@Validated @PreAuthorize("hasRole(@Role.ADMIN)")
@RequiredArgsConstructor @Slf4j
public class OrderProductAdminController {

    private final OrderProductService orderProductService;

    @GetMapping("/{orderId}/products/{productId}")
    @Operation(summary = "Find order product by order id and product id", tags = "orderProduct-admin")
    public ResponseEntity<OrderProductResponse> getOrderProduct(@PathVariable UUID orderId,
                                                                @PathVariable UUID productId) {
        return ResponseEntity.ok()
                .body(orderProductService.getOrderProductResponseById(orderId, productId));
    }

    @GetMapping("")
    @Operation(summary = "Find all instances of order product as pages", tags = "orderProduct-admin")
    public ResponseEntity<Page<OrderProductResponse>> getOrderProductPageable(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(orderProductService.getOrderProductResponsePageable(pageable));
    }

    @PostMapping("")
    @Operation(summary = "Add a new order product", tags = "orderProduct-admin")
    public ResponseEntity<OrderProductResponse> createOrderProduct(@RequestBody
                                                                   @Validated({ValidationGroup.onCreate.class, Default.class})
                                                                   @JsonView(BaseView.Create.class)
                                                                           OrderProductRequest orderProductRequest) {
        OrderProductResponse response = orderProductService.createOrderProduct(orderProductRequest);
        String orderId = String.valueOf(orderProductRequest.getOrderId());
        String productId = String.valueOf(orderProductRequest.getProductId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{orderId}/products/{productId}")
                .buildAndExpand(orderId, productId).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/{orderId}/products/{productId}")
    @Operation(summary = "Delete a order product by id", tags = "orderProduct-admin")
    public ResponseEntity<Void> deleteOrderProduct(@PathVariable UUID orderId,
                                                   @PathVariable UUID productId) {
        orderProductService.deleteByOrderIdAndProductId(orderId, productId);
        return ResponseEntity.noContent().build();
    }
}
