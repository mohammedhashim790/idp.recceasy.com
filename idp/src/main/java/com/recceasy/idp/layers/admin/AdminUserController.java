package com.recceasy.idp.layers.admin;

import com.recceasy.idp.dto.user.AdminCreateUserRequest;
import com.recceasy.idp.dto.user.AdminCreateUserResponse;
import com.recceasy.idp.dto.user.UserRole;
import com.recceasy.idp.layers.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private CurrentUser currentUser;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AdminCreateUserRequest req) {
        UserRole role = req.getRole();
        if (role == null) {
            role = UserRole.EXTERNAL;
        }
        if (role == UserRole.SUPER_USER) {
            throw new IllegalArgumentException("Super user cannot be created via admin. Only one user can exist.");
        }
        String tenantId = currentUser.getTenantId();
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("Tenant not resolved from context");
        }
        AdminCreateUserResponse created = adminUserService.createUser(req.getUsername(), req.getPhone(), role, tenantId);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}/force-reset")
    public ResponseEntity<?> forceReset(@PathVariable("id") String id) {
        adminUserService.forceReset(id);
        return ResponseEntity.ok(Map.of("status","RESET_REQUIRED"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        adminUserService.deleteUser(id);
        return ResponseEntity.ok(Map.of("deleted", true));
    }
}
