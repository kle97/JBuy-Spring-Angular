package io.spring.jbuy.features.product;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto toProductDto(Product product);

    Product toProduct(ProductDto productDto);
}
