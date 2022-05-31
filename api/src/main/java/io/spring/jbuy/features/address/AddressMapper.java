package io.spring.jbuy.features.address;

import io.spring.jbuy.common.mapper.ReferenceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ReferenceMapper.class})
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    @Mapping(target = "userId", source = "user.id")
    AddressResponse toAddressResponse(Address address);

    @Mapping(target = "user", source = "userId")
    Address toAddress(AddressRequest addressRequest);

    @Mapping(target = "user", ignore = true)
    Address toExistingAddress(AddressRequest addressRequest, @MappingTarget Address address);
}
