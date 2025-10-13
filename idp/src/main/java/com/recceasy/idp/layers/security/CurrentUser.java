package com.recceasy.idp.layers.security;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Request-scoped accessor for current user context populated by AuthContextFilter.
 * Falls back to empty context if no auth present.
 */
@Component
public class CurrentUser {
    private static final ThreadLocal<Map<String, Object>> CLAIMS_HOLDER = new ThreadLocal<>();

    public static void setClaims(Map<String, Object> claims) {
        CLAIMS_HOLDER.set(claims);
    }

    public static void clear() {
        CLAIMS_HOLDER.remove();
    }

    public String getUsername() {
        Map<String, Object> c = CLAIMS_HOLDER.get();
        return c == null ? null : (String) c.getOrDefault("username", c.get("sub"));
    }

    public String getTenantId() {
        Map<String, Object> c = CLAIMS_HOLDER.get();
        Object v = c == null ? null : c.get("tenant_id");
        return v == null ? null : String.valueOf(v);
    }

    public Set<String> getPermissions() {
        Map<String, Object> c = CLAIMS_HOLDER.get();
        if (c == null) return Collections.emptySet();
        Object p = c.get("permissions");
        if (p == null) return Collections.emptySet();
        Set<String> out = new HashSet<>();
        for (String s : String.valueOf(p).split(",")) {
            if (!s.isBlank()) out.add(s.trim());
        }
        return out;
    }

    public static boolean signedIn() {
        return CLAIMS_HOLDER.get() != null;
    }

    public boolean hasPermission(String permission) {
        return getPermissions().contains(permission);
    }
}
