package com.example.lms.service;


import com.example.lms.model.*;
import com.example.lms.repository.LeaveBalanceRepository;
import com.example.lms.repository.LeaveRequestRepository;
import com.example.lms.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaveServiceTest {

    @Mock LeaveRequestRepository leaveRequestRepository;
    @Mock LeaveBalanceRepository leaveBalanceRepository;
    @Mock UserRepository userRepository;

    @InjectMocks LeaveService leaveService; // your existing service

    @Test
    void approve_shouldDeductDaysFromBalance() {
        User emp = new User();
        emp.setId(1L);
        emp.setUsername("emp1");
        emp.setPassword("x");
        emp.setRole(Role.EMPLOYEE);

        LeaveRequest lr = new LeaveRequest();
        lr.setId(100L);
        lr.setUser(emp);
        lr.setReason("Trip");
        lr.setStatus("PENDING");
        lr.setFromDate(LocalDate.of(2025, 9, 10));
        lr.setToDate(LocalDate.of(2025, 9, 12)); // 3 days inclusive

        LeaveBalance bal = new LeaveBalance();
        bal.setId(10L);
        bal.setTotalLeaves(20);
        bal.setUsedLeaves(2);
        bal.setRemainingLeaves(18);
        bal.setUser(emp);

        when(leaveRequestRepository.findById(100L)).thenReturn(Optional.of(lr));
        when(leaveBalanceRepository.findByUserId(1L)).thenReturn(bal);
        when(leaveRequestRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(leaveBalanceRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        LeaveRequest updated = leaveService.updateLeaveStatus(100L, "APPROVED");

        assertEquals("APPROVED", updated.getStatus());
        assertEquals(3, bal.getUsedLeaves());
        assertEquals(17, bal.getRemainingLeaves()); // 20 - 3
        verify(leaveBalanceRepository).save(bal);
        verify(leaveRequestRepository).save(updated);
    }

    @Test
    void reject_shouldNotChangeBalance() {
        User emp = new User();
        emp.setId(1L);
        emp.setUsername("emp1");
        emp.setPassword("x");
        emp.setRole(Role.EMPLOYEE);

        LeaveRequest lr = new LeaveRequest();
        lr.setId(101L);
        lr.setUser(emp);
        lr.setFromDate(LocalDate.of(2025, 9, 10));
        lr.setToDate(LocalDate.of(2025, 9, 10));
        lr.setStatus("PENDING");

        LeaveBalance bal = new LeaveBalance();
        bal.setId(10L);
        bal.setTotalLeaves(20);
        bal.setUsedLeaves(2);
        bal.setRemainingLeaves(18);
        bal.setUser(emp);

        when(leaveRequestRepository.findById(101L)).thenReturn(Optional.of(lr));
        when(leaveRequestRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        LeaveRequest updated = leaveService.updateLeaveStatus(101L, "REJECTED");

        assertEquals("REJECTED", updated.getStatus());
        assertEquals(2, bal.getUsedLeaves());
        assertEquals(18, bal.getRemainingLeaves());
        verify(leaveBalanceRepository, never()).save(any());
    }
}

