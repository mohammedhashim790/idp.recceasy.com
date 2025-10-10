package com.recceasy.idp.dto.auth;

public class LoginResponse {
    private String jwt;
    private String sessionToken;

    public LoginResponse(String jwt, String sessionToken) {
        this.jwt = jwt;
        this.sessionToken = sessionToken;
    }

    public String getJwt() { return jwt; }
    public String getSessionToken() { return sessionToken; }
}
