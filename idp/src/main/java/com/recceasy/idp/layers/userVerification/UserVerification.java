package com.recceasy.idp.layers.userVerification;

import com.recceasy.idp.utils.RecceasyTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;


@Entity
public class UserVerification {


    private static final int EXPIRES_AT_MINUTES = 15;

    @Id
    private String username;

    @Column
    private String token;

    @Column
    private Long createdTime;

    @Column
    private Long expiresAt;

    @PrePersist
    public void onCreate() {
        this.createdTime = RecceasyTime.now();
        this.expiresAt = RecceasyTime.addMinutes(EXPIRES_AT_MINUTES);
    }


    public UserVerification(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public UserVerification() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }
}
