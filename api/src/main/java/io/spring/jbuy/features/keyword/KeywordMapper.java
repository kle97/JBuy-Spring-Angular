package io.spring.jbuy.features.keyword;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface KeywordMapper {

    KeywordMapper INSTANCE = Mappers.getMapper(KeywordMapper.class);

    KeywordResponse toKeywordResponse(Keyword keyword);

    Keyword toKeyword(KeywordRequest keywordRequest);

    Keyword toExistingKeyword(KeywordRequest keywordRequest, @MappingTarget Keyword keyword);
}
