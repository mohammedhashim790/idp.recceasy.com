package com.recceasy.idp.dto.auth;

public class LoginResponse {
    private final String jwt;
    private final String sessionToken;

    public LoginResponse(String jwt, String sessionToken) {
        this.jwt = jwt;
        this.sessionToken = sessionToken;
    }

    public String getJwt() {
        return jwt;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}
