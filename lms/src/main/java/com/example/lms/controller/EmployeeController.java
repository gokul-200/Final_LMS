//package com.example.lms.controller;
//
//import com.example.lms.dto.LeaveDtos;
//import com.example.lms.model.LeaveBalance;
//import com.example.lms.model.LeaveRequest;
//import com.example.lms.model.User;
//import com.example.lms.repository.UserRepository;
//import com.example.lms.service.LeaveService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/employee")
//@CrossOrigin
//public class EmployeeController {
//    @Autowired private LeaveService leaveService;
//    @Autowired private UserRepository userRepository;
//
//    @PostMapping("/apply-leave")
//    public LeaveRequest applyLeave( @RequestBody LeaveDtos dto) {
////        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
////        return leaveService.applyLeave(user.getId(), dto.getReason());
//        User user=userRepository.findByUsername(dto.getUsername()).orElseThrow();
//        return leaveService.applyLeave(user.getId(), dto.getReason());
//    }
//
//    @GetMapping("/my-leaves")
//    public List<LeaveRequest> myLeaves(@AuthenticationPrincipal UserDetails userDetails) {
//        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
//        return leaveService.getMyLeaves(user.getId());
//    }
//
//    @GetMapping("/leave-balance")
//    public LeaveBalance myBalance(@AuthenticationPrincipal UserDetails userDetails) {
//        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
//        return leaveService.getLeaveBalance(user.getId());
//    }
//}

package com.example.lms.controller;

import com.example.lms.dto.LeaveDtos;
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
                .orElseThrow(() -> new RuntimeException("User not found"));
        return leaveService.applyLeave(user.getId(), dto.getReason(),dto.getFromDate(), dto.getToDate());
    }

    @GetMapping("/my-leaves")
    public List<LeaveRequest> myLeaves(@RequestParam String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
//        System.out.println(user);
        return leaveService.getMyLeaves(user.getId());
    }


    @GetMapping("/leave-balance")
    public Object leaveBalance(@RequestParam String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return leaveService.getLeaveBalance(user.getId());
    }
}

