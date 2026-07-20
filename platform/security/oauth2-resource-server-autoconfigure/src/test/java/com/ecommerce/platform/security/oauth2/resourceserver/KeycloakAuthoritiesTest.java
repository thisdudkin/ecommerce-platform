package com.ecommerce.platform.security.oauth2.resourceserver;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;

/**
 * Verifies conversion of Keycloak claims into Spring Security authorities.
 */
final class KeycloakAuthoritiesTest {

    @Test
    void scopesRealmRolesAndClientRolesAreCombined() {
        Collection<GrantedAuthority> authorities = new KeycloakAuthorities("catalog-47").convert(
                Jwt.withTokenValue("token-89")
                        .header("alg", "none")
                        .claim("scope", "inventory.read inventory.write")
                        .claim("realm_access", Map.of("roles", List.of("operator-31")))
                        .claim("resource_access", Map.of(
                                "catalog-47", Map.of("roles", List.of("editor-53"))
                        ))
                        .build()
        );
        assertThat(
                authorities.stream().map(GrantedAuthority::getAuthority).toList(),
                containsInAnyOrder(
                        "SCOPE_inventory.read",
                        "SCOPE_inventory.write",
                        "ROLE_REALM_operator-31",
                        "ROLE_CLIENT_catalog-47_editor-53"
                )
        );
    }

    @Test
    void rolesForOtherClientsAreIgnored() {
        Collection<GrantedAuthority> authorities = new KeycloakAuthorities("profile-61").convert(
                Jwt.withTokenValue("token-71")
                        .header("alg", "none")
                        .claim("resource_access", Map.of(
                                "profile-61", Map.of("roles", List.of("reader-41")),
                                "orders-67", Map.of("roles", List.of("owner-43"))
                        ))
                        .build()
        );
        assertThat(
                authorities.stream().map(GrantedAuthority::getAuthority).toList(),
                contains("ROLE_CLIENT_profile-61_reader-41")
        );
    }

    @Test
    void duplicateRolesCreateOneAuthority() {
        Collection<GrantedAuthority> authorities = new KeycloakAuthorities("checkout-59").convert(
                Jwt.withTokenValue("token-73")
                        .header("alg", "none")
                        .claim("realm_access", Map.of("roles", List.of("member-79", "member-79")))
                        .build()
        );
        assertThat(
                authorities.stream().map(GrantedAuthority::getAuthority).toList(),
                contains("ROLE_REALM_member-79")
        );
    }

}
