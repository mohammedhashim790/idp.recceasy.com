package com.recceasy.idp.config;

import com.recceasy.idp.layers.authentication.JwtService;
import com.recceasy.idp.layers.authentication.SessionService;
import com.recceasy.idp.layers.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Lightweight filter that validates Authorization: Bearer <JWT> and optional X-Session-Token,
 * then exposes claims through CurrentUser. Does not block requests (keeps permitAll) but
 * allows services/controllers to read current user/tenant.
 */
@Component
public class AuthContextFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private SessionService sessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String auth = request.getHeader("Authorization");
        String session = request.getHeader("X-Session-Token");
        try {
            if (auth != null && auth.startsWith("Bearer ")) {
                String token = auth.substring(7);
                if (jwtService.isValid(token)) {
                    Map<String, Object> claims = jwtService.verifyAndGetClaims(token);
                    // If session header present, ensure it's valid as well; otherwise ignore
                    if (session == null || sessionService.validate(session)) {
                        CurrentUser.setClaims(claims);
                    }
                }
            }
        } catch (Exception ignored) {
            // Keep silent; context will just be empty
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            CurrentUser.clear();
        }
    }
}
