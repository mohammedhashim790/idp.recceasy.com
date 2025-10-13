package com.recceasy.idp.layers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recceasy.idp.dto.password.RequestResetPassword;
import com.recceasy.idp.dto.password.ResetPassword;
import com.recceasy.idp.handlers.ExceptionHandlers.InvalidTokenException;
import com.recceasy.idp.handlers.ExceptionHandlers.TokenExpiredException;
import com.recceasy.idp.handlers.GlobalExceptionHandler.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@Import(GlobalExceptionHandler.class)
@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void verify_success_returnsUser() throws Exception {
        User u = new User("john@example.com");
        u.setId("u1");
        given(userService.verify("john@example.com", "tok")).willReturn(u);

        mockMvc.perform(post("/auth/verify").param("username", "john@example.com").param("token", "tok")).andExpect(status().isOk()).andExpect(jsonPath("$.id", is("u1"))).andExpect(jsonPath("$.username", is("john@example.com")));
    }

    @Test
    void verify_tokenExpired_returns500() throws Exception {
        given(userService.verify("john@example.com", "tok")).willThrow(new TokenExpiredException());
        mockMvc.perform(post("/auth/verify").param("username", "john@example.com").param("token", "tok")).andExpect(status().isInternalServerError()).andExpect(jsonPath("$.message").exists());
    }

    @Test
    void requestPassword_success_returnsTrue() throws Exception {
        RequestResetPassword req = new RequestResetPassword();
        req.setUsername("john@example.com");
        given(userService.requestResetPassword("john@example.com")).willReturn(new User());

        mockMvc.perform(put("/auth/request-password").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req))).andExpect(status().isOk()).andExpect(content().string("true"));
    }

    @Test
    void requestPassword_userNotFound_returns500() throws Exception {
        RequestResetPassword req = new RequestResetPassword();
        req.setUsername("missing@example.com");
        given(userService.requestResetPassword("missing@example.com")).willThrow(new UsernameNotFoundException("Username 'missing@example.com' not found"));

        mockMvc.perform(put("/auth/request-password").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req))).andExpect(status().isInternalServerError()).andExpect(jsonPath("$.message", is("Username 'missing@example.com' not found")));
    }

    @Test
    void resetPassword_success_returnsTrue() throws Exception {
        ResetPassword req = new ResetPassword();
        req.setUsername("john@example.com");
        req.setToken("tok");
        req.setPassword("StrongPass!2345");
        given(userService.verifyAndResetPassword("john@example.com", "tok", "StrongPass!2345")).willReturn(true);

        mockMvc.perform(put("/auth/reset-password").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req))).andExpect(status().isOk()).andExpect(content().string("true"));
    }

    @Test
    void resetPassword_invalidToken_returns500() throws Exception {
        ResetPassword req = new ResetPassword();
        req.setUsername("john@example.com");
        req.setToken("bad");
        req.setPassword("StrongPass!2345");
        given(userService.verifyAndResetPassword(anyString(), anyString(), anyString())).willThrow(new InvalidTokenException());

        mockMvc.perform(put("/auth/reset-password").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req))).andExpect(status().isInternalServerError()).andExpect(jsonPath("$.message").exists());
    }
}
