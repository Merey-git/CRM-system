package com.example.lab4.repository;

import com.example.lab4.model.Operators;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperatorsRepository extends JpaRepository<Operators, Long> {
    List<Operators> findByDepartment(String department);
}