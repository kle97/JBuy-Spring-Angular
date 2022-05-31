package io.spring.jbuy.features.customer;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.validator.ValidationGroup;
import io.spring.jbuy.common.view.BaseView;
import io.spring.jbuy.features.user.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.groups.Default;
import java.net.URI;
import java.util.UUID;

@RestController @RequestMapping("/api/v1/admin/customers")
@Tag(name = "customer-admin", description = "customer API for admin")
@Validated @PreAuthorize("hasRole(@Role.ADMIN)")
@RequiredArgsConstructor @Slf4j
public class CustomerAdminController {

    private final CustomerService customerService;

    @GetMapping("/{customerId}")
    @Operation(summary = "Find customer by id", tags = "customer-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable UUID customerId) {
        return ResponseEntity.ok().body(customerService.getCustomerResponseById(customerId));
    }

    @GetMapping("")
    @Operation(summary = "Find all instances of customer as pages", tags = "customer-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<Page<CustomerResponse>> getCustomerPageable(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(customerService.getCustomerResponsePageable(pageable));
    }

    @PostMapping("")
    @Operation(summary = "Add a new customer", tags = "customer-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody
                                                           @Validated({ValidationGroup.onCreate.class, Default.class})
                                                           @JsonView(BaseView.Create.class)
                                                                   UserRequest userRequest) {
        CustomerResponse response = customerService.createCustomer(userRequest);
        String customerId = String.valueOf(response.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{customerId}")
                .buildAndExpand(customerId).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{customerId}")
    @Operation(summary = "Update an customer by id", tags = "customer-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable UUID customerId,
                                                           @RequestBody @Valid @JsonView(BaseView.Update.class)
                                                                   CustomerRequest customerRequest) {
        CustomerResponse response = customerService.updateCustomer(customerId, customerRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{customerId}")
    @Operation(summary = "Delete an customer by id", tags = "customer-admin")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID customerId) {
        customerService.deleteById(customerId);
        return ResponseEntity.noContent().build();
    }
}
