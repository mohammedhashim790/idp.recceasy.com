package com.recceasy.idp.dto.user;

public class AdminCreateUserRequest {
    private String username; // email or phone based identifier
    private String phone;    // optional if username is email
    private UserRole role;   // ADMIN/INTERNAL/EXTERNAL (SUPER_USER via /auth/register)

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
}
