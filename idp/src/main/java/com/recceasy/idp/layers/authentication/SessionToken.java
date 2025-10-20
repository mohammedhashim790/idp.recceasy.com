package com.recceasy.idp.layers.authentication;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity
public class SessionToken {
    @Id
    private String token;
    private String userId;
    private String tenantId;
    private long expiresAt;
    private boolean revoked;

    public SessionToken() {
    }

    public SessionToken(String token, String userId, String tenantId, long expiresAt) {
        this.token = token;
        this.userId = userId;
        this.tenantId = tenantId;
        this.expiresAt = expiresAt;
        this.revoked = false;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public boolean isExpired() {
        return Instant.now().getEpochSecond() >= expiresAt;
    }
}
