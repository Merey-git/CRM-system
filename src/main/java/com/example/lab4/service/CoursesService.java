package com.example.lab4.service;

import com.example.lab4.model.Courses;
import com.example.lab4.repository.CoursesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoursesService {

    private final CoursesRepository repository;

    public CoursesService(CoursesRepository repository) {
        this.repository = repository;
    }

    public List<Courses> getAll() {
        return repository.findAll();
    }

    public Courses getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void save(Courses course) {
        repository.save(course);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}