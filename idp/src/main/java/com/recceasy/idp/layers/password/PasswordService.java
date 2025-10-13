package com.recceasy.idp.layers.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    @Autowired
    private PasswordRepository passwordRepository;

    public Password create(String user_id, String password) {
        PasswordValidator.validateOrThrow(password);
        return passwordRepository.save(new Password(user_id, PasswordEncoder.encode(password)));
    }

    public Password update(String user_id, String password) {
        PasswordValidator.validateOrThrow(password);
        return passwordRepository.save(new Password(user_id, PasswordEncoder.encode(password)));
    }

}
