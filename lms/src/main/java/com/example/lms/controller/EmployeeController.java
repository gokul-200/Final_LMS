package com.example.lms.controller;

import com.example.lms.dto.LeaveDtos;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.model.LeaveRequest;
import com.example.lms.model.User;
import com.example.lms.repository.UserRepository;
import com.example.lms.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin
public class EmployeeController {

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/apply-leave")
    public LeaveRequest applyLeave(@RequestBody LeaveDtos dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(()->
                        new ResourceNotFoundException("Leave request not found with username"+dto.getUsername()));
        return leaveService.applyLeave(user.getId(), dto.getReason(),dto.getFromDate(), dto.getToDate());
    }

    @GetMapping("/my-leaves")
    public List<LeaveRequest> myLeaves(@RequestParam String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->
                        new ResourceNotFoundException("Leave request not found with username"+username));
//        System.out.println(user);
        return leaveService.getMyLeaves(user.getId());
    }


    @GetMapping("/leave-balance")
    public Object leaveBalance(@RequestParam String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->
                        new ResourceNotFoundException("Leave request not found with username"+username));
        return leaveService.getLeaveBalance(user.getId());
    }
}

