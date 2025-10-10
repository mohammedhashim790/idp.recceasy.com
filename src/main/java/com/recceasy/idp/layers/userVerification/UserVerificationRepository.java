package com.recceasy.idp.layers.userVerification;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserVerificationRepository extends JpaRepository<UserVerification, String > {
    UserVerification findUserVerificationByUsername(String username);
}
