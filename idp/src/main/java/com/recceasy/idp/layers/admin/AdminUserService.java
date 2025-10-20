package com.recceasy.idp.layers.admin;

import com.recceasy.idp.dto.user.UserRole;
import com.recceasy.idp.dto.user.UserStatus;
import com.recceasy.idp.layers.password.PasswordService;
import com.recceasy.idp.layers.tenant.TenantRepository;
import com.recceasy.idp.layers.user.User;
import com.recceasy.idp.layers.user.UserRepository;
import com.recceasy.idp.utils.TemporaryPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AdminUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private TenantRepository tenantRepository;

    @Transactional
    public com.recceasy.idp.dto.user.AdminCreateUserResponse createUser(String username, String phone, UserRole role, String tenantId) {
        Optional<User> existing = userRepository.findUserByUsername(username);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        if (tenantId == null || tenantId.isBlank() || tenantRepository.findTenantByTenantId(tenantId).isEmpty()) {
            throw new IllegalArgumentException("Invalid tenant id");
        }
        User user = new User(username);
        user.setPhone(phone);
        user.setEnabled(false);
        user.setUserStatus(UserStatus.RESET_REQUIRED);
        user.setRole(role);
        user.setTenantId(tenantId);
        user = userRepository.save(user);
        String temp = TemporaryPassword.generateTemporaryPassword();
        passwordService.create(user.getId(), temp);
        return new com.recceasy.idp.dto.user.AdminCreateUserResponse(user.getId(), user.getUsername(), role, temp, UserStatus.RESET_REQUIRED);
    }

    @Transactional
    public void forceReset(String userId) {
        User u = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        String temp = TemporaryPassword.generateTemporaryPassword();
        passwordService.update(u.getId(), temp);
        u.setEnabled(false);
        u.setUserStatus(UserStatus.RESET_REQUIRED);
        userRepository.save(u);
    }

    @Transactional
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
