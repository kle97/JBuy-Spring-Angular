package io.spring.jbuy.features.address;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.validator.ValidationGroup;
import io.spring.jbuy.common.view.BaseView;
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

@RestController @RequestMapping("/api/v1/admin/addresses")
@Tag(name = "address-admin", description = "address API for admin")
@Validated @PreAuthorize("hasRole(@Role.ADMIN)")
@RequiredArgsConstructor @Slf4j
public class AddressAdminController {

    private final AddressService addressService;

    @GetMapping("/{addressId}")
    @Operation(summary = "Find address by id", tags = "address-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<AddressResponse> getAddress(@PathVariable UUID addressId) {
        return ResponseEntity.ok().body(addressService.getAddressResponseById(addressId));
    }

    @GetMapping("")
    @Operation(summary = "Find all instances of address as pages", tags = "address-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<Page<AddressResponse>> getAddressPageable(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(addressService.getAddressResponsePageable(pageable));
    }

    @PostMapping("")
    @Operation(summary = "Add a new address", tags = "address-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<AddressResponse> createAddress(@RequestBody
                                                         @Validated({ValidationGroup.onCreate.class, Default.class})
                                                         @JsonView(BaseView.Create.class)
                                                                 AddressRequest addressRequest) {
        AddressResponse response = addressService.createAddress(addressRequest);
        String addressId = String.valueOf(response.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{addressId}")
                .buildAndExpand(addressId).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{addressId}")
    @Operation(summary = "Update an address by id", tags = "address-admin")
    @JsonView(BaseView.Admin.class)
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable UUID addressId,
                                                         @RequestBody @Valid @JsonView(BaseView.Update.class)
                                                                 AddressRequest addressRequest) {
        AddressResponse response = addressService.updateAddress(addressId, addressRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "Delete an address by id", tags = "address-admin")
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID addressId) {
        addressService.deleteById(addressId);
        return ResponseEntity.noContent().build();
    }
}
