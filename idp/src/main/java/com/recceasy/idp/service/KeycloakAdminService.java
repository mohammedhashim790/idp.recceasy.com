package com.recceasy.idp.service;


import com.recceasy.idp.dto.user.CreateUser;
import com.recceasy.idp.layers.user.User;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KeycloakAdminService {


    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public KeycloakAdminService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public User createUser(CreateUser createUser) throws Exception {
        UserRepresentation user = new UserRepresentation();

        user.setEnabled(true);
        user.setUsername(createUser.getUsername());

        user.setEmail(createUser.getUsername());
        user.setEmailVerified(true);

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

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
            return new User(createUser.getUsername());
        } else {
            // Handle error
            String errorMessage = response.readEntity(String.class);
            System.err.println("Failed to create user. Status: " + response.getStatus() + ", Reason: " + errorMessage);
            throw new Exception(errorMessage);
        }

    }


}
