package com.ecommerce.profiles.exception;

import jakarta.persistence.EntityNotFoundException;

public class CountryNotFoundException extends EntityNotFoundException {
    public CountryNotFoundException(String code) {
        super("Country not found: %s".formatted(code));
    }
}
