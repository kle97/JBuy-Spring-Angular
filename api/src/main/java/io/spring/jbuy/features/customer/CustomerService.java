package io.spring.jbuy.features.customer;

import io.spring.jbuy.common.exception.ResourceNotFoundException;
import io.spring.jbuy.features.user.UserRequest;
import io.spring.jbuy.features.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor @Slf4j
public class CustomerService {

    private final UserService userService;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional(readOnly = true)
    public Customer getCustomerById(UUID customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(Customer.class, customerId));
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerResponseById(UUID customerId) {
        return customerMapper.toCustomerResponse(getCustomerById(customerId));
    }

    @Transactional(readOnly = true)
    public Page<CustomerResponse> getCustomerResponsePageable(Pageable pageable) {
        return customerRepository.findAll(pageable).map(customerMapper::toCustomerResponse);
    }

    @Transactional
    public CustomerResponse createCustomer(UserRequest userRequest) {
        Customer transientCustomer = new Customer(this.userService.createInternalUser(userRequest, true));
        return customerMapper.toCustomerResponse(customerRepository.save(transientCustomer));
    }

    @Transactional
    public CustomerResponse updateCustomer(UUID customerId, CustomerRequest customerRequest) {
        Customer currentCustomer = getCustomerById(customerId);
        return customerMapper.toCustomerResponse(customerMapper.toExistingCustomer(customerRequest, currentCustomer));
    }

    @Transactional
    public void deleteById(UUID customerId) {
        if (customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
        } else {
            throw new ResourceNotFoundException(Customer.class, customerId);
        }
    }
}
