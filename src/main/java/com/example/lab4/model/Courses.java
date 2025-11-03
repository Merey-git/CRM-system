package com.example.lab4.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Courses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private int price;

    @OneToMany(mappedBy = "course")
    @JsonIgnoreProperties("course")
    private List<ApplicationRequest> requests = new ArrayList<>();

    public Courses(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}