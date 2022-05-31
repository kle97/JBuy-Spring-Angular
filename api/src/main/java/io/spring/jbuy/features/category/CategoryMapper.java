package io.spring.jbuy.features.category;

import io.spring.jbuy.common.mapper.ReferenceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ReferenceMapper.class})
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryResponse toCategoryResponse(Category category);

    @Mapping(target = "listOfAttributeType", ignore = true)
    @Mapping(target = "listOfProduct", ignore = true)
    Category toCategory(CategoryRequest categoryRequest);

    @Mapping(target = "listOfAttributeType", ignore = true)
    @Mapping(target = "listOfProduct", ignore = true)
    Category toExistingCategory(CategoryRequest categoryRequest, @MappingTarget Category category);
}
