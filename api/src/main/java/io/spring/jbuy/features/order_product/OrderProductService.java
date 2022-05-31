package io.spring.jbuy.features.order_product;

import io.spring.jbuy.common.exception.ResourceNotFoundException;
import io.spring.jbuy.features.product.Product;
import io.spring.jbuy.features.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.UUID;

@Service
@RequiredArgsConstructor @Slf4j
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;
    private final OrderProductMapper orderProductMapper;

    @Transactional(readOnly = true)
    public OrderProduct getOrderProductById(UUID orderId, UUID productId) {
        return orderProductRepository.findById_OrderIdAndId_ProductId(orderId, productId)
                .orElseThrow(() -> new ResourceNotFoundException(OrderProduct.class, User.class,
                                                                 orderId, Product.class, productId));
    }

    @Transactional(readOnly = true)
    public OrderProductResponse getOrderProductResponseById(UUID orderId, UUID productId) {
        return orderProductMapper.toOrderProductResponse(getOrderProductById(orderId, productId));
    }

    @Transactional(readOnly = true)
    public Page<OrderProductResponse> getOrderProductResponsePageable(Pageable pageable) {
        return orderProductRepository.findAll(pageable).map(orderProductMapper::toOrderProductResponse);
    }

    @Transactional(readOnly = true)
    public Page<OrderProductResponse> getOrderProductResponsePageableByOrderId(UUID orderId, Pageable pageable) {
        return orderProductRepository
                .findAllById_OrderId(orderId, pageable)
                .map(orderProductMapper::toOrderProductResponse);
    }

    @Transactional
    public OrderProductResponse createOrderProduct(OrderProductRequest orderProductRequest) {
        validateOrderProductRequest(orderProductRequest);
        OrderProduct transientOrderProduct = orderProductMapper.toOrderProduct(orderProductRequest);
        return orderProductMapper.toOrderProductResponse(
                orderProductRepository.save(transientOrderProduct));
    }

    @Transactional
    public void deleteByOrderIdAndProductId(UUID orderId, UUID productId) {
        if (orderProductRepository.existsById_OrderIdAndId_ProductId(orderId, productId)) {
            orderProductRepository.deleteById_OrderIdAndId_ProductId(orderId, productId);
        } else {
            throw new ResourceNotFoundException(OrderProduct.class, User.class,
                                                orderId, Product.class, productId);
        }
    }

    private void validateOrderProductRequest(OrderProductRequest orderProductRequest) {
        if (orderProductRepository.existsById_OrderIdAndId_ProductId(orderProductRequest.getOrderId(),
                                                                        orderProductRequest.getProductId())) {
            throw new ValidationException("Order product already exists!");
        }
    }
}
