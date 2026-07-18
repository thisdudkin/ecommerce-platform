package com.ecommerce.profiles.exception;

import jakarta.persistence.EntityNotFoundException;

import java.util.UUID;

public class ProfileNotFoundException extends EntityNotFoundException {
    public ProfileNotFoundException(UUID profileId) {
        super("Profile not found: %s".formatted(profileId));
    }
}
