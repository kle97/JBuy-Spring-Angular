package io.spring.jbuy.features.order_product;

import io.spring.jbuy.common.mapper.ReferenceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ReferenceMapper.class})
public interface OrderProductMapper {

    OrderProductMapper INSTANCE = Mappers.getMapper(OrderProductMapper.class);

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "productId", source = "product.id")
    OrderProductResponse toOrderProductResponse(OrderProduct orderProduct);

    @Mapping(target = "order", source = "orderId")
    @Mapping(target = "product", source = "productId")
    @Mapping(target = "id", ignore = true)
    OrderProduct toOrderProduct(OrderProductRequest orderProductRequest);
}
