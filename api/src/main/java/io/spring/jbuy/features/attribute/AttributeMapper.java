package io.spring.jbuy.features.attribute;

import io.spring.jbuy.common.mapper.ReferenceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ReferenceMapper.class})
public interface AttributeMapper {

    AttributeMapper INSTANCE = Mappers.getMapper(AttributeMapper.class);

    @Mapping(target = "attributeTypeId", source = "attributeType.id")
    @Mapping(target = "attributeType", source = "attributeType.name")
    AttributeResponse toAttributeResponse(Attribute attribute);

    @Mapping(target = "attributeType", source = "attributeTypeId")
    @Mapping(target = "listOfProductAttribute", ignore = true)
    Attribute toAttribute(AttributeRequest attributeRequest);

    @Mapping(target = "attributeType", ignore = true)
    @Mapping(target = "listOfProductAttribute", ignore = true)
    Attribute toExistingAttribute(AttributeRequest attributeRequest, @MappingTarget Attribute attribute);
}
