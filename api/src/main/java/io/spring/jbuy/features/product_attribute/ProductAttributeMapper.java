package io.spring.jbuy.features.product_attribute;

import io.spring.jbuy.common.mapper.ReferenceMapper;
import io.spring.jbuy.features.attribute.AttributeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ReferenceMapper.class, AttributeMapper.class})
public interface ProductAttributeMapper {

    ProductAttributeMapper INSTANCE = Mappers.getMapper(ProductAttributeMapper.class);

    ProductAttributeResponse toProductAttributeResponse(ProductAttribute productAttribute);

    @Mapping(target = "product", source = "productId")
    @Mapping(target = "attribute", source = "attributeId")
    @Mapping(target = "id", ignore = true)
    ProductAttribute toProductAttribute(ProductAttributeRequest productAttributeRequest);

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "attribute", ignore = true)
    @Mapping(target = "id", ignore = true)
    ProductAttribute toExistingProductAttribute(ProductAttributeRequest productAttributeRequest,
                                                @MappingTarget ProductAttribute productAttribute);
}
