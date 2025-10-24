package com.recceasy.idp.dto.user;

public class AdminCreateUserResponse {
    private final String userId;
    private final String username;
    private final UserRole role;
    private final String temporaryPassword;
    private final UserStatus status;

    public AdminCreateUserResponse(String userId, String username, UserRole role, String temporaryPassword, UserStatus status) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.temporaryPassword = temporaryPassword;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public UserRole getRole() {
        return role;
    }

    public String getTemporaryPassword() {
        return temporaryPassword;
    }

    public UserStatus getStatus() {
        return status;
    }
}
