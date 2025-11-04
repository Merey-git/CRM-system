package com.example.lab4.api;

import com.example.lab4.dto.*;
import com.example.lab4.model.ApplicationRequest;
import com.example.lab4.model.Courses;
import com.example.lab4.service.ApplicationRequestService;
import com.example.lab4.service.CoursesService;
import com.example.lab4.service.OperatorsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/requests")
public class ApplicationRequestRestController {

    private final ApplicationRequestService requestService;
    private final CoursesService coursesService;
    private final OperatorsService operatorsService;

    public ApplicationRequestRestController(ApplicationRequestService requestService,
                                            CoursesService coursesService,
                                            OperatorsService operatorsService) {
        this.requestService = requestService;
        this.coursesService = coursesService;
        this.operatorsService = operatorsService;
    }


    @GetMapping
    public ResponseEntity<List<ApplicationRequestDTO>> getAllRequests() {
        List<ApplicationRequest> requests = requestService.getAll();

        if (requests.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<ApplicationRequestDTO> requestDTOs = requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(requestDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationRequestDTO> getRequestById(@PathVariable Long id) {
        ApplicationRequest request = requestService.getById(id);

        if (request == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(convertToDTO(request));
    }

    @GetMapping("/new")
    public ResponseEntity<List<ApplicationRequestDTO>> getNewRequests() {
        List<ApplicationRequest> requests = requestService.getHandled(false);

        if (requests.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<ApplicationRequestDTO> requestDTOs = requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(requestDTOs);
    }

    @GetMapping("/processed")
    public ResponseEntity<List<ApplicationRequestDTO>> getProcessedRequests() {
        List<ApplicationRequest> requests = requestService.getHandled(true);

        if (requests.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<ApplicationRequestDTO> requestDTOs = requests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(requestDTOs);
    }

    @PostMapping
    public ResponseEntity<ApplicationRequestDTO> createRequest(@RequestBody ApplicationRequestCreateDTO createDTO) {
        Courses course = coursesService.getById(createDTO.getCourseId());
        if (course == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ApplicationRequest request = new ApplicationRequest();
        request.setUserName(createDTO.getUserName());
        request.setCommentary(createDTO.getCommentary());
        request.setPhone(createDTO.getPhone());
        request.setCourse(course);
        request.setHandled(createDTO.isHandled());

        requestService.save(request);

        return new ResponseEntity<>(convertToDTO(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationRequestDTO> updateRequest(@PathVariable Long id,
                                                               @RequestBody ApplicationRequestCreateDTO updateDTO) {
        ApplicationRequest existingRequest = requestService.getById(id);

        if (existingRequest == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Courses course = coursesService.getById(updateDTO.getCourseId());
        if (course == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        existingRequest.setUserName(updateDTO.getUserName());
        existingRequest.setCommentary(updateDTO.getCommentary());
        existingRequest.setPhone(updateDTO.getPhone());
        existingRequest.setCourse(course);
        existingRequest.setHandled(updateDTO.isHandled());

        requestService.save(existingRequest);

        return ResponseEntity.ok(convertToDTO(existingRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        ApplicationRequest request = requestService.getById(id);

        if (request == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        requestService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/assign-operators")
    public ResponseEntity<ApplicationRequestDTO> assignOperators(@PathVariable Long id,
                                                                 @RequestBody AssignOperatorsDTO assignDTO) {
        ApplicationRequest request = requestService.getById(id);

        if (request == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        requestService.assignOperators(id, assignDTO.getOperatorIds(), operatorsService);

        ApplicationRequest updatedRequest = requestService.getById(id);
        return ResponseEntity.ok(convertToDTO(updatedRequest));
    }

    @DeleteMapping("/{requestId}/operators/{operatorId}")
    public ResponseEntity<ApplicationRequestDTO> removeOperator(@PathVariable Long requestId,
                                                                @PathVariable Long operatorId) {
        ApplicationRequest request = requestService.getById(requestId);

        if (request == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        requestService.removeOperator(requestId, operatorId);

        ApplicationRequest updatedRequest = requestService.getById(requestId);
        return ResponseEntity.ok(convertToDTO(updatedRequest));
    }

    @PutMapping("/{id}/mark-handled")
    public ResponseEntity<ApplicationRequestDTO> markAsHandled(@PathVariable Long id) {
        ApplicationRequest request = requestService.getById(id);

        if (request == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        requestService.markAsHandled(id);

        ApplicationRequest updatedRequest = requestService.getById(id);
        return ResponseEntity.ok(convertToDTO(updatedRequest));
    }

    private ApplicationRequestDTO convertToDTO(ApplicationRequest request) {
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
}