package com.example.lms.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LeaveBalance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalLeaves;
    private int usedLeaves;
    private int remainingLeaves;

    @OneToOne
    private User user;
}
