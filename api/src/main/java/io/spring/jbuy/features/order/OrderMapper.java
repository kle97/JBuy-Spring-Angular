package io.spring.jbuy.features.order;

import io.spring.jbuy.common.mapper.ReferenceMapper;
import io.spring.jbuy.features.order_product.BuyNowOrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ReferenceMapper.class})
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "userId", source = "user.id")
    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "userId", source = "user.id")
    OrderDetailResponse toOrderDetailResponse(Order order);

    @Mapping(target = "user", source = "userId")
    @Mapping(target = "listOfOrderProduct", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "total", ignore = true)
    Order toOrder(OrderRequest orderRequest);

    @Mapping(target = "user", source = "userId")
    @Mapping(target = "listOfOrderProduct", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "total", ignore = true)
    Order buyNowOrderRequestToOrder(BuyNowOrderRequest buyNowOrderRequest);
}
