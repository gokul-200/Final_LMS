

package com.example.lms;

import com.example.lms.model.*;
import com.example.lms.repository.LeaveBalanceRepository;
import com.example.lms.repository.LeaveRequestRepository;
import com.example.lms.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository,
                      LeaveBalanceRepository leaveBalanceRepository,
                      LeaveRequestRepository leaveRequestRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // --- admin ---
        userRepository.findByUsername("admin").orElseGet(() -> {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            return userRepository.save(admin);
        });

        // --- emp1 ---
        User emp1 = userRepository.findByUsername("emp1").orElseGet(() -> {
            User u = new User();
            u.setUsername("emp1");
            u.setPassword(passwordEncoder.encode("emp123"));
            u.setRole(Role.EMPLOYEE);
            return userRepository.save(u);
        });

        if (leaveBalanceRepository.findByUserId(emp1.getId()) == null) {
            LeaveBalance b = new LeaveBalance();
            b.setTotalLeaves(20);
            b.setUsedLeaves(0);
            b.setRemainingLeaves(20);
            b.setUser(emp1);
            leaveBalanceRepository.save(b);
        }

        if (leaveRequestRepository.findByUserId(emp1.getId()).isEmpty()) {
            LeaveRequest lr = new LeaveRequest();
            lr.setReason("Medical Leave");
            lr.setStatus("PENDING");
            // include these only if your entity has these fields:
            lr.setFromDate(LocalDate.now().plusDays(1));
            lr.setToDate(LocalDate.now().plusDays(2));
            lr.setUser(emp1);
            leaveRequestRepository.save(lr);
        }

        // --- emp2 ---
        User emp2 = userRepository.findByUsername("emp2").orElseGet(() -> {
            User u = new User();
            u.setUsername("emp2");
            u.setPassword(passwordEncoder.encode("emp123"));
            u.setRole(Role.EMPLOYEE);
            return userRepository.save(u);
        });

        if (leaveBalanceRepository.findByUserId(emp2.getId()) == null) {
            LeaveBalance b = new LeaveBalance();
            b.setTotalLeaves(20);
            b.setUsedLeaves(0);
            b.setRemainingLeaves(20);
            b.setUser(emp2);
            leaveBalanceRepository.save(b);
        }
    }
}