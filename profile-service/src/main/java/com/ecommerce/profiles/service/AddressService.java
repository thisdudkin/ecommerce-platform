package com.ecommerce.profiles.service;

import com.ecommerce.profiles.rest.dto.AddressListResponse;
import com.ecommerce.profiles.rest.dto.AddressResponse;
import com.ecommerce.profiles.rest.dto.CreateAddressRequest;
import com.ecommerce.profiles.rest.dto.UpdateAddressRequest;

import java.util.UUID;

public interface AddressService {

    AddressResponse createProfileAddress(UUID profileId, CreateAddressRequest request);

    AddressResponse getProfileAddress(UUID profileId, UUID addressId);

    AddressListResponse listProfileAddresses(UUID profileId);

    AddressResponse updateProfileAddress(UUID profileId, UUID addressId, UpdateAddressRequest request);

    void deleteProfileAddress(UUID profileId, UUID addressId);

}
