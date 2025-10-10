package com.recceasy.idp.layers.user;


import com.recceasy.idp.dto.user.UserRole;
import com.recceasy.idp.dto.user.UserStatus;
import jakarta.persistence.*;
import org.springframework.format.annotation.NumberFormat;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true, nullable = false)
    private String username;

    private boolean enabled;

    @Column(nullable = true)
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private String phone;

    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    // Simple tenant mapping: store tenantId string matching Tenant.tenantId
    @Column(nullable = true)
    private String tenantId;

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public User(String username, UserRole role) {
        this.username = username;
        this.role = role;
    }

    public User(String username) {
        this.username = username;
    }


    public User() {

    }

    @PrePersist
    protected void onCreate() {
        this.enabled = false;
        this.setUserStatus(UserStatus.UNCONFIRMED);
        if (this.role == null) this.role = UserRole.EXTERNAL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
}
