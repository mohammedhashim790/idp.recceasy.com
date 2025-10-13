package com.recceasy.idp.handlers.TokenHandler;

import java.util.Random;

public class VerificationTokenHandler implements TokenHandler {


    public String generateToken() {
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        int MINIMUM_LENGTH = 100;
        int MAXIMUM_LENGTH = 150;
        while (salt.length() < (rnd.nextInt(MAXIMUM_LENGTH + 1 - MINIMUM_LENGTH) + MINIMUM_LENGTH)) {
            String SALT_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_!*";
            int index = (int) (rnd.nextFloat() * SALT_CHARACTERS.length());
            salt.append(SALT_CHARACTERS.charAt(index));
        }
        return salt.toString();
    }


}
