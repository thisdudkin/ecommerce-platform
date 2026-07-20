package com.ecommerce.platform.security.oauth2.resourceserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

/**
 * Verifies servlet security auto-configuration and its extension boundary.
 */
final class ResourceServerWebAutoConfigurationTest {

    @Test
    void servletApplicationReceivesOneSecurityFilterChain() {
        new WebApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(
                        ResourceServerAutoConfiguration.class,
                        ResourceServerWebAutoConfiguration.class
                ))
                .withPropertyValues("ecommerce.security.oauth2.resource-server.resource-id=warehouse-19")
                .withBean(JwtDecoder.class, () -> token -> Jwt.withTokenValue(token)
                        .header("alg", "none")
                        .subject("employee-29")
                        .issuedAt(Instant.parse("2037-04-11T10:15:30Z"))
                        .expiresAt(Instant.parse("2037-04-11T10:20:30Z"))
                        .build())
                .run(context -> assertThat(
                        context.getBeansOfType(SecurityFilterChain.class).size(),
                        is(1)
                ));
    }

    @Test
    void consumerFilterChainReplacesTheDefault() {
        SecurityFilterChain chain = new DefaultSecurityFilterChain(request -> true);
        new WebApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(
                        ResourceServerAutoConfiguration.class,
                        ResourceServerWebAutoConfiguration.class
                ))
                .withPropertyValues("ecommerce.security.oauth2.resource-server.resource-id=warehouse-31")
                .withBean(SecurityFilterChain.class, () -> chain)
                .run(context -> assertThat(
                        context.getBean(SecurityFilterChain.class),
                        sameInstance(chain)
                ));
    }

    @Test
    void nonWebApplicationDoesNotReceiveFilterChain() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(
                        ResourceServerAutoConfiguration.class,
                        ResourceServerWebAutoConfiguration.class
                ))
                .withPropertyValues("ecommerce.security.oauth2.resource-server.resource-id=warehouse-43")
                .run(context -> assertThat(
                        context.getBeansOfType(SecurityFilterChain.class).size(),
                        is(0)
                ));
    }

}
