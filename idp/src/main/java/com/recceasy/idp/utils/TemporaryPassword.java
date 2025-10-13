package com.recceasy.idp.utils;

import com.recceasy.idp.layers.password.PasswordValidator;

import java.security.SecureRandom;

public class TemporaryPassword {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIALS = "!@#$%^&*()_+-=[]{}|;:'\"<>,.?/`~";

    public static String generateTemporaryPassword() {
        int length = 16; // longer than minimum
        SecureRandom random = new SecureRandom();
        String all = UPPER + LOWER + DIGITS + SPECIALS;
        StringBuilder sb = new StringBuilder();
        // ensure required types
        sb.append(UPPER.charAt(random.nextInt(UPPER.length())));
        sb.append(LOWER.charAt(random.nextInt(LOWER.length())));
        sb.append(SPECIALS.charAt(random.nextInt(SPECIALS.length())));
        // fill the rest
        for (int i = sb.length(); i < length; i++) {
            sb.append(all.charAt(random.nextInt(all.length())));
        }
        String candidate = shuffle(sb.toString(), random);
        // validate and, if somehow fails, regenerate
        if (!isValid(candidate)) {
            return generateTemporaryPassword();
        }
        return candidate;
    }

    private static boolean isValid(String password) {
        try {
            PasswordValidator.validateOrThrow(password);
            return true;
        } catch (RuntimeException ex) {
            return false;
        }
    }

    private static String shuffle(String input, SecureRandom random) {
        char[] a = input.toCharArray();
        for (int i = a.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }
        return new String(a);
    }
}
