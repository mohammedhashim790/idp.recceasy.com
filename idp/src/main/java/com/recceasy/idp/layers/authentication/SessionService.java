package com.recceasy.idp.layers.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
public class SessionService {
    private static final long SESSION_TTL_SECONDS = 60L * 60L * 24L; // 24h

    @Autowired
    private SessionTokenRepository repository;

    @Transactional
    public SessionToken issue(String userId, String tenantId) {
        String token = randomToken();
        long exp = Instant.now().getEpochSecond() + SESSION_TTL_SECONDS;
        SessionToken st = new SessionToken(token, userId, tenantId, exp);
        return repository.save(st);
    }

    public boolean validate(String token) {
        Optional<SessionToken> ref = repository.findByToken(token);
        return ref.filter(t -> !t.isRevoked() && !t.isExpired()).isPresent();
    }

    @Transactional
    public void revoke(String token) {
        repository.findByToken(token).ifPresent(t -> {
            t.setRevoked(true);
            repository.save(t);
        });
    }

    private String randomToken() {
        byte[] b = new byte[32];
        new SecureRandom().nextBytes(b);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
    }
}
