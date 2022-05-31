package io.spring.jbuy.features.cart_item;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.validator.ValidationGroup;
import io.spring.jbuy.common.view.BaseView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import javax.validation.Valid;
import javax.validation.groups.Default;
import java.util.List;
import java.util.UUID;

@RestController @RequestMapping("/api/v1/cart-items")
@Tag(name = "cartItem", description = "cart item API")
@Validated
@RequiredArgsConstructor @Slf4j
public class CartItemController {

    private final CartItemService cartItemService;

    @GetMapping("/{userId}/count")
    @Operation(summary = "Find cart item count by user id", tags = "cartItem",
            security = {@SecurityRequirement(name = "httpBasic")})
    @PreAuthorize("hasRole(@Role.ADMIN) or @webSecurity.hasUserId(#userId)")
    public ResponseEntity<Integer> getCartItemCountByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok().body(cartItemService.getCartItemCountByUserId(userId));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Find all instances of cart item by user id", tags = "cartItem",
            security = {@SecurityRequirement(name = "httpBasic")})
    @PreAuthorize("hasRole(@Role.ADMIN) or @webSecurity.hasUserId(#userId)")
    public ResponseEntity<List<CartItemResponse>> getAllCartItemsByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok().body(cartItemService.getAllCartItemsResponseByUserId(userId));
    }

    @PostMapping("")
    @Operation(summary = "Add a new cart item", tags = "cartItem", security = {@SecurityRequirement(name = "httpBasic")})
    @PreAuthorize("hasRole(@Role.ADMIN) or @webSecurity.hasUserId(#cartItemRequest.userId)")
    public ResponseEntity<CartItemResponse> createCartItem(@RequestBody
                                                           @Validated({ValidationGroup.onCreate.class, Default.class})
                                                           @JsonView(BaseView.Create.class)
                                                                   CartItemRequest cartItemRequest) {
        CartItemResponse response = cartItemService.createCartItem(cartItemRequest);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{userId}/products/{productId}")
    @Operation(summary = "Update a cart item by user id and product id", tags = "cartItem",
            security = {@SecurityRequirement(name = "httpBasic")})
    @PreAuthorize("hasRole(@Role.ADMIN) or @webSecurity.hasUserId(#userId)")
    public ResponseEntity<CartItemResponse> updateCartItem(@PathVariable UUID userId,
                                                           @PathVariable UUID productId,
                                                           @RequestBody @Valid @JsonView(BaseView.Update.class)
                                                                   CartItemRequest cartItemRequest) {
        CartItemResponse response = cartItemService.updateCartItem(userId, productId,
                                                                   cartItemRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{userId}/products/{productId}")
    @Operation(summary = "Delete a cart item by id", tags = "cartItem",
            security = {@SecurityRequirement(name = "httpBasic")})
    @PreAuthorize("hasRole(@Role.ADMIN) or @webSecurity.hasUserId(#userId)")
    public ResponseEntity<Void> deleteCartItem(@PathVariable UUID userId,
                                               @PathVariable UUID productId) {
        cartItemService.deleteByUserIdAndProductId(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
