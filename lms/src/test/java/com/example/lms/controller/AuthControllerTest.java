package com.example.lms.controller;

import com.example.lms.model.Role;
import com.example.lms.model.User;
import com.example.lms.repository.LeaveBalanceRepository;
import com.example.lms.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired private MockMvc mvc;

    @MockBean private AuthenticationManager authManager;
    @MockBean private UserRepository userRepository;
    @MockBean private PasswordEncoder passwordEncoder;
    @MockBean private LeaveBalanceRepository leaveBalanceRepository;

    @Test
    void login_returnsRoleAdmin_onSuccess() throws Exception {
        User user=new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("admintest");
        user.setRole(Role.ADMIN);


        Mockito.when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));

        Mockito.when(authManager.authenticate(Mockito.any()))
                        .thenReturn(new TestingAuthenticationToken("admin","admin123"));

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"admin123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }
}

