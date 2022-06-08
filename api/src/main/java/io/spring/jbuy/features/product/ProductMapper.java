package io.spring.jbuy.features.product;

import io.spring.jbuy.common.mapper.ReferenceMapper;
import io.spring.jbuy.features.attribute.AttributeMapper;
import io.spring.jbuy.features.category.CategoryMapper;
import io.spring.jbuy.features.product.dto.ProductDetailResponse;
import io.spring.jbuy.features.product.dto.ProductRequest;
import io.spring.jbuy.features.product.dto.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CategoryMapper.class, AttributeMapper.class, ReferenceMapper.class})
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductResponse toProductResponse(Product product);

    ProductDetailResponse toProductDetailResponse(Product product);

    @Mapping(target = "listOfCartItem", ignore = true)
    @Mapping(target = "listOfOrderProduct", ignore = true)
    @Mapping(target = "listOfReview", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "ratingCount", ignore = true)
    @Mapping(target = "discounted", ignore = true)
    Product toProduct(ProductRequest productRequest);

    @Mapping(target = "listOfCartItem", ignore = true)
    @Mapping(target = "listOfOrderProduct", ignore = true)
    @Mapping(target = "listOfReview", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "ratingCount", ignore = true)
    @Mapping(target = "discounted", ignore = true)
    Product toExistingProduct(ProductRequest productRequest, @MappingTarget Product product);
}
