package com.recceasy.idp.layers.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recceasy.idp.dto.user.AdminCreateUserRequest;
import com.recceasy.idp.dto.user.AdminCreateUserResponse;
import com.recceasy.idp.dto.user.UserRole;
import com.recceasy.idp.dto.user.UserStatus;
import com.recceasy.idp.handlers.GlobalExceptionHandler.GlobalExceptionHandler;
import com.recceasy.idp.layers.security.CurrentUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@Import(GlobalExceptionHandler.class)
@SpringBootTest
class AdminUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdminUserService adminUserService;

    @MockitoBean
    private CurrentUser currentUser;

    @Test
    void create_withNullRole_defaultsExternal_returns200() throws Exception {
        AdminCreateUserRequest req = new AdminCreateUserRequest();
        req.setUsername("user@example.com");
        req.setPhone("123");
        // role null
        given(currentUser.getTenantId()).willReturn("tenant-1");
        AdminCreateUserResponse resp = new AdminCreateUserResponse("u1","user@example.com", UserRole.EXTERNAL, "Temp!Pass12345", UserStatus.RESET_REQUIRED);
        given(adminUserService.createUser(eq("user@example.com"), eq("123"), eq(UserRole.EXTERNAL), eq("tenant-1"))).willReturn(resp);

        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is("u1")))
                .andExpect(jsonPath("$.role", is("EXTERNAL")))
                .andExpect(jsonPath("$.status", is("RESET_REQUIRED")));
    }

    @Test
    void create_superUserRole_returns400() throws Exception {
        AdminCreateUserRequest req = new AdminCreateUserRequest();
        req.setUsername("user@example.com");
        req.setRole(UserRole.SUPER_USER);
        given(currentUser.getTenantId()).willReturn("tenant-1");

        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Super user cannot be created via admin. Only one user can exist.")));
    }

    @Test
    void create_missingTenantInContext_returns400() throws Exception {
        AdminCreateUserRequest req = new AdminCreateUserRequest();
        req.setUsername("user@example.com");
        req.setRole(UserRole.ADMIN);
        given(currentUser.getTenantId()).willReturn(null);

        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Tenant not resolved from context")));
    }

    @Test
    void create_valid_returnsResponse200() throws Exception {
        AdminCreateUserRequest req = new AdminCreateUserRequest();
        req.setUsername("user2@example.com");
        req.setPhone("555");
        req.setRole(UserRole.ADMIN);
        given(currentUser.getTenantId()).willReturn("tenant-2");
        AdminCreateUserResponse resp = new AdminCreateUserResponse("u2","user2@example.com", UserRole.ADMIN, "Temp!Pass12345", UserStatus.RESET_REQUIRED);
        given(adminUserService.createUser(eq("user2@example.com"), eq("555"), eq(UserRole.ADMIN), eq("tenant-2"))).willReturn(resp);

        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is("u2")))
                .andExpect(jsonPath("$.role", is("ADMIN")));
    }

    @Test
    void forceReset_returns200() throws Exception {
        mockMvc.perform(put("/admin/users/abc/force-reset"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("RESET_REQUIRED")));
        verify(adminUserService, times(1)).forceReset("abc");
    }

    @Test
    void deleteUser_returns200() throws Exception {
        mockMvc.perform(delete("/admin/users/abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deleted", is(true)));
        verify(adminUserService, times(1)).deleteUser("abc");
    }
}
