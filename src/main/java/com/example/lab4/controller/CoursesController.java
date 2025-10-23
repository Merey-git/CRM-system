package com.example.lab4.controller;

import com.example.lab4.model.Courses;
import com.example.lab4.service.CoursesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/courses")
public class CoursesController {

    private final CoursesService service;

    public CoursesController(CoursesService service) {
        this.service = service;
    }

    @GetMapping
    public String allCourses(Model model) {
        model.addAttribute("courses", service.getAll());
        return "courses";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("course", new Courses());
        return "add-course";
    }

    @PostMapping("/add")
    public String saveCourse(@ModelAttribute("course") Courses course) {
        service.save(course);
        return "redirect:/courses";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/courses";
    }
}
