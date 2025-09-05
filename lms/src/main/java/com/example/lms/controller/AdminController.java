package com.example.lms.controller;

import com.example.lms.model.LeaveRequest;
import com.example.lms.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private LeaveService leaveService;

    @GetMapping("/leave-requests")
    public List<LeaveRequest> getAllLeaveRequests() {

        return leaveService.getAllRequests();
    }

    @PostMapping("/leave/{id}/{status}")
    public LeaveRequest updateLeaveStatus(@PathVariable Long id, @PathVariable String status) {
        return leaveService.updateLeaveStatus(id, status);
    }
}

