package com.recceasy.idp.layers.health;

import com.recceasy.idp.dto.user.CreateUser;
import com.recceasy.idp.service.KeycloakAdminService;
import com.recceasy.idp.utils.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/health")
public class HealthController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private KeycloakAdminService keycloakAdminService;

    @GetMapping
    public String health() throws MessagingException, jakarta.mail.MessagingException, UnsupportedEncodingException {
        return "{\"status\":\"I'm healthy\"}";
    }

    @GetMapping("/time")
    public long time() {
        return System.currentTimeMillis();
    }

    @PutMapping("/put")
    public void put() throws Exception {
        this.keycloakAdminService.createUser(new CreateUser("agathachristie025@gmail.com", "Test@123"));
    }


    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        try {
            keycloakAdminService.verifyUser(token);
            return ResponseEntity.ok("<h1>Account Verified!</h1><p>Your account has been successfully verified. You can now log in.</p>");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("<h1>Verification Failed</h1><p>" + e.getMessage() + "</p>");
        }
    }


}
