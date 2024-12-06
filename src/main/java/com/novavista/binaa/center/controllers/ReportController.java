package com.novavista.binaa.center.controllers;

import com.novavista.binaa.center.dto.ReportDTO;
import com.novavista.binaa.center.services.ReportService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/reports")
@Slf4j
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ReportDTO> createReport(@Valid @RequestBody ReportDTO reportDTO) {
        log.info("Creating new report");
        return new ResponseEntity<>(reportService.createReport(reportDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ReportDTO> getReportById(@PathVariable Long id) {
        log.info("Fetching report with id: {}", id);
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @GetMapping("/case/{caseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<ReportDTO>> getReportsByCase(@PathVariable Long caseId) {
        log.info("Fetching reports for case: {}", caseId);
        return ResponseEntity.ok(reportService.getReportsByCase(caseId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ReportDTO> updateReport(@PathVariable Long id, @Valid @RequestBody ReportDTO reportDTO) {
        log.info("Updating report with id: {}", id);
        return ResponseEntity.ok(reportService.updateReport(id, reportDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        log.info("Deleting report with id: {}", id);
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
