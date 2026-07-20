package com.ecommerce.platform.security.oauth2.resourceserver;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

/**
 * Verifies typed access to Keycloak role claims.
 */
final class KeycloakAccessTest {

    @Test
    void absentAccessHasNoRoles() {
        assertThat(new KeycloakAccess(null).roles(), empty());
    }

    @Test
    void missingRolesAreEmpty() {
        assertThat(new KeycloakAccess(Map.of("tenant", "irregular-tenant")).roles(), empty());
    }

    @Test
    void blankRolesAreIgnored() {
        assertThat(
                new KeycloakAccess(Map.of("roles", List.of("auditor-83", " ", "buyer-29"))).roles(),
                contains("auditor-83", "buyer-29")
        );
    }

}
