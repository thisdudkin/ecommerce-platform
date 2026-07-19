package com.ecommerce.platform.security.oauth2.resourceserver;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

/**
 * Configures JWT authentication for standard Keycloak tokens.
 */
@AutoConfiguration
@ConditionalOnClass({
        JwtDecoder.class,
        JwtAuthenticationConverter.class
})
@EnableConfigurationProperties(ResourceServerProperties.class)
public class ResourceServerAutoConfiguration {

    /**
     * Creates the default Keycloak JWT authentication converter.
     */
    @Bean
    @ConditionalOnMissingBean(JwtAuthenticationConverter.class)
    JwtAuthenticationConverter jwtAuthenticationConverter(ResourceServerProperties properties) {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(
                new KeycloakAuthorities(properties.resourceId())
        );
        return converter;
    }

}
