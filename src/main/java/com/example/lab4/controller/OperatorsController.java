package com.example.lab4.controller;

import com.example.lab4.model.Operators;
import com.example.lab4.service.OperatorsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/operators")
public class OperatorsController {

    private final OperatorsService service;

    public OperatorsController(OperatorsService service) {
        this.service = service;
    }

    @GetMapping
    public String allOperators(Model model) {
        model.addAttribute("operators", service.getAll());
        return "operators";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("operator", new Operators());
        return "add-operator";
    }

    @PostMapping("/add")
    public String saveOperator(@ModelAttribute Operators operator) {
        service.save(operator);
        return "redirect:/operators";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/operators";
    }
}