package io.spring.jbuy.features.address;

import com.fasterxml.jackson.annotation.JsonView;
import io.spring.jbuy.common.validator.ValidationGroup;
import io.spring.jbuy.common.view.BaseView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
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

@RestController @RequestMapping("/api/v1/addresses")
@Tag(name = "address", description = "address API")
@Validated
@RequiredArgsConstructor @Slf4j
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/{addressId}")
    @Operation(summary = "Find address by id", tags = "address", security = {@SecurityRequirement(name = "httpBasic")})
    @PostAuthorize("hasRole(@Role.ADMIN) or @webSecurity.hasUserId(returnObject.body.userId)")
    public ResponseEntity<AddressResponse> getAddress(@PathVariable UUID addressId) {
        return ResponseEntity.ok().body(addressService.getAddressResponseById(addressId));
    }

    @GetMapping("/{userId}/page")
    @Operation(summary = "Find all instances of address of a user as pages", tags = "address",
            security = {@SecurityRequirement(name = "httpBasic")})
    @PreAuthorize("hasRole(@Role.ADMIN) or @webSecurity.hasUserId(#userId)")
    public ResponseEntity<Page<AddressResponse>> getAddressByUserIdPageable(@PathVariable UUID userId,
                                                                            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok().body(addressService.getAddressResponsePageableByUserId(userId, pageable));
    }

    @PostMapping("")
    @Operation(summary = "Add a new address", tags = "address", security = {@SecurityRequirement(name = "httpBasic")})
    @PreAuthorize("hasRole(@Role.ADMIN) or @webSecurity.hasUserId(#addressRequest.userId)")
    public ResponseEntity<AddressResponse> createAddress(@RequestBody
                                                         @Validated({ValidationGroup.onCreate.class, Default.class})
                                                         @JsonView({BaseView.Create.class})
                                                                 AddressRequest addressRequest) {
        AddressResponse response = addressService.createAddress(addressRequest);
        String addressId = String.valueOf(response.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{addressId}")
                .buildAndExpand(addressId).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{addressId}")
    @Operation(summary = "Update an address by id", tags = "address", security = {@SecurityRequirement(name = "httpBasic")})
    @PreAuthorize("hasRole(@Role.ADMIN) or @webSecurity.hasUserId(#addressRequest.userId)")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable UUID addressId,
                                                         @RequestBody @Valid @JsonView({BaseView.Update.class})
                                                                 AddressRequest addressRequest) {
        AddressResponse response = addressService.updateAddress(addressId, addressRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "Delete an address by id", tags = "address", security = {@SecurityRequirement(name = "httpBasic")})
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID addressId) {
        addressService.deleteById(addressId);
        return ResponseEntity.noContent().build();
    }
}
