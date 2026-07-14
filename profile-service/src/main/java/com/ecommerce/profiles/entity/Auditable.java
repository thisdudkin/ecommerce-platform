package com.ecommerce.profiles.entity;

import java.time.Instant;

public interface Auditable {
    Instant getCreatedAt();

    Instant getUpdatedAt();
}
