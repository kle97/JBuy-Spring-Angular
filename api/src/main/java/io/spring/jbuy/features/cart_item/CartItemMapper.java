package io.spring.jbuy.features.cart_item;

import io.spring.jbuy.common.mapper.ReferenceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ReferenceMapper.class})
public interface CartItemMapper {

    CartItemMapper INSTANCE = Mappers.getMapper(CartItemMapper.class);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "regularPrice", source = "product.regularPrice")
    @Mapping(target = "discountPrice", source = "product.discountPrice")
    @Mapping(target = "stock", source = "product.stock")
    @Mapping(target = "images", source = "product.images")
    CartItemResponse toCartItemResponse(CartItem cartItem);

    @Mapping(target = "user", source = "userId")
    @Mapping(target = "product", source = "productId")
    @Mapping(target = "id", ignore = true)
    CartItem toCartItem(CartItemRequest cartItemRequest);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "id", ignore = true)
    CartItem toExistingCartItem(CartItemRequest cartItemRequest, @MappingTarget CartItem cartItem);
}
