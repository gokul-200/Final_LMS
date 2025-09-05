//package com.example.lms.controller;
//
//import com.example.lms.dto.AuthDtos;
//import com.example.lms.model.Role;
//import com.example.lms.model.User;
//import com.example.lms.repository.UserRepository;
////import com.example.lms.security.JwtUtil;
//import com.example.lms.service.AuthService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/auth")
//@CrossOrigin
//public class AuthController {
//    @Autowired private AuthService authService;
//    @Autowired private AuthenticationManager authManager;
////    @Autowired private JwtUtil jwtUtil;
//    @Autowired private UserRepository userRepository;
//
//    @PostMapping("/register")
//    public User register(@RequestBody AuthDtos dto) {
//        return authService.register(dto.getUsername(), dto.getPassword(),
//                dto.getRole().equalsIgnoreCase("ADMIN") ? Role.ADMIN : Role.EMPLOYEE);
//    }
//
////    @PostMapping("/login")
////    public Map<String, String> login(@RequestBody AuthDtos dto) {
////        Authentication authentication = authManager.authenticate(
////                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
////        );
////
////        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow();
////        System.out.println("Trying login for :"+dto.getUsername());
////        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
////
////        Map<String, String> response = new HashMap<>();
////        response.put("token", token);
////        response.put("role", user.getRole().name());
////        return response;
////    }
//
//    @PostMapping("/login")
//    public Map<String, String> login(@RequestBody AuthDtos dto) {
//        authManager.authenticate(
//                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
//        );
//
//        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow();
//
//        Map<String, String> response = new HashMap<>();
//        response.put("message", "Login successful");
//        response.put("role", user.getRole().name());
//        return response;
//    }
//}

package com.example.lms.controller;

import com.example.lms.dto.AuthDtos;
import com.example.lms.model.LeaveBalance;
import com.example.lms.model.Role;
import com.example.lms.model.User;
import com.example.lms.repository.LeaveBalanceRepository;
import com.example.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody AuthDtos dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "User already exists"));
        }

//        User user = new User(null, dto.getUsername(), passwordEncoder.encode(dto.getPassword()), dto.getRole().equalsIgnoreCase("ADMIN") ? com.example.lms.model.Role.ADMIN : com.example.lms.model.Role.EMPLOYEE);
//        userRepository.save(user);
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("ADMIN".equalsIgnoreCase(dto.getRole()) ? Role.ADMIN : Role.EMPLOYEE);
        userRepository.save(user);

        if (user.getRole() == Role.EMPLOYEE &&
                leaveBalanceRepository.findByUserId(user.getId()) == null) {
            LeaveBalance b = new LeaveBalance();
            b.setTotalLeaves(20);
            b.setUsedLeaves(0);
            b.setRemainingLeaves(20);
            b.setUser(user);
            leaveBalanceRepository.save(b);
        }


        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthDtos dto) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );

            User user = userRepository.findByUsername(dto.getUsername()).orElseThrow();

            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "role", user.getRole().name()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials"));
        }
    }
}

