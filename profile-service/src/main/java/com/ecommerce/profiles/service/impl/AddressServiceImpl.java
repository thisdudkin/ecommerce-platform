package com.ecommerce.profiles.service.impl;

import com.ecommerce.profiles.repository.AddressRepository;
import com.ecommerce.profiles.rest.dto.AddressListResponse;
import com.ecommerce.profiles.rest.dto.AddressResponse;
import com.ecommerce.profiles.rest.dto.CreateAddressRequest;
import com.ecommerce.profiles.rest.dto.UpdateAddressRequest;
import com.ecommerce.profiles.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public AddressResponse createProfileAddress(UUID profileId, CreateAddressRequest request) {
        return null;
    }

    @Override
    public AddressResponse getProfileAddress(UUID profileId, UUID addressId) {
        return null;
    }

    @Override
    public AddressListResponse listProfileAddresses(UUID profileId) {
        return null;
    }

    @Override
    public AddressResponse updateProfileAddress(UUID profileId, UUID addressId, UpdateAddressRequest request) {
        return null;
    }

    @Override
    public void deleteProfileAddress(UUID profileId, UUID addressId) {

    }

}
