package com.ecommerce.platform.security.oauth2.resourceserver;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Verifies the required resource server configuration.
 */
final class ResourceServerPropertiesTest {

    @Test
    void resourceIdentifierIsPreserved() {
        assertThat(new ResourceServerProperties("billing-97").resourceId(), is("billing-97"));
    }

    @Test
    void blankResourceIdentifierIsRejected() {
        assertThat(
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new ResourceServerProperties(" ")
                ).getMessage(),
                containsString("resource-id must not be blank")
        );
    }

    @Test
    void absentResourceIdentifierIsRejected() {
        assertThat(
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new ResourceServerProperties(null)
                ).getMessage(),
                containsString("resource-id must not be blank")
        );
    }

}
