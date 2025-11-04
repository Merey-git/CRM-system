package com.example.lab4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequestDTO {
    private Long id;
    private String userName;
    private String commentary;
    private String phone;
    private boolean handled;
    private Long courseId;
    private String courseName;
    private List<OperatorDTO> operators;
}