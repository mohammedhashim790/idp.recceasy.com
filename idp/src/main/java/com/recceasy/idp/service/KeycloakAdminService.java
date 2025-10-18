package com.recceasy.idp.service;


import com.recceasy.idp.dto.user.CreateUser;
import com.recceasy.idp.layers.user.User;
import com.recceasy.idp.utils.EmailService;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class KeycloakAdminService {


    private final Keycloak keycloak;
    private final EmailService emailService; // <-- NEW

    @Value("${keycloak.realm}")
    private String realm;

    public KeycloakAdminService(Keycloak keycloak, EmailService emailService) {
        this.keycloak = keycloak;
        this.emailService = emailService;
    }

    public User createUser(CreateUser createUser) throws Exception {
        UserRepresentation user = new UserRepresentation();

        user.setEnabled(true);
        user.setUsername(createUser.getUsername());

        user.setEmail(createUser.getUsername());
        user.setEmailVerified(false);

        user.setRequiredActions(new ArrayList<>() {{
            add("VERIFY_EMAIL");
        }});

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        // --- NEW: Generate and add verification token as an attribute ---
        String verificationToken = UUID.randomUUID().toString();
        user.setAttributes(Collections.singletonMap("verificationToken", Collections.singletonList(verificationToken)));

        // Create user (requires manage-users role)
        Response response = usersResource.create(user);

        // Handle response
        if (response.getStatus() == 201) {
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(false);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(createUser.getPassword());

            usersResource.get(userId).resetPassword(passwordCred);
            System.out.println("User created successfully with ID: " + userId);
            emailService.sendVerificationEmail(user.getEmail(), verificationToken);
            return new User(createUser.getUsername());
        } else {
            // Handle error
            String errorMessage = response.readEntity(String.class);
            System.err.println("Failed to create user. Status: " + response.getStatus() + ", Reason: " + errorMessage);
            throw new Exception(errorMessage);
        }
    }

    public void verifyUser(String token) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        // TODO: For production, a mapping table (token -> userId) in a local DB is faster.
        List<UserRepresentation> users = usersResource.searchByAttributes("verificationToken:" + token);

        if (users.isEmpty()) {
            throw new RuntimeException("Invalid or expired verification token.");
        }

        UserRepresentation user = users.get(0);

        user.setEnabled(true);
        user.setEmailVerified(true);

        user.getAttributes().remove("verificationToken");
        usersResource.get(user.getId()).update(user);
        System.out.println("User verified and enabled: " + user.getUsername());
    }


}
