package com.example.lms.service;

import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.model.LeaveBalance;
import com.example.lms.model.LeaveRequest;
import com.example.lms.model.User;
import com.example.lms.repository.LeaveBalanceRepository;
import com.example.lms.repository.LeaveRequestRepository;
import com.example.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LeaveService {
    @Autowired private LeaveRequestRepository leaveRequestRepository;
    @Autowired private LeaveBalanceRepository leaveBalanceRepository;
    @Autowired private UserRepository userRepository;

    public LeaveRequest applyLeave(Long userId, String reason,String fromDate,String toDate) {
        User user = userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("Leave request not found with id"+userId));
        LeaveBalance balance = leaveBalanceRepository.findByUserId(userId);

        if (balance.getRemainingLeaves() <= 0) {
            throw new RuntimeException("No remaining leaves");
        }

        LeaveRequest leave = new LeaveRequest();
        leave.setReason(reason);
        leave.setStatus("PENDING");
        leave.setFromDate(LocalDate.parse(fromDate));
        leave.setToDate(LocalDate.parse(toDate));
        leave.setUser(user);
        return leaveRequestRepository.save(leave);
    }

    public List<LeaveRequest> getMyLeaves(Long id) {
        return leaveRequestRepository.findByUserId(id);
    }

    public LeaveBalance getLeaveBalance(Long userId) {
        return leaveBalanceRepository.findByUserId(userId);
    }

    public List<LeaveRequest> getAllRequests() {
        return leaveRequestRepository.findAll();
    }


public LeaveRequest updateLeaveStatus(Long id, String status) {
    LeaveRequest leave = leaveRequestRepository.findById(id).orElseThrow();
    leave.setStatus(status);

    if ("APPROVED".equalsIgnoreCase(status)) {
        LeaveBalance balance = leaveBalanceRepository.findByUserId(leave.getUser().getId());

        if (balance != null && balance.getRemainingLeaves() > 0) {
            balance.setUsedLeaves(balance.getUsedLeaves() + 1);
            balance.setRemainingLeaves(balance.getRemainingLeaves() - 1);
            leaveBalanceRepository.save(balance);
        }
//
    }

    return leaveRequestRepository.save(leave);
}
}
