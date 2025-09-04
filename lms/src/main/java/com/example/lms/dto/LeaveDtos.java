package com.example.lms.dto;

import lombok.*;

@Getter @Setter
public class LeaveDtos {
    private String username;
    private String reason;
    private String fromDate;
    private String toDate;
}