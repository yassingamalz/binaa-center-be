package com.novavista.binaa.center.controllers;

import com.novavista.binaa.center.dto.CaseDTO;
import com.novavista.binaa.center.services.CaseService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cases")
@Slf4j
public class CaseController {
    private final CaseService caseService;

    @Autowired
    public CaseController(CaseService caseService) {
        this.caseService = caseService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<CaseDTO>> getAllCases() {
        log.info("Fetching all cases");
        return ResponseEntity.ok(caseService.getAllCases());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<CaseDTO> createCase(@Valid @RequestBody CaseDTO caseDTO) {
        log.info("Creating new case");
        return new ResponseEntity<>(caseService.createCase(caseDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<CaseDTO> getCaseById(@PathVariable Long id) {
        log.info("Fetching case with id: {}", id);
        return ResponseEntity.ok(caseService.getCaseById(id));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<CaseDTO>> getAllActiveCases() {
        log.info("Fetching all active cases");
        return ResponseEntity.ok(caseService.getAllActiveCases());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<CaseDTO>> searchCases(@RequestParam String name) {
        log.info("Searching cases with name: {}", name);
        return ResponseEntity.ok(caseService.searchCasesByName(name));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<CaseDTO>> getCasesByStatus(@PathVariable String status) {
        log.info("Fetching cases with status: {}", status);
        return ResponseEntity.ok(caseService.getCasesByStatus(status));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CaseDTO> updateCase(@PathVariable Long id, @Valid @RequestBody CaseDTO caseDTO) {
        log.info("Updating case with id: {}", id);
        return ResponseEntity.ok(caseService.updateCase(id, caseDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCase(@PathVariable Long id) {
        log.info("Deleting case with id: {}", id);
        caseService.deleteCase(id);
        return ResponseEntity.noContent().build();
    }
}