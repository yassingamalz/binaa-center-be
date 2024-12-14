package com.novavista.binaa.center.controllers;

import com.novavista.binaa.center.dto.request.TreatmentPlanDTO;
import com.novavista.binaa.center.enums.TreatmentPlanStatus;
import com.novavista.binaa.center.services.TreatmentPlanService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treatment-plans")
@Slf4j
public class TreatmentPlanController {
    private final TreatmentPlanService treatmentPlanService;

    @Autowired
    public TreatmentPlanController(TreatmentPlanService treatmentPlanService) {
        this.treatmentPlanService = treatmentPlanService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<TreatmentPlanDTO> createTreatmentPlan(@Valid @RequestBody TreatmentPlanDTO planDTO) {
        log.info("Creating new treatment plan");
        return new ResponseEntity<>(treatmentPlanService.createTreatmentPlan(planDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<TreatmentPlanDTO> getTreatmentPlanById(@PathVariable Long id) {
        log.info("Fetching treatment plan with id: {}", id);
        return ResponseEntity.ok(treatmentPlanService.getTreatmentPlanById(id));
    }

    @GetMapping("/case/{caseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<TreatmentPlanDTO>> getTreatmentPlansByCase(@PathVariable Long caseId) {
        log.info("Fetching treatment plans for case: {}", caseId);
        return ResponseEntity.ok(treatmentPlanService.getTreatmentPlansByCase(caseId));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<TreatmentPlanDTO>> getTreatmentPlansByStatus(@PathVariable TreatmentPlanStatus status) {
        log.info("Fetching treatment plans with status: {}", status);
        return ResponseEntity.ok(treatmentPlanService.getTreatmentPlansByStatus(status));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TreatmentPlanDTO> updateTreatmentPlan(@PathVariable Long id, @Valid @RequestBody TreatmentPlanDTO planDTO) {
        log.info("Updating treatment plan with id: {}", id);
        return ResponseEntity.ok(treatmentPlanService.updateTreatmentPlan(id, planDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTreatmentPlan(@PathVariable Long id) {
        log.info("Deleting treatment plan with id: {}", id);
        treatmentPlanService.deleteTreatmentPlan(id);
        return ResponseEntity.noContent().build();
    }
}