package com.recceasy.idp.layers.user;


import com.recceasy.idp.dto.password.RequestResetPassword;
import com.recceasy.idp.dto.password.ResetPassword;
import com.recceasy.idp.handlers.ExceptionHandlers.InvalidTokenException;
import com.recceasy.idp.handlers.ExceptionHandlers.TokenExpiredException;
import com.recceasy.idp.handlers.ExceptionHandlers.UserExistingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
public class UserController {


    @Autowired
    private UserService userService;


    @GetMapping("/verify")
    public User verify(@RequestParam String username, @RequestParam String token) throws UserExistingException, TokenExpiredException, InvalidTokenException {
        return userService.verify(username, token);
    }

    @PutMapping("/request-password")
    public User requestResetPassword(@RequestBody RequestResetPassword requestResetPassword) {
        return userService.requestResetPassword(requestResetPassword.getUsername());
    }

    @PutMapping("/reset-password")
    public boolean verifyAndResetPassword(@RequestBody ResetPassword resetPassword) throws InvalidTokenException, TokenExpiredException {
        return userService.verifyAndResetPassword(resetPassword.getUsername(), resetPassword.getToken(), resetPassword.getPassword());
    }


}
