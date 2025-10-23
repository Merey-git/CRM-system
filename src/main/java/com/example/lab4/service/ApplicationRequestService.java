package com.example.lab4.service;

import com.example.lab4.model.ApplicationRequest;
import com.example.lab4.model.Operators;
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

    public void assignOperators(Long requestId, List<Long> operatorIds, OperatorsService operatorsService) {
        ApplicationRequest request = getById(requestId);
        if (request != null && !request.isHandled()) {
            request.getOperators().clear();
            for (Long operatorId : operatorIds) {
                Operators operator = operatorsService.getById(operatorId);
                if (operator != null) {
                    request.getOperators().add(operator);
                }
            }
            request.setHandled(true);
            repository.save(request);
        }
    }

    public void removeOperator(Long requestId, Long operatorId) {
        ApplicationRequest request = getById(requestId);
        if (request != null) {
            request.getOperators().removeIf(op -> op.getId().equals(operatorId));
            repository.save(request);
        }
    }

    public void markAsHandled(Long id) {
        ApplicationRequest request = getById(id);
        if (request != null) {
            request.setHandled(true);
            repository.save(request);
        }
    }
}