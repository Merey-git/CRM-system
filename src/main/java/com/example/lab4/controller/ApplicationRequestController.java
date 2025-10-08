package com.example.lab4.controller;

import com.example.lab4.model.ApplicationRequest;
import com.example.lab4.service.ApplicationRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/requests")
public class ApplicationRequestController {

    private final ApplicationRequestService service;

    public ApplicationRequestController(ApplicationRequestService service) {
        this.service = service;
    }

    @GetMapping
    public String allRequests(Model model) {
        model.addAttribute("requests", service.getAll());
        return "requests";
    }

    @GetMapping("/new")
    public String pending(Model model) {
        model.addAttribute("requests", service.getHandled(false));
        return "new"; //
    }

    @GetMapping("/processed")
    public String processed(Model model) {
        model.addAttribute("requests", service.getHandled(true));
        return "processed";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("request", new ApplicationRequest());
        return "add-request";
    }

    @PostMapping("/add")
    public String saveRequest(@ModelAttribute ApplicationRequest request) {
        request.setHandled(false);
        service.save(request);
        return "redirect:/requests/new";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        model.addAttribute("request", service.getById(id));
        return "details";
    }

    @PostMapping("/{id}/process")
    public String process(@PathVariable Long id) {
        service.markAsHandled(id);
        return "redirect:/requests/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/requests";
    }
}
