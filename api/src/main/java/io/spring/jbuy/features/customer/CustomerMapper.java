package io.spring.jbuy.features.customer;

import io.spring.jbuy.common.mapper.ReferenceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ReferenceMapper.class})
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    CustomerResponse toCustomerResponse(Customer customer);

    @Mapping(target = "user", source = "userId")
    Customer toCustomer(CustomerRequest customerRequest);

    @Mapping(target = "user", ignore = true)
    Customer toExistingCustomer(CustomerRequest customerRequest, @MappingTarget Customer customer);
}
