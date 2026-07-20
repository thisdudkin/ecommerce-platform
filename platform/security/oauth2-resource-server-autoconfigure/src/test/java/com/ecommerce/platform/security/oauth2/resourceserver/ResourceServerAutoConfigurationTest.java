package com.ecommerce.platform.security.oauth2.resourceserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;

/**
 * Verifies resource server auto-configuration and its extension boundary.
 */
final class ResourceServerAutoConfigurationTest {

    @Test
    void configuredResourceIdentifierSelectsClientRoles() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(ResourceServerAutoConfiguration.class))
                .withPropertyValues("ecommerce.security.oauth2.resource-server.resource-id=payments-23")
                .run(context -> assertThat(
                        context.getBean(JwtAuthenticationConverter.class)
                                .convert(Jwt.withTokenValue("token-37")
                                        .header("alg", "none")
                                        .claim("resource_access", Map.of(
                                                "payments-23", Map.of("roles", List.of("approver-11"))
                                        ))
                                        .build())
                                .getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .toList(),
                        hasItem("ROLE_CLIENT_payments-23_approver-11")
                ));
    }

    @Test
    void consumerConverterReplacesTheDefault() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(ResourceServerAutoConfiguration.class))
                .withPropertyValues("ecommerce.security.oauth2.resource-server.resource-id=shipping-13")
                .withBean(JwtAuthenticationConverter.class, () -> converter)
                .run(context -> assertThat(
                        context.getBean(JwtAuthenticationConverter.class),
                        sameInstance(converter)
                ));
    }

    @Test
    void missingResourceIdentifierFailsImmediately() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(ResourceServerAutoConfiguration.class))
                .run(context -> assertThat(context.getStartupFailure(), is(notNullValue())));
    }

    @Test
    void missingSecurityClassesDisableTheConfiguration() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(ResourceServerAutoConfiguration.class))
                .withClassLoader(new FilteredClassLoader(JwtDecoder.class))
                .withPropertyValues("ecommerce.security.oauth2.resource-server.resource-id=search-17")
                .run(context -> assertThat(
                        context.getBeansOfType(JwtAuthenticationConverter.class).size(),
                        is(0)
                ));
    }

    @Test
    void bothAutoConfigurationsAreRegistered() {
        assertThat(
                ImportCandidates.load(AutoConfiguration.class, getClass().getClassLoader()).getCandidates(),
                hasItems(
                        ResourceServerAutoConfiguration.class.getName(),
                        ResourceServerWebAutoConfiguration.class.getName()
                )
        );
    }

}
