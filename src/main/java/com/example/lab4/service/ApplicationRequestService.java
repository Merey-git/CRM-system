package com.example.lab4.service;

import com.example.lab4.model.ApplicationRequest;
import com.example.lab4.repository.ApplicationRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationRequestService {

    private final ApplicationRequestRepository repository;

    public ApplicationRequestService(ApplicationRequestRepository repository) {
        this.repository = repository;
    }

    public List<ApplicationRequest> getAll() {
        return repository.findAll();
    }

    public List<ApplicationRequest> getHandled(boolean handled) {
        return repository.findByHandled(handled);
    }

    public ApplicationRequest getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void save(ApplicationRequest request) {
        repository.save(request);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public void markAsHandled(Long id) {
        ApplicationRequest req = getById(id);
        if (req != null) {
            req.setHandled(true);
            repository.save(req);
        }
    }
}
