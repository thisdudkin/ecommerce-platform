package com.ecommerce.profiles.rest.controller;

import com.ecommerce.profiles.rest.api.AddressesApi;
import com.ecommerce.profiles.rest.dto.AddressListResponse;
import com.ecommerce.profiles.rest.dto.AddressResponse;
import com.ecommerce.profiles.rest.dto.CreateAddressRequest;
import com.ecommerce.profiles.rest.dto.UpdateAddressRequest;
import com.ecommerce.profiles.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AddressRestController implements AddressesApi {

    private final AddressService addressService;

    @Override
    public ResponseEntity<AddressResponse> createProfileAddress(UUID profileId, CreateAddressRequest createAddressRequest) {
        AddressResponse response = addressService.createProfileAddress(profileId, createAddressRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{addressId}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Override
    public ResponseEntity<Void> deleteProfileAddress(UUID profileId, UUID addressId) {
        addressService.deleteProfileAddress(profileId, addressId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<AddressResponse> getProfileAddress(UUID profileId, UUID addressId) {
        AddressResponse response = addressService.getProfileAddress(profileId, addressId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AddressListResponse> listProfileAddresses(UUID profileId) {
        AddressListResponse response = addressService.listProfileAddresses(profileId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AddressResponse> updateProfileAddress(UUID profileId, UUID addressId, UpdateAddressRequest updateAddressRequest) {
        AddressResponse response = addressService.updateProfileAddress(profileId, addressId, updateAddressRequest);
        return ResponseEntity.ok(response);
    }

}
