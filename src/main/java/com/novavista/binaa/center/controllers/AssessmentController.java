package com.novavista.binaa.center.controllers;

import com.novavista.binaa.center.dto.AssessmentDTO;
import com.novavista.binaa.center.enums.AssessmentStatus;
import com.novavista.binaa.center.services.AssessmentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/assessments")
@Slf4j
public class AssessmentController {
    private final AssessmentService assessmentService;

    @Autowired
    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<AssessmentDTO> createAssessment(@Valid @RequestBody AssessmentDTO assessmentDTO) {
        log.info("Creating new assessment");
        return new ResponseEntity<>(assessmentService.createAssessment(assessmentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<AssessmentDTO> getAssessmentById(@PathVariable Long id) {
        log.info("Fetching assessment with id: {}", id);
        return ResponseEntity.ok(assessmentService.getAssessmentById(id));
    }

    @GetMapping("/case/{caseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<AssessmentDTO>> getAssessmentsByCase(@PathVariable Long caseId) {
        log.info("Fetching assessments for case: {}", caseId);
        return ResponseEntity.ok(assessmentService.getAssessmentsByCase(caseId));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<AssessmentDTO>> getAssessmentsByStatus(@PathVariable AssessmentStatus status) {
        log.info("Fetching assessments with status: {}", status);
        return ResponseEntity.ok(assessmentService.getAssessmentsByStatus(status));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<AssessmentDTO> updateAssessment(@PathVariable Long id, @Valid @RequestBody AssessmentDTO assessmentDTO) {
        log.info("Updating assessment with id: {}", id);
        return ResponseEntity.ok(assessmentService.updateAssessment(id, assessmentDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long id) {
        log.info("Deleting assessment with id: {}", id);
        assessmentService.deleteAssessment(id);
        return ResponseEntity.noContent().build();
    }
}
