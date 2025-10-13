package com.recceasy.idp.layers.userVerification;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

public class UserVerificationLinkBuilder {

    private final UriComponentsBuilder uri;

    public UserVerificationLinkBuilder() {
        this.uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/auth/verify");
    }


    public UserVerificationLinkBuilder withToken(String token) {
        this.uri.queryParam("token", token);
        return this;
    }

    public UserVerificationLinkBuilder withUsername(String username) {
        this.uri.queryParam("username", username);
        return this;
    }

    public UserVerificationLinkBuilder withEmail(String email) {
        this.uri.queryParam("email", email);
        return this;
    }

    public String build() {
        return this.uri.build().toUriString();
    }
}
