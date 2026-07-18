package com.ecommerce.profiles.exception;

import jakarta.persistence.EntityNotFoundException;

import java.util.UUID;

public class AddressNotFoundException extends EntityNotFoundException {
    public AddressNotFoundException(UUID profileId, UUID addressId) {
        super("Profile '%s' does not have an address '%s'".formatted(profileId, addressId));
    }
}
