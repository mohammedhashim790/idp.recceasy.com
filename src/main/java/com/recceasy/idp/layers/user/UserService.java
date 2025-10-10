package com.recceasy.idp.layers.user;

import com.recceasy.idp.dto.user.UserStatus;
import com.recceasy.idp.handlers.ExceptionHandlers.InvalidTokenException;
import com.recceasy.idp.handlers.ExceptionHandlers.TokenExpiredException;
import com.recceasy.idp.handlers.ExceptionHandlers.UserExistingException;
import com.recceasy.idp.layers.password.PasswordService;
import com.recceasy.idp.layers.password.PasswordValidator;
import com.recceasy.idp.layers.user.UsernameValidator;
import com.recceasy.idp.layers.userVerification.UserVerificationLinkBuilder;
import com.recceasy.idp.layers.userVerification.UserVerificationService;
import com.recceasy.idp.utils.EmailService;
import com.recceasy.idp.utils.TemporaryPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private UserVerificationService userVerificationService;

    @Autowired
    private EmailService emailService;


    public User register(User newUser, String password) throws UserExistingException, jakarta.mail.MessagingException, UnsupportedEncodingException {
        // Validate username as email
        UsernameValidator.validateOrThrow(newUser.getUsername());
        // Validate password strength
        PasswordValidator.validateOrThrow(password);

        Optional<User> user = userRepository.findUserByUsername(newUser.getUsername());
        if (user.isPresent()) {
            throw new UserExistingException();
        }

        // Super User role for /auth/register flow as per requirements
        newUser.setRole(com.recceasy.idp.dto.user.UserRole.SUPER_USER);
        User createdUser = userRepository.save(newUser);
        passwordService.create(createdUser.getId(), password);

        this.emailService.sendVerificationEmail(
                new UserVerificationLinkBuilder()
                        .withToken(userVerificationService.create(createdUser.getUsername())
                        ).withUsername(newUser.getUsername()).build()
        , newUser.getUsername());

        return createdUser;
    }

    public User verify(String username, String token) throws UserExistingException, TokenExpiredException, InvalidTokenException {
        Optional<User> userRef = userRepository.findUserByUsername(username);
        if (userRef.isEmpty()) {
            throw new UsernameNotFoundException("Token for username '" + username + "' expired or invalid");
        }
        userVerificationService.verify(username, token);
        User user = userRef.get();
        user.setEnabled(true);
        user.setUserStatus(UserStatus.CONFIRMED);
        userRepository.save(user);
        return user;
    }

    // TODO
    // SEND EMAIL / SMS FOR VERIFICATION
    public User requestResetPassword(String username) {
        Optional<User> userRef = userRepository.findUserByUsername(username);
        if (userRef.isEmpty()) {
            throw new UsernameNotFoundException("Username '" + username + "' not found");
        }
        // Send Verification Link
        User user = userRef.get();
        passwordService.update(user.getId(), TemporaryPassword.generateTemporaryPassword());
        user.setUserStatus(UserStatus.RESET_PASSWORD);
        user.setEnabled(false);


        emailService.sendOTPEmail();

        return user;
    }

    public boolean verifyAndResetPassword(String username, String token, String password) throws InvalidTokenException, TokenExpiredException {
        Optional<User> userRef = userRepository.findUserByUsername(username);
        if (userRef.isEmpty()) {
            throw new UsernameNotFoundException("Username '" + username + "' not found");
        }

        // Send Verification Link
        User user = userRef.get();
        passwordService.update(user.getId(), password);
        user.setEnabled(true);
        user.setUserStatus(UserStatus.CONFIRMED);
        userVerificationService.verify(username, token);

        return true;
    }


}
