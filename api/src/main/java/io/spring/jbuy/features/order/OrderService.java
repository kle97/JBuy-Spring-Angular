package io.spring.jbuy.features.order;

import io.spring.jbuy.common.exception.ResourceNotFoundException;
import io.spring.jbuy.features.authentication.CustomUserDetailsService;
import io.spring.jbuy.features.cart_item.CartItem;
import io.spring.jbuy.features.cart_item.CartItemRepository;
import io.spring.jbuy.features.cart_item.CartItemService;
import io.spring.jbuy.features.order_product.BuyNowOrderRequest;
import io.spring.jbuy.features.order_product.OrderProduct;
import io.spring.jbuy.features.product.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.spring.jbuy.features.order.OrderStatus.COMPLETE;

@Service
@RequiredArgsConstructor @Slf4j
public class OrderService {

    private final CustomUserDetailsService customUserDetailsService;
    private final CartItemRepository cartItemRepository;
    private final CartItemService cartItemService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional(readOnly = true)
    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(Order.class, orderId));
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderResponseById(UUID orderId) {
        return orderMapper.toOrderResponse(getOrderById(orderId));
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrderResponsePageable(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toOrderResponse);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrderResponsePageableByUserId(UUID userId, Pageable pageable) {
        return orderRepository
                .findAllByUser_Id(userId, pageable)
                .map(orderMapper::toOrderResponse);
    }

//    @Transactional(readOnly = true)
//    public Page<OrderDetailResponse> getOrderDetailResponsePageableByUserId(UUID userId, Pageable pageable) {
//        return orderRepository
//                .findWithOrderProductsByUser_Id(userId, pageable)
//                .map(orderMapper::toOrderDetailResponse);
//    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        List<CartItem> listOfCartItem = cartItemRepository.findAllById_UserId(orderRequest.getUserId());
        long cartItemCount = listOfCartItem.size();

        if (cartItemCount == 0) {
            throw new ValidationException("No item in cart!");
        }

        double total = 0;
        List<OrderProduct> listOfOrderProduct = new ArrayList<>();
        Order transientOrder = orderMapper.toOrder(orderRequest);
        for (CartItem cartItem : listOfCartItem) {
            Product product = cartItem.getProduct();
            OrderProduct newOrderProduct = new OrderProduct(transientOrder,
                                                            product,
                                                            product.getName(),
                                                            product.getImages(),
                                                            cartItem.getQuantity(),
                                                            product.getDiscountPrice());
            if (cartItem.getQuantity() > product.getStock()) {
                throw new ValidationException("Product's quantity is not enough!");
            }
//            product.setStock(product.getStock() - cartItem.getQuantity());
            total += newOrderProduct.getPrice() * newOrderProduct.getQuantity();
            listOfOrderProduct.add(newOrderProduct);
        }

        cartItemRepository.deleteAllById_UserId(orderRequest.getUserId());

        transientOrder.setListOfOrderProduct(listOfOrderProduct);
        transientOrder.setTotal(total);

        //TODO: implement processing and payment service before completing order
        transientOrder.setOrderStatus(COMPLETE.status());
        transientOrder.setOrderDate(LocalDateTime.now());

        return orderMapper.toOrderResponse(orderRepository.save(transientOrder));
    }

    @Transactional
    public OrderResponse createBuyNowOrder(BuyNowOrderRequest buyNowOrderRequest) {
        UUID userId = buyNowOrderRequest.getUserId();
        UUID productId = buyNowOrderRequest.getProductId();
        CartItem cartItem = cartItemService.getCartItemById(userId, productId);

        Order transientOrder = orderMapper.buyNowOrderRequestToOrder(buyNowOrderRequest);

        Product product = cartItem.getProduct();
        OrderProduct newOrderProduct = new OrderProduct(transientOrder,
                                                        product,
                                                        product.getName(),
                                                        product.getImages(),
                                                        cartItem.getQuantity(),
                                                        product.getDiscountPrice());
        if (cartItem.getQuantity() > product.getStock()) {
            throw new ValidationException("Product's quantity is not enough!");
        }
//        product.setStock(product.getStock() - cartItem.getQuantity());

        List<OrderProduct> listOfOrderProduct = new ArrayList<>();
        listOfOrderProduct.add(newOrderProduct);

        cartItemService.deleteByUserIdAndProductId(userId, productId);

        transientOrder.setListOfOrderProduct(listOfOrderProduct);
        transientOrder.setTotal(newOrderProduct.getPrice() * newOrderProduct.getQuantity());

        //TODO: implement processing and payment service before completing order
        transientOrder.setOrderStatus(COMPLETE.status());
        transientOrder.setOrderDate(LocalDateTime.now());

        return orderMapper.toOrderResponse(orderRepository.save(transientOrder));
    }

    @Transactional
    public void deleteById(UUID orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
        } else {
            throw new ResourceNotFoundException(Order.class, orderId);
        }
    }

    @Transactional(readOnly = true)
    public OrderResponse getAndValidateOrderById(UUID orderId) {
        Order order = this.getOrderById(orderId);
        if (!customUserDetailsService.hasUserId(order.getUser().getId()) && !customUserDetailsService.isAdmin()) {
            throw new AuthenticationServiceException("Unauthorized request!");
        }
        return this.orderMapper.toOrderResponse(order);
    }

    @Transactional(readOnly = true)
    public Boolean doesOrderBelongToCurrentUser(UUID orderId) {
        Order order = this.getOrderById(orderId);
        return customUserDetailsService.hasUserId(order.getUser().getId())
                || customUserDetailsService.isAdmin();
    }
}
