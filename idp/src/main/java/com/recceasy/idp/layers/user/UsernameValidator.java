package com.recceasy.idp.layers.user;

import com.recceasy.idp.handlers.ExceptionHandlers.username.InvalidEmailException;

import java.util.regex.Pattern;

public class UsernameValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    public static void validateOrThrow(String username) {
        if (username == null || username.isBlank()) {
            throw new InvalidEmailException("Username must be a non-empty email address");
        }
        if (!EMAIL_PATTERN.matcher(username).matches()) {
            throw new InvalidEmailException("Username must be a valid email address");
        }
    }
}
