package com.example.lab4.controller;

import com.example.lab4.model.ApplicationRequest;
import com.example.lab4.service.ApplicationRequestService;
import com.example.lab4.service.CoursesService;
import com.example.lab4.service.OperatorsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/requests")
public class ApplicationRequestController {

    private final ApplicationRequestService service;
    private final CoursesService coursesService;
    private final OperatorsService operatorsService;

    public ApplicationRequestController(ApplicationRequestService service,
                                        CoursesService coursesService,
                                        OperatorsService operatorsService) {
        this.service = service;
        this.coursesService = coursesService;
        this.operatorsService = operatorsService;
    }

    @GetMapping
    public String allRequests(Model model) {
        model.addAttribute("requests", service.getAll());
        return "requests";
    }

    @GetMapping("/new")
    public String pending(Model model) {
        model.addAttribute("requests", service.getHandled(false));
        return "new";
    }

    @GetMapping("/processed")
    public String processed(Model model) {
        model.addAttribute("requests", service.getHandled(true));
        return "processed";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("request", new ApplicationRequest());
        model.addAttribute("courses", coursesService.getAll());
        return "add-request";
    }

    @PostMapping("/add")
    public String saveRequest(@ModelAttribute ApplicationRequest request,
                              @RequestParam Long courseId) {
        request.setHandled(false);
        request.setCourse(coursesService.getById(courseId));
        service.save(request);
        return "redirect:/requests/new";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        model.addAttribute("request", service.getById(id));
        return "details";
    }

    @GetMapping("/{id}/assign")
    public String assignOperatorsForm(@PathVariable Long id, Model model) {
        ApplicationRequest request = service.getById(id);
        if (request == null || request.isHandled()) {
            return "redirect:/requests/" + id;
        }
        model.addAttribute("request", request);
        model.addAttribute("operators", operatorsService.getAll());
        return "assign-operators";
    }

    @PostMapping("/{id}/assign")
    public String assignOperators(@PathVariable Long id,
                                  @RequestParam(required = false) List<Long> operatorIds) {
        if (operatorIds != null && !operatorIds.isEmpty()) {
            service.assignOperators(id, operatorIds, operatorsService);
        }
        return "redirect:/requests/" + id;
    }

    @PostMapping("/{id}/removeOperator")
    public String removeOperator(@PathVariable Long id,
                                 @RequestParam Long operatorId) {
        service.removeOperator(id, operatorId);
        return "redirect:/requests/" + id;
    }

    @PostMapping("/{id}/markAsHandled")
    public String markAsHandled(@PathVariable Long id) {
        service.markAsHandled(id);
        return "redirect:/requests/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/requests";
    }
}