package com.recceasy.idp.layers.authentication;

import com.recceasy.idp.dto.auth.LoginRequest;
import com.recceasy.idp.dto.auth.LoginResponse;
import com.recceasy.idp.dto.user.CreateUser;
import com.recceasy.idp.layers.password.Password;
import com.recceasy.idp.layers.password.PasswordRepository;
import com.recceasy.idp.layers.user.User;
import com.recceasy.idp.layers.user.UserRepository;
import com.recceasy.idp.layers.user.UserService;
import com.recceasy.idp.service.KeycloakAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserService userService;

    @Autowired
    private KeycloakAdminService keycloakAdminService;

    @PostMapping("/register")
    public User register(@RequestBody CreateUser createUser) throws Exception {
        return keycloakAdminService.createUser(createUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userRef = userRepository.findUserByUsername(request.getUsername());
        if (userRef.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials"));
        }
        User user = userRef.get();
        if (!user.isEnabled()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User is not verified yet"));
        }
        Optional<Password> passRef = passwordRepository.findByUserId(user.getId());
        if (passRef.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials"));
        }
        Password stored = passRef.get();
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        if (!encoder.matches(request.getPassword(), stored.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials"));
        }

        String name = user.getUsername();
        String phone = user.getPhone();
        String tenantId = user.getTenantId();
        String jwt = jwtService.generateToken(user.getUsername(), name, phone, tenantId);
        SessionToken session = sessionService.issue(user.getId(), tenantId);
        return ResponseEntity.ok(new LoginResponse(jwt, session.getToken()));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader(value = "Authorization", required = false) String authorization, @RequestHeader(value = "X-Session-Token", required = false) String sessionToken) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Missing or invalid Authorization header"));
        }
        String jwt = authorization.substring(7);
        if (!jwtService.isValid(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid or expired JWT"));
        }
        if (sessionToken == null || !sessionService.validate(sessionToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid or expired session token"));
        }
        return ResponseEntity.ok(Map.of("message", "Valid"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "X-Session-Token") String sessionToken) {
        sessionService.revoke(sessionToken);
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }
}
