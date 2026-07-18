package com.ecommerce.profiles.exception;

import java.util.UUID;

public class AddressLimitExceededException extends RuntimeException {
    public AddressLimitExceededException(UUID profileId) {
        super("Address limit exceeded for the profile: %s".formatted(profileId));
    }
}
