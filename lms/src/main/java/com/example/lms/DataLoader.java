//package com.example.lms;
//
//import com.example.lms.model.*;
//import com.example.lms.repository.LeaveBalanceRepository;
//import com.example.lms.repository.LeaveRequestRepository;
//import com.example.lms.repository.UserRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DataLoader implements CommandLineRunner {
//    private final UserRepository userRepository;
//    private final LeaveBalanceRepository leaveBalanceRepository;
//    private final LeaveRequestRepository leaveRequestRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public DataLoader(UserRepository userRepository, LeaveBalanceRepository leaveBalanceRepository,
//                      LeaveRequestRepository leaveRequestRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.leaveBalanceRepository = leaveBalanceRepository;
//        this.leaveRequestRepository = leaveRequestRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        if (userRepository.findByUsername("admin").isEmpty()) {
//            User admin = new User(null, "admin", passwordEncoder.encode("admin123"), Role.ADMIN);
//            userRepository.save(admin);
//        }
//
//        if (userRepository.findByUsername("emp1").isEmpty()) {
//            User emp1 = new User(null, "emp1", passwordEncoder.encode("emp123"), Role.EMPLOYEE);
//            userRepository.save(emp1);
//            leaveBalanceRepository.save(new LeaveBalance(null, 20, 0, 20, emp1));
//            leaveRequestRepository.save(new LeaveRequest(null, "Medical Leave", "PENDING", emp1));
//        }
//
//        if (userRepository.findByUsername("emp2").isEmpty()) {
//            User emp2 = new User(null, "emp2", passwordEncoder.encode("emp123"), Role.EMPLOYEE);
//            userRepository.save(emp2);
//            leaveBalanceRepository.save(new LeaveBalance(null, 20, 0, 20, emp2));
//        }
//    }
//
//}

// src/main/java/com/example/lms/DataLoader.java
package com.example.lms;

import com.example.lms.model.LeaveBalance;
import com.example.lms.model.LeaveRequest;
import com.example.lms.model.Role;
import com.example.lms.model.User;
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
        // --- Admin ---
        userRepository.findByUsername("admin").orElseGet(() ->
                userRepository.save(new User(
                        null,
                        "admin",
                        passwordEncoder.encode("admin123"),
                        Role.ADMIN
                ))
        );

        // --- emp1 ---
        User emp1 = userRepository.findByUsername("emp1").orElseGet(() ->
                userRepository.save(new User(
                        null,
                        "emp1",
                        passwordEncoder.encode("emp123"),
                        Role.EMPLOYEE
                ))
        );
        if (leaveBalanceRepository.findByUserId(emp1.getId()) == null) {
            // LeaveBalance(totalLeaves, takenLeaves, remainingLeaves, user)
            leaveBalanceRepository.save(new LeaveBalance(
                    null, 20, 0, 20, emp1
            ));
        }
        // Seed one pending leave with dates for emp1 if none exist
        if (leaveRequestRepository.findByUserId(emp1.getId()).isEmpty()) {
            LeaveRequest lr = new LeaveRequest();
            lr.setReason("Medical Leave");
            lr.setStatus("PENDING");
            lr.setFromDate(LocalDate.now().plusDays(1)); // tomorrow
            lr.setToDate(LocalDate.now().plusDays(3));   // 2-day leave (inclusive)
            lr.setUser(emp1);
            leaveRequestRepository.save(lr);
        }

        // --- emp2 ---
        User emp2 = userRepository.findByUsername("emp2").orElseGet(() ->
                userRepository.save(new User(
                        null,
                        "emp2",
                        passwordEncoder.encode("emp123"),
                        Role.EMPLOYEE
                ))
        );
        if (leaveBalanceRepository.findByUserId(emp2.getId()) == null) {
            leaveBalanceRepository.save(new LeaveBalance(
                    null, 20, 0, 20, emp2
            ));
        }
    }
}

