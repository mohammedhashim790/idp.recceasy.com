package com.recceasy.idp.layers.password;

import com.recceasy.idp.layers.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordRepository extends JpaRepository<Password, User> {
    Optional<Password> findByUserId(String id);
}
