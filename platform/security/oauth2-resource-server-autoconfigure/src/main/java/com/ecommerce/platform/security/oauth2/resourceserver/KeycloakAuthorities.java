package com.ecommerce.platform.security.oauth2.resourceserver;

import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Converts standard Keycloak claims to Spring Security authorities.
 */
final class KeycloakAuthorities implements Converter<Jwt, Collection<GrantedAuthority>> {
    private static final String REALM_ACCESS = "realm_access";
    private static final String RESOURCE_ACCESS = "resource_access";
    private static final String REALM_PREFIX = "ROLE_REALM_";
    private static final String CLIENT_PREFIX = "ROLE_CLIENT_";

    private final JwtGrantedAuthoritiesConverter scopes = new JwtGrantedAuthoritiesConverter();

    private final String resourceId;

    /**
     * Creates the converter for a Keycloak resource.
     */
    KeycloakAuthorities(String resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * Combines scopes, realm roles and client roles.
     */
    @Override
    public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        authorities.addAll(scopes(jwt));
        authorities.addAll(realm(jwt));
        authorities.addAll(client(jwt));
        return List.copyOf(authorities);
    }

    /**
     * Returns standard OAuth scope authorities.
     */
    private Collection<GrantedAuthority> scopes(Jwt jwt) {
        return scopes.convert(jwt);
    }

    /**
     * Returns authorities from Keycloak realm roles.
     */
    private Collection<GrantedAuthority> realm(Jwt jwt) {
        var access = new KeycloakAccess(jwt.getClaimAsMap(REALM_ACCESS));
        return authorities(access.roles(), REALM_PREFIX);
    }

    /**
     * Returns authorities for the configured Keycloak client.
     */
    private Collection<GrantedAuthority> client(Jwt jwt) {
        var resources = new KeycloakAccess(jwt.getClaimAsMap(RESOURCE_ACCESS));
        var client = new KeycloakAccess(resources.getClaimAsMap(resourceId));
        String prefix = CLIENT_PREFIX + resourceId + "_";
        return authorities(client.roles(), prefix);
    }

    /**
     * Adds the given prefix to role names.
     */
    private Collection<GrantedAuthority> authorities(List<String> roles, String prefix) {
        return roles.stream()
                .<GrantedAuthority>map(role -> new SimpleGrantedAuthority(prefix + role))
                .toList();
    }
}
