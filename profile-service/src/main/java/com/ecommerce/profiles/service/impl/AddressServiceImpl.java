package com.ecommerce.profiles.service.impl;

import com.ecommerce.profiles.entity.Address;
import com.ecommerce.profiles.entity.Country;
import com.ecommerce.profiles.entity.Profile;
import com.ecommerce.profiles.exception.AddressLimitExceededException;
import com.ecommerce.profiles.exception.AddressNotFoundException;
import com.ecommerce.profiles.exception.CountryNotFoundException;
import com.ecommerce.profiles.exception.ProfileNotFoundException;
import com.ecommerce.profiles.mapper.AddressMapper;
import com.ecommerce.profiles.repository.AddressRepository;
import com.ecommerce.profiles.repository.CountryRepository;
import com.ecommerce.profiles.repository.ProfileRepository;
import com.ecommerce.profiles.rest.dto.AddressListResponse;
import com.ecommerce.profiles.rest.dto.AddressResponse;
import com.ecommerce.profiles.rest.dto.CreateAddressRequest;
import com.ecommerce.profiles.rest.dto.UpdateAddressRequest;
import com.ecommerce.profiles.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class AddressServiceImpl implements AddressService {
    private static final int ADDRESSES_LIMIT = 5;

    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;
    private final ProfileRepository profileRepository;
    private final CountryRepository countryRepository;

    @Override
    @Transactional
    public AddressResponse createProfileAddress(UUID profileId, CreateAddressRequest request) {
        Profile profile = getProfileForUpdate(profileId);
        long addressCount = addressRepository.countByProfileId(profileId);
        if (addressCount >= ADDRESSES_LIMIT) {
            throw new AddressLimitExceededException(profileId);
        }
        Country country = getCountry(request.getCountryCode());
        Address address = addressMapper.toEntity(request);
        address.setProfile(profile);
        address.setCountry(country);
        Optional<Address> currentPreferred = addressCount < 1
                ? Optional.empty()
                : addressRepository.findByProfileIdAndPreferredTrue(profileId);
        boolean shouldBePreferred = addressCount < 1
                || Boolean.TRUE.equals(request.getPreferred())
                || currentPreferred.isEmpty();
        if (shouldBePreferred) {
            demote(currentPreferred);
        }
        address.setPreferred(shouldBePreferred);
        Address savedAddress = addressRepository.saveAndFlush(address);
        return addressMapper.toResponse(savedAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getProfileAddress(UUID profileId, UUID addressId) {
        return addressMapper.toResponse(getOrThrow(profileId, addressId));
    }

    @Override
    @Transactional(readOnly = true)
    public AddressListResponse listProfileAddresses(UUID profileId) {
        if (!profileRepository.existsById(profileId)) {
            throw new ProfileNotFoundException(profileId);
        }
        List<Address> addresses = addressRepository.findAllByProfileIdWithDetails(profileId);
        return new AddressListResponse(addresses
                .stream()
                .map(addressMapper::toResponse)
                .toList());
    }

    @Override
    @Transactional
    public AddressResponse updateProfileAddress(UUID profileId, UUID addressId, UpdateAddressRequest request) {
        getProfileForUpdate(profileId);
        Address address = getOrThrow(profileId, addressId);
        Country country = getCountry(request.getCountryCode());
        addressMapper.update(address, request);
        address.setCountry(country);
        Optional<Address> currentPreferred = addressRepository.findByProfileIdAndPreferredTrue(profileId);
        if (Boolean.TRUE.equals(request.getPreferred())) {
            if (currentPreferred.filter(preferred -> !preferred.equals(address)).isPresent()) {
                demote(currentPreferred);
            }
            address.setPreferred(true);
        } else if (currentPreferred.isEmpty()) {
            address.setPreferred(true);
        } else {
            address.setPreferred(
                    currentPreferred
                            .filter(address::equals)
                            .isPresent()
            );
        }
        addressRepository.flush();
        return addressMapper.toResponse(address);
    }

    @Override
    @Transactional
    public void deleteProfileAddress(UUID profileId, UUID addressId) {
        getProfileForUpdate(profileId);
        Address address = getOrThrow(profileId, addressId);
        Optional<Address> replacement = address.isPreferred()
                ? addressRepository.findFirstByProfileIdAndIdNotOrderByCreatedAtAsc(profileId, addressId)
                : Optional.empty();
        addressRepository.delete(address);
        if (replacement.isPresent()) {
            addressRepository.flush();
            replacement.get().setPreferred(true);
        }
    }

    Profile getProfileForUpdate(UUID profileId) {
        return profileRepository.findByIdForUpdate(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(profileId));
    }

    Country getCountry(String countryCode) {
        return countryRepository.findByCode(countryCode)
                .orElseThrow(() -> new CountryNotFoundException(countryCode));
    }

    Address getOrThrow(UUID profileId, UUID addressId) {
        return addressRepository.findByIdAndProfileIdWithDetails(profileId, addressId)
                .orElseThrow(() -> new AddressNotFoundException(profileId, addressId));
    }

    void demote(Optional<Address> currentPreferred) {
        currentPreferred.ifPresent(address -> {
            address.setPreferred(false);
            addressRepository.flush();
        });
    }

}
