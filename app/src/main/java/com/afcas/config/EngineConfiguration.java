package com.afcas.config;

import com.afcas.engine.AuthorizationEngine;
import com.afcas.engine.EngineMode;
import com.afcas.impl.AuthorizationProvider;
import com.afcas.objects.IAuthorizationProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for the authorization engine mode.
 * 
 * Allows switching between SQL stored procedures and Java application-layer logic
 * via the 'afcas.engine.mode' property.
 */
@Configuration
public class EngineConfiguration {

    @Value("${afcas.engine.mode:sql}")
    private String engineModeString;

    /**
     * Create the IAuthorizationProvider based on configuration.
     * 
     * @param javaEngine The application-layer authorization engine
     * @param sqlProvider The original SQL-based provider
     * @return The appropriate provider based on configuration
     */
    /**
     * Primary provider selected by {@code afcas.engine.mode}.
     * Bean name avoids clashing with the {@link AuthorizationProvider} @Service.
     */
    @Bean
    @Primary
    public IAuthorizationProvider configuredAuthorizationProvider(
            AuthorizationEngine javaEngine,
            AuthorizationProvider sqlProvider) {

        EngineMode mode = EngineMode.valueOf(engineModeString.toUpperCase());

        switch (mode) {
            case JAVA:
                return javaEngine;
            case SQL:
            default:
                return sqlProvider;
        }
    }
}
