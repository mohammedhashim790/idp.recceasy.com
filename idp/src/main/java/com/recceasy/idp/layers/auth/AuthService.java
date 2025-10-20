package com.recceasy.idp.layers.auth;

import com.recceasy.idp.dto.user.CreateUser;
import com.recceasy.idp.layers.password.PasswordService;
import com.recceasy.idp.layers.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordService passwordService;


    public void signUp(CreateUser user) {

    }
}
