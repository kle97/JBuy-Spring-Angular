package io.spring.jbuy.features.authority;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthorityMapper {

    AuthorityMapper INSTANCE = Mappers.getMapper(AuthorityMapper.class);

    AuthorityResponse toAuthorityResponse(Authority authority);

    @Mapping(target = "listOfUser", ignore = true)
    Authority toAuthority(AuthorityRequest authorityRequest);

    @Mapping(target = "listOfUser", ignore = true)
    Authority toExistingAuthority(AuthorityRequest authorityRequest, @MappingTarget Authority authority);
}
