package com.example.lab4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperatorDTO {
    private Long id;
    private String name;
    private String surname;
    private String department;

    public String getFullName() {
        return name + " " + surname;
    }
}