package io.spring.jbuy.features.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "expiry", ignore = true)
    UserResponse toUserResponse(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "listOfAuthority", ignore = true)
    @Mapping(target = "listOfReview", ignore = true)
    @Mapping(target = "listOfOrder", ignore = true)
    @Mapping(target = "listOfCartItem", ignore = true)
    @Mapping(target = "listOfAddress", ignore = true)
    User toUser(UserRequest userRequest);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "listOfAuthority", ignore = true)
    @Mapping(target = "listOfReview", ignore = true)
    @Mapping(target = "listOfOrder", ignore = true)
    @Mapping(target = "listOfCartItem", ignore = true)
    @Mapping(target = "listOfAddress", ignore = true)
    User toExistingUser(UserRequest userRequest, @MappingTarget User user);
}
