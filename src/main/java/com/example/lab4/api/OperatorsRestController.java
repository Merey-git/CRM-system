package com.example.lab4.api;

import com.example.lab4.dto.ApplicationRequestDTO;
import com.example.lab4.dto.OperatorDTO;
import com.example.lab4.model.ApplicationRequest;
import com.example.lab4.model.Operators;
import com.example.lab4.service.ApplicationRequestService;
import com.example.lab4.service.OperatorsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/operators")
public class OperatorsRestController {

    private final OperatorsService operatorsService;
    private final ApplicationRequestService applicationRequestService;

    public OperatorsRestController(OperatorsService operatorsService,
                                   ApplicationRequestService applicationRequestService) {
        this.operatorsService = operatorsService;
        this.applicationRequestService = applicationRequestService;
    }

    @GetMapping
    public ResponseEntity<List<OperatorDTO>> getAllOperators() {
        List<Operators> operators = operatorsService.getAll();

        if (operators.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<OperatorDTO> operatorDTOs = operators.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(operatorDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperatorDTO> getOperatorById(@PathVariable Long id) {
        Operators operator = operatorsService.getById(id);

        if (operator == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(convertToDTO(operator));
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<OperatorDTO>> getOperatorsByDepartment(@PathVariable String department) {
        List<Operators> operators = operatorsService.getByDepartment(department);

        if (operators.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<OperatorDTO> operatorDTOs = operators.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(operatorDTOs);
    }

    @PostMapping
    public ResponseEntity<OperatorDTO> createOperator(@RequestBody OperatorDTO operatorDTO) {
        Operators operator = new Operators();
        operator.setName(operatorDTO.getName());
        operator.setSurname(operatorDTO.getSurname());
        operator.setDepartment(operatorDTO.getDepartment());

        operatorsService.save(operator);

        return new ResponseEntity<>(convertToDTO(operator), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OperatorDTO> updateOperator(@PathVariable Long id,
                                                      @RequestBody OperatorDTO operatorDTO) {
        Operators existingOperator = operatorsService.getById(id);

        if (existingOperator == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existingOperator.setName(operatorDTO.getName());
        existingOperator.setSurname(operatorDTO.getSurname());
        existingOperator.setDepartment(operatorDTO.getDepartment());

        operatorsService.save(existingOperator);

        return ResponseEntity.ok(convertToDTO(existingOperator));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOperator(@PathVariable Long id) {
        Operators operator = operatorsService.getById(id);

        if (operator == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        operatorsService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{operatorId}/assign/{requestId}")
    public ResponseEntity<ApplicationRequestDTO> assignOperatorToRequest(
            @PathVariable Long operatorId,
            @PathVariable Long requestId) {

        Operators operator = operatorsService.getById(operatorId);
        if (operator == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ApplicationRequest request = applicationRequestService.getById(requestId);
        if (request == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (request.getOperators().contains(operator)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        request.getOperators().add(operator);
        if (!request.isHandled()) {
            request.setHandled(true);
        }
        applicationRequestService.save(request);
        return ResponseEntity.ok(convertRequestToDTO(request));
    }


    private ApplicationRequestDTO convertRequestToDTO(ApplicationRequest request) {
        ApplicationRequestDTO dto = new ApplicationRequestDTO();
        dto.setId(request.getId());
        dto.setUserName(request.getUserName());
        dto.setCommentary(request.getCommentary());
        dto.setPhone(request.getPhone());
        dto.setHandled(request.isHandled());

        if (request.getCourse() != null) {
            dto.setCourseId(request.getCourse().getId());
            dto.setCourseName(request.getCourse().getName());
        }

        if (request.getOperators() != null) {
            List<OperatorDTO> operatorDTOs = request.getOperators().stream()
                    .map(op -> new OperatorDTO(
                            op.getId(),
                            op.getName(),
                            op.getSurname(),
                            op.getDepartment()
                    ))
                    .collect(Collectors.toList());
            dto.setOperators(operatorDTOs);
        }

        return dto;
    }

    private OperatorDTO convertToDTO(Operators operator) {
        OperatorDTO dto = new OperatorDTO();
        dto.setId(operator.getId());
        dto.setName(operator.getName());
        dto.setSurname(operator.getSurname());
        dto.setDepartment(operator.getDepartment());
        return dto;
    }
}