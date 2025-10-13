package com.recceasy.idp.layers.password;

import com.recceasy.idp.handlers.ExceptionHandlers.password.PasswordMissingLowercaseException;
import com.recceasy.idp.handlers.ExceptionHandlers.password.PasswordMissingSpecialCharacterException;
import com.recceasy.idp.handlers.ExceptionHandlers.password.PasswordMissingUppercaseException;
import com.recceasy.idp.handlers.ExceptionHandlers.password.PasswordTooShortException;

public class PasswordValidator {

    private static final int MIN_LENGTH = 12;
    private static final String SPECIALS = "!@#$%^&*_+-";

    public static void validateOrThrow(String password) {
        if (password == null) {
            throw new PasswordTooShortException();
        }
        if (password.length() < MIN_LENGTH) {
            throw new PasswordTooShortException();
        }
        if (!password.chars().anyMatch(Character::isUpperCase)) {
            throw new PasswordMissingUppercaseException();
        }
        if (!password.chars().anyMatch(Character::isLowerCase)) {
            throw new PasswordMissingLowercaseException();
        }
        boolean hasSpecial = password.chars().anyMatch(c -> SPECIALS.indexOf(c) >= 0);
        if (!hasSpecial) {
            throw new PasswordMissingSpecialCharacterException(SPECIALS);
        }
    }
}
