package com.example.lms.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LeaveRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reason;
    private String status; // PENDING, APPROVED, REJECTED

    private LocalDate fromDate;
    private LocalDate toDate;

    @ManyToOne
    private User user;
}