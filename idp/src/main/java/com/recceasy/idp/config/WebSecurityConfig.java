package com.recceasy.idp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final AuthContextFilter authContextFilter;

    public WebSecurityConfig(AuthContextFilter authContextFilter) {
        this.authContextFilter = authContextFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll())
                .csrf().disable()
                .addFilterBefore(authContextFilter, UsernamePasswordAuthenticationFilter.class).cors().disable();
        return http.build();
    }
}
