package io.spring.jbuy.features.address;

import io.spring.jbuy.common.exception.ResourceNotFoundException;
import io.spring.jbuy.features.authentication.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j @RequiredArgsConstructor
public class AddressService {

    private final CustomUserDetailsService customUserDetailsService;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Transactional(readOnly = true)
    public Address getAddressById(UUID addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException(Address.class, addressId));
    }

    @Transactional(readOnly = true)
    public AddressResponse getAddressResponseById(UUID addressId) {
        return addressMapper.toAddressResponse(getAddressById(addressId));
    }

    @Transactional(readOnly = true)
    public Page<AddressResponse> getAddressResponsePageable(Pageable pageable) {
        return addressRepository.findAll(pageable).map(addressMapper::toAddressResponse);
    }

    @Transactional(readOnly = true)
    public Page<AddressResponse> getAddressResponsePageableByUserId(UUID userId, Pageable pageable) {
        return addressRepository.findAllByUser_Id(userId, pageable).map(addressMapper::toAddressResponse);
    }

    @Transactional
    public AddressResponse createAddress(AddressRequest addressRequest) {
        this.validateAddressRequest(addressRequest);
        if (addressRequest.getPrimaryAddress()) {
            List<Address> listOfAddress = addressRepository.findAllByUser_Id(addressRequest.getUserId());
            for(Address address: listOfAddress) {
                address.setPrimaryAddress(false);
            }
        }
        Address transientAddress = addressMapper.toAddress(addressRequest);
        return addressMapper.toAddressResponse(addressRepository.save(transientAddress));
    }

    @Transactional
    public AddressResponse updateAddress(UUID addressId, AddressRequest addressRequest) {
        Address currentAddress = getAddressById(addressId);
        if (addressRequest.getPrimaryAddress()) {
            List<Address> listOfAddress = addressRepository.findAllByUser_Id(addressRequest.getUserId());
            for(Address address: listOfAddress) {
                address.setPrimaryAddress(false);
            }
        }
        return addressMapper.toAddressResponse(addressMapper.toExistingAddress(addressRequest, currentAddress));
    }

    @Transactional
    public void deleteById(UUID addressId) {
        Address currentAddress = this.getAddressById(addressId);
        if (customUserDetailsService.hasUserId(currentAddress.getUser().getId())
                || customUserDetailsService.isAdmin()) {
            addressRepository.deleteById(addressId);
        } else {
            throw new AuthenticationServiceException("Unauthorized request!");
        }
    }

    @Transactional(readOnly = true)
    public void validateAddressRequest(AddressRequest addressRequest) {
        if (addressRepository.countAllByUser_Id(addressRequest.getUserId()) == 20) {
            throw new ValidationException("Addresses' limit is 20!");
        }
    }
}
