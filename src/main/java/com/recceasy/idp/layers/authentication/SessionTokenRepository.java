package com.recceasy.idp.layers.authentication;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionTokenRepository extends JpaRepository<SessionToken, String> {
    Optional<SessionToken> findByToken(String token);
}
