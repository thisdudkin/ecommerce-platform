package com.ecommerce.platform.security.oauth2.resourceserver;

import org.jspecify.annotations.NonNull;
import org.springframework.security.oauth2.core.ClaimAccessor;

import java.util.List;
import java.util.Map;

/**
 * Provides typed access to a nested Keycloak access claim.
 */
record KeycloakAccess(Map<String, Object> claims) implements ClaimAccessor {
    private static final String ROLES = "roles";

    /**
     * Normalizes an absent access claim.
     */
    KeycloakAccess {
        claims = claims == null ? Map.of() : Map.copyOf(claims);
    }

    /**
     * Returns the underlying claim values.
     */
    @NonNull
    @Override
    public Map<String, Object> getClaims() {
        return claims;
    }

    /**
     * Returns non-blank roles from the access claim.
     */
    List<String> roles() {
        List<String> roles = getClaimAsStringList(ROLES);
        if (roles == null) {
            return List.of();
        }
        return roles.stream()
                .filter(role -> !role.isBlank())
                .toList();
    }

}
