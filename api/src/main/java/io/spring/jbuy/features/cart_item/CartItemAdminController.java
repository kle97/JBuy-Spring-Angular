package io.spring.jbuy.features.cart_item;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.groups.Default;
import java.net.URI;
import java.util.UUID;

@RestController @RequestMapping("/api/v1/admin/cart-items")
@Tag(name = "cartItem-admin", description = "cart item API for admin")
@Validated @PreAuthorize("hasRole(@Role.ADMIN)")
@RequiredArgsConstructor @Slf4j
public class CartItemAdminController {

    private final CartItemService cartItemService;

    @GetMapping("/{userId}/products/{productId}")
    @Operation(summary = "Find cart item by user id and product id", tags = "cartItem-admin")
    public ResponseEntity<CartItemResponse> getCartItem(@PathVariable UUID userId,
                                                        @PathVariable UUID productId) {
        return ResponseEntity.ok()
                .body(cartItemService.getCartItemResponseById(userId, productId));
    }

    @GetMapping("")
    @Operation(summary = "Find all instances of cart item as pages", tags = "cartItem-admin")
    public ResponseEntity<Page<CartItemResponse>> getCartItemPageable(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(cartItemService.getCartItemResponsePageable(pageable));
    }

    @PostMapping("")
    @Operation(summary = "Add a new cart item", tags = "cartItem-admin")
    public ResponseEntity<CartItemResponse> createCartItem(@RequestBody
                                                           @Validated({ValidationGroup.onCreate.class, Default.class})
                                                           @JsonView(BaseView.Create.class)
                                                                   CartItemRequest cartItemRequest) {
        CartItemResponse response = cartItemService.createCartItem(cartItemRequest);
        String userId = String.valueOf(cartItemRequest.getUserId());
        String productId = String.valueOf(cartItemRequest.getProductId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{userId}/products/{productId}")
                .buildAndExpand(userId, productId).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{userId}/products/{productId}")
    @Operation(summary = "Update a cart item by user id and product id", tags = "cartItem-admin")
    public ResponseEntity<CartItemResponse> updateCartItem(@PathVariable UUID userId,
                                                           @PathVariable UUID productId,
                                                           @RequestBody @Valid @JsonView(BaseView.Update.class)
                                                                   CartItemRequest cartItemRequest) {
        CartItemResponse response = cartItemService.updateCartItem(userId, productId,
                                                                   cartItemRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{userId}/products/{productId}")
    @Operation(summary = "Delete a cart item by id", tags = "cartItem-admin")
    public ResponseEntity<Void> deleteCartItem(@PathVariable UUID userId,
                                               @PathVariable UUID productId) {
        cartItemService.deleteByUserIdAndProductId(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
