package com.ecommerce.platform.security.oauth2.resourceserver;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

/**
 * Holds the Keycloak resource server configuration.
 */
@ConfigurationProperties(ResourceServerProperties.PREFIX)
record ResourceServerProperties(String resourceId) {
    static final String PREFIX = "ecommerce.security.oauth2.resource-server";

    /**
     * Validates the required resource identifier.
     */
    ResourceServerProperties {
        Assert.hasText(resourceId, PREFIX + ".resource-id must not be blank");
    }
}
