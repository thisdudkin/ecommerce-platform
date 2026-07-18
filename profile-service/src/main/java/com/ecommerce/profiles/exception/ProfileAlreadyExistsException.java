package com.ecommerce.profiles.exception;

import java.util.UUID;

public class ProfileAlreadyExistsException extends RuntimeException {
    public ProfileAlreadyExistsException(UUID userId) {
        super("Profile already exists for user: %s".formatted(userId));
    }

    public ProfileAlreadyExistsException(UUID userId, Throwable cause) {
        super("Profile already exists for user: %s".formatted(userId), cause);
    }
}
