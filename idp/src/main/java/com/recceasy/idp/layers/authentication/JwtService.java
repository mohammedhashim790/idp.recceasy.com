package com.recceasy.idp.layers.authentication;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Minimal HS256 JWT generator/validator without external deps.
 */
public class JwtService {
    private final byte[] secret;
    private final long ttlSeconds;

    public JwtService(String secret, long ttlSeconds) {
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.ttlSeconds = ttlSeconds;
    }

    public String generateToken(String username, String name, String phone, String tenantId) {
        long now = Instant.now().getEpochSecond();
        long exp = now + ttlSeconds;
        Map<String, Object> payload = new HashMap<>();
        payload.put("sub", username);
        payload.put("username", username);
        payload.put("name", name);
        payload.put("phone", phone);
        payload.put("tenant_id", tenantId);
        payload.put("iat", now);
        payload.put("exp", exp);
        return sign(payload);
    }

    public boolean isValid(String token) {
        try {
            Map<String, Object> claims = verifyAndGetClaims(token);
            long exp = ((Number) claims.get("exp")).longValue();
            return Instant.now().getEpochSecond() < exp;
        } catch (Exception e) {
            return false;
        }
    }

    public Map<String, Object> verifyAndGetClaims(String token) throws Exception {
        String[] parts = token.split("\\.");
        if (parts.length != 3) throw new IllegalArgumentException("Invalid JWT format");
        String headerPayload = parts[0] + "." + parts[1];
        String expectedSig = base64Url(hmacSha256(headerPayload.getBytes(StandardCharsets.UTF_8)));
        if (!expectedSig.equals(parts[2])) throw new SecurityException("Invalid signature");
        String json = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        Map<String, Object> claims = Json.minimalParse(json);
        return claims;
    }

    private String sign(Map<String, Object> payload) {
        String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payloadJson = Json.minimalStringify(payload);
        String headerB64 = base64Url(headerJson.getBytes(StandardCharsets.UTF_8));
        String payloadB64 = base64Url(payloadJson.getBytes(StandardCharsets.UTF_8));
        String headerPayload = headerB64 + "." + payloadB64;
        String sig = base64Url(hmacSha256(headerPayload.getBytes(StandardCharsets.UTF_8)));
        return headerPayload + "." + sig;
    }

    private byte[] hmacSha256(byte[] data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret, "HmacSHA256"));
            return mac.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String base64Url(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }

    // Tiny JSON helper for flat maps
    static class Json {
        static String minimalStringify(Map<String, Object> map) {
            StringBuilder sb = new StringBuilder();
            sb.append('{');
            boolean first = true;
            for (Map.Entry<String, Object> e : map.entrySet()) {
                if (!first) sb.append(',');
                first = false;
                sb.append('"').append(escape(e.getKey())).append('"').append(':');
                Object v = e.getValue();
                if (v instanceof Number || v instanceof Boolean) {
                    sb.append(v);
                } else {
                    sb.append('"').append(escape(String.valueOf(v))).append('"');
                }
            }
            sb.append('}');
            return sb.toString();
        }

        static Map<String, Object> minimalParse(String json) {
            Map<String, Object> out = new HashMap<>();
            String s = json.trim();
            if (s.startsWith("{") && s.endsWith("}")) s = s.substring(1, s.length() - 1);
            if (s.isBlank()) return out;
            String[] parts = s.split(",");
            for (String p : parts) {
                String[] kv = p.split(":", 2);
                String key = unquote(kv[0].trim());
                String val = kv[1].trim();
                if (val.startsWith("\"") && val.endsWith("\"")) {
                    out.put(key, unquote(val));
                } else if (val.equals("true") || val.equals("false")) {
                    out.put(key, Boolean.parseBoolean(val));
                } else {
                    try {
                        out.put(key, Long.parseLong(val));
                    } catch (Exception ex) {
                        out.put(key, val);
                    }
                }
            }
            return out;
        }

        private static String unquote(String s) {
            if (s.startsWith("\"") && s.endsWith("\"")) return s.substring(1, s.length() - 1);
            return s;
        }

        private static String escape(String s) {
            return s.replace("\\", "\\\\").replace("\"", "\\\"");
        }
    }
}
