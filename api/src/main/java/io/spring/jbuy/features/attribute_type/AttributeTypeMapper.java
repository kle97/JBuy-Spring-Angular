package io.spring.jbuy.features.attribute_type;

import io.spring.jbuy.common.mapper.ReferenceMapper;
import io.spring.jbuy.features.attribute.AttributeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ReferenceMapper.class, AttributeMapper.class})
public interface AttributeTypeMapper {

    AttributeTypeMapper INSTANCE = Mappers.getMapper(AttributeTypeMapper.class);

    AttributeTypeResponse toAttributeTypeResponse(AttributeType attributeType);

    AttributeTypeDetailResponse toAttributeTypeDetailResponse(AttributeType attributeType);

    @Mapping(target = "listOfAttribute", ignore = true)
    AttributeType toAttributeType(AttributeTypeRequest attributeTypeRequest);

    @Mapping(target = "listOfAttribute", ignore = true)
    @Mapping(target = "listOfCategory", ignore = true)
    AttributeType toExistingAttributeType(AttributeTypeRequest attributeTypeRequest,
                                          @MappingTarget AttributeType attributeType);
}
