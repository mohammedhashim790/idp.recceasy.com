package com.recceasy.idp.config;

import com.recceasy.idp.layers.authentication.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfiguration {

    @Value("${idp.jwt.secret:change-this-secret-in-config}")
    private String secret;

    @Value("${idp.jwt.ttlSeconds:3600}")
    private long ttlSeconds;

    @Bean
    public JwtService jwtService() {
        return new JwtService(secret, ttlSeconds);
    }
}
