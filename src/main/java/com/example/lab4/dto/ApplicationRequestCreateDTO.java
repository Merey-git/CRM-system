package com.example.lab4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequestCreateDTO {
    private String userName;
    private String commentary;
    private String phone;
    private Long courseId;
    private boolean handled;
}