package com.recceasy.idp.layers.userVerification;

import com.recceasy.idp.handlers.ExceptionHandlers.InvalidTokenException;
import com.recceasy.idp.handlers.ExceptionHandlers.TokenExpiredException;
import com.recceasy.idp.handlers.TokenHandler.VerificationTokenHandler;
import com.recceasy.idp.utils.RecceasyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserVerificationService {

    @Autowired
    private UserVerificationRepository userVerificationRepository;

    public boolean verify(String username, String token) throws TokenExpiredException, InvalidTokenException {
        UserVerification userVerification = userVerificationRepository.findUserVerificationByUsername(username);
        long currentTime = RecceasyTime.now();
        if (currentTime > userVerification.getExpiresAt()) {
            throw new TokenExpiredException();
        }
        if (!token.equals(userVerification.getToken())) {
            throw new InvalidTokenException();
        }
        delete(userVerification);
        return true;
    }

    public String create(String username) {
        String token = new VerificationTokenHandler().generateToken();
        userVerificationRepository.save(new UserVerification(username, token));
        return token;
    }

    public void delete(UserVerification userVerification) {
        userVerificationRepository.delete(userVerification);
    }

}
