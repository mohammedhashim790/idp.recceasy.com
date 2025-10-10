package com.recceasy.idp.layers.domain;

public class DomainValidator {


    private static final String DOMAIN_PATTERN = "^[a-zA-Z.]+$";

    /**
     * Extracts the second-level domain name from a given string.
     * For example, for "contacts.google.com" or "google.com", it returns "google".
     *
     * @param domain The input domain string.
     * @return The second-level domain name.
     */
    public static String getSecondLevelDomain(String domain) {
        // Return an empty string if the input is null or invalid
        if (domain == null || domain.isEmpty()) {
            return "";
        }

        // Split the string by the period. We need to escape it with \\
        String[] parts = domain.split("\\.");

        // If there are at least two parts (e.g., "google.com"),
        // the main domain is the second-to-last one.
        if (parts.length >= 2) {
            return parts[parts.length - 2];
        }

        // Otherwise, the input might be a single word like "localhost", so return it.
        return domain;
    }


    public static boolean validateDomain(String domain) {
        return domain.matches(DOMAIN_PATTERN);
    }
}
