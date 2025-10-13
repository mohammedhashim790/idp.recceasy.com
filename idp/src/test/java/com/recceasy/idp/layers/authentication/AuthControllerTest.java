package com.recceasy.idp.layers.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recceasy.idp.dto.auth.LoginRequest;
import com.recceasy.idp.dto.user.CreateUser;
import com.recceasy.idp.handlers.GlobalExceptionHandler.GlobalExceptionHandler;
import com.recceasy.idp.handlers.ExceptionHandlers.username.InvalidEmailException;
import com.recceasy.idp.layers.password.Password;
import com.recceasy.idp.layers.password.PasswordRepository;
import com.recceasy.idp.layers.user.User;
import com.recceasy.idp.layers.user.UserRepository;
import com.recceasy.idp.layers.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@AutoConfigureMockMvc
@Import(GlobalExceptionHandler.class)
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordRepository passwordRepository;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private SessionService sessionService;

    @MockitoBean
    private UserService userService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private User enabledUser;

    @BeforeEach
    void setup() {
        enabledUser = new User("john@example.com");
        enabledUser.setId("u1");
        enabledUser.setEnabled(true);
        enabledUser.setPhone("1234567890");
        enabledUser.setTenantId("tenant-1");
    }

    @Test
    void login_userNotFound_returns401() throws Exception {
        given(userRepository.findUserByUsername("john@example.com")).willReturn(Optional.empty());
        LoginRequest req = new LoginRequest();
        req.setUsername("john@example.com");
        req.setPassword("Password!23456");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Invalid credentials")));
    }

    @Test
    void login_userDisabled_returns401() throws Exception {
        User u = new User("john@example.com");
        u.setEnabled(false);
        given(userRepository.findUserByUsername("john@example.com")).willReturn(Optional.of(u));

        LoginRequest req = new LoginRequest();
        req.setUsername("john@example.com");
        req.setPassword("Password!23456");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("User is not verified yet")));
    }

    @Test
    void login_passwordMissing_returns401() throws Exception {
        User u = new User("john@example.com");
        u.setEnabled(true);
        u.setId("u1");
        given(userRepository.findUserByUsername("john@example.com")).willReturn(Optional.of(u));
        given(passwordRepository.findByUserId("u1")).willReturn(Optional.empty());

        LoginRequest req = new LoginRequest();
        req.setUsername("john@example.com");
        req.setPassword("Password!23456");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Invalid credentials")));
    }

    @Test
    void login_wrongPassword_returns401() throws Exception {
        User u = enabledUser;
        given(userRepository.findUserByUsername("john@example.com")).willReturn(Optional.of(u));
        String hash = encoder.encode("CorrectPassword!234");
        given(passwordRepository.findByUserId("u1")).willReturn(Optional.of(new Password("u1", hash)));

        LoginRequest req = new LoginRequest();
        req.setUsername("john@example.com");
        req.setPassword("WrongPassword!234");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Invalid credentials")));
    }

    @Test
    void login_success_returnsJwtAndSessionToken() throws Exception {
        User u = enabledUser;
        given(userRepository.findUserByUsername("john@example.com")).willReturn(Optional.of(u));
        String hash = encoder.encode("CorrectPassword!234");
        given(passwordRepository.findByUserId("u1")).willReturn(Optional.of(new Password("u1", hash)));
        given(jwtService.generateToken(anyString(), anyString(), org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any())).willReturn("jwt-token");
        given(sessionService.issue(eq("u1"), eq("tenant-1"))).willReturn(new SessionToken("session123", "u1", "tenant-1", System.currentTimeMillis()/1000 + 3600));

        LoginRequest req = new LoginRequest();
        req.setUsername("john@example.com");
        req.setPassword("CorrectPassword!234");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt", is("jwt-token")))
                .andExpect(jsonPath("$.sessionToken", is("session123")));
    }

    @Test
    void validate_missingAuthorization_returns401() throws Exception {
        mockMvc.perform(post("/auth/validate"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", containsString("Missing or invalid Authorization")));
    }

    @Test
    void validate_invalidJwt_returns401() throws Exception {
        given(jwtService.isValid("bad")).willReturn(false);
        mockMvc.perform(post("/auth/validate").header("Authorization", "Bearer bad"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", containsString("Invalid or expired JWT")));
    }

    @Test
    void validate_invalidSession_returns401() throws Exception {
        given(jwtService.isValid("good")).willReturn(true);
        mockMvc.perform(post("/auth/validate").header("Authorization", "Bearer good").header("X-Session-Token", "expired"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", containsString("Invalid or expired session token")));
    }

    @Test
    void validate_missingSession_returns401() throws Exception {
        given(jwtService.isValid("good")).willReturn(true);
        mockMvc.perform(post("/auth/validate").header("Authorization", "Bearer good"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", containsString("Invalid or expired session token")));
    }

    @Test
    void validate_ok_returns200() throws Exception {
        given(jwtService.isValid("good")).willReturn(true);
        given(sessionService.validate("ok-session")).willReturn(true);
        mockMvc.perform(post("/auth/validate").header("Authorization", "Bearer good").header("X-Session-Token", "ok-session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Valid")));
    }

    @Test
    void logout_revokesSession_returns200() throws Exception {
        mockMvc.perform(post("/auth/logout").header("X-Session-Token", "tok"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Logged out")));
        verify(sessionService, times(1)).revoke("tok");
    }

    @Test
    void register_delegatesToUserService_returnsUser() throws Exception {
        CreateUser cu = new CreateUser();
        cu.setUsername("john@example.com");
        cu.setPassword("Str0ngPassword!@");
        User created = new User("john@example.com");
        created.setId("u1");
        given(userService.register(org.mockito.ArgumentMatchers.any(User.class), eq("Str0ngPassword!@"))).willReturn(created);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cu)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("u1")))
                .andExpect(jsonPath("$.username", is("john@example.com")));
    }

    @Test
    void register_invalidEmail_returns400() throws Exception {
        CreateUser cu = new CreateUser();
        cu.setUsername("not-an-email");
        cu.setPassword("Str0ngPassword!@");
        org.mockito.BDDMockito.willThrow(new InvalidEmailException("Username must be a valid email address"))
                .given(userService)
                .register(org.mockito.ArgumentMatchers.any(User.class), anyString());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cu)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("valid email")));
    }

    @Test
    void register_weakPassword_returns400() throws Exception {
        CreateUser cu = new CreateUser();
        cu.setUsername("john@example.com");
        cu.setPassword("weak");
        given(userService.register(org.mockito.ArgumentMatchers.any(User.class), anyString())).willThrow(new com.recceasy.idp.handlers.ExceptionHandlers.password.PasswordTooShortException());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cu)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Password")));
    }
}
