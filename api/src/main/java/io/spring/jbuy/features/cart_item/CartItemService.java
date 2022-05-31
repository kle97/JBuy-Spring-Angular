package io.spring.jbuy.features.cart_item;

import io.spring.jbuy.common.exception.ResourceNotFoundException;
import io.spring.jbuy.features.customer.Customer;
import io.spring.jbuy.features.customer.CustomerService;
import io.spring.jbuy.features.product.Product;
import io.spring.jbuy.features.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor @Slf4j
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CustomerService customerService;
    private final CartItemMapper cartItemMapper;

    @Transactional(readOnly = true)
    public CartItem getCartItemById(UUID userId, UUID productId) {
        return cartItemRepository.findById_UserIdAndId_ProductId(userId, productId)
                .orElseThrow(() -> new ResourceNotFoundException(CartItem.class, User.class,
                                                                 userId, Product.class, productId));
    }

    @Transactional(readOnly = true)
    public CartItemResponse getCartItemResponseById(UUID userId, UUID productId) {
        return cartItemMapper.toCartItemResponse(getCartItemById(userId, productId));
    }

    @Transactional(readOnly = true)
    public Page<CartItemResponse> getCartItemResponsePageable(Pageable pageable) {
        return cartItemRepository.findAll(pageable).map(cartItemMapper::toCartItemResponse);
    }

    @Transactional(readOnly = true)
    public List<CartItem> getAllCartItemsByUserId(UUID userId) {
        return cartItemRepository.findAllById_UserId(userId);
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> getAllCartItemsResponseByUserId(UUID userId) {
        return this.getAllCartItemsByUserId(userId)
                .stream()
                .map(cartItemMapper::toCartItemResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Integer getCartItemCountByUserId(UUID userId) {
        Customer customer = customerService.getCustomerById(userId);
        return customer.getTotalCartItemQuantity();
    }

    @Transactional
    public CartItemResponse createCartItem(CartItemRequest cartItemRequest) {
        UUID userId = cartItemRequest.getUserId();
        Customer customer = customerService.getCustomerById(userId);
        UUID productId = cartItemRequest.getProductId();
        if (cartItemRepository.existsById_UserIdAndId_ProductId(userId, productId)) {
            CartItem currentCartItem = getCartItemById(userId, productId);
            currentCartItem.setQuantity(currentCartItem.getQuantity() + cartItemRequest.getQuantity());
            customer.addCartItemQuantity(cartItemRequest.getQuantity());
            return cartItemMapper.toCartItemResponse(currentCartItem);
        } else {
            CartItem transientCartItem = cartItemMapper.toCartItem(cartItemRequest);
            customer.addCartItemQuantity(cartItemRequest.getQuantity());
            return cartItemMapper.toCartItemResponse(
                    cartItemRepository.save(transientCartItem));
        }
    }

    @Transactional
    public CartItemResponse updateCartItem(UUID userId, UUID productId,
                                           CartItemRequest cartItemRequest) {
        CartItem currentCartItem = getCartItemById(userId, productId);
        Customer customer = customerService.getCustomerById(userId);
        if (currentCartItem.getQuantity() > cartItemRequest.getQuantity()) {
            customer.removeCartItemQuantity(currentCartItem.getQuantity() - cartItemRequest.getQuantity());
        } else {
            customer.addCartItemQuantity(cartItemRequest.getQuantity() - currentCartItem.getQuantity());
        }
        return cartItemMapper.toCartItemResponse(
                cartItemMapper.toExistingCartItem(cartItemRequest, currentCartItem));
    }

    @Transactional
    public void deleteByUserIdAndProductId(UUID userId, UUID productId) {
        CartItem currentCartItem = getCartItemById(userId, productId);
        Customer customer = customerService.getCustomerById(userId);
        customer.removeCartItemQuantity(currentCartItem.getQuantity());
        cartItemRepository.deleteById_UserIdAndId_ProductId(userId, productId);
    }
}
