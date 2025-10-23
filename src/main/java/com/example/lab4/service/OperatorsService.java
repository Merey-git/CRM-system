package com.example.lab4.service;

import com.example.lab4.model.Operators;
import com.example.lab4.repository.OperatorsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperatorsService {

    private final OperatorsRepository repository;

    public OperatorsService(OperatorsRepository repository) {
        this.repository = repository;
    }

    public List<Operators> getAll() {
        return repository.findAll();
    }

    public Operators getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Operators> getByDepartment(String department) {
        return repository.findByDepartment(department);
    }

    public void save(Operators operator) {
        repository.save(operator);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}