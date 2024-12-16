package com.novavista.binaa.center.controllers;

import com.novavista.binaa.center.dto.request.ReportDTO;
import com.novavista.binaa.center.dto.request.ReportGenerationRequestDTO;
import com.novavista.binaa.center.dto.response.ReportResultDTO;
import com.novavista.binaa.center.services.ReportService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @PostMapping("/generate")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ReportResultDTO> generateReport(@Valid @RequestBody ReportGenerationRequestDTO request) {
        log.info("Generating report for case ID: {}, type: {}", request.getCaseId(), request.getReportType());
        return new ResponseEntity<>(reportService.generateReport(request), HttpStatus.CREATED);
    }

//    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
//    public ResponseEntity<ReportDTO> createReport(@Valid @RequestBody ReportDTO reportDTO) {
//        log.info("Creating new report");
//        return new ResponseEntity<>(reportService.createReport(reportDTO), HttpStatus.CREATED);
//    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ReportResultDTO> getReportById(@PathVariable Long id) {
        log.info("Fetching report with id: {}", id);
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @GetMapping("/case/{caseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<ReportResultDTO>> getReportsByCase(@PathVariable Long caseId) {
        log.info("Fetching reports for case: {}", caseId);
        return ResponseEntity.ok(reportService.getReportsByCase(caseId));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Page<ReportResultDTO>> searchReports(
            @RequestParam(required = false) Long caseId,
            @RequestParam(required = false) String reportType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Pageable pageable) {

        log.info("Searching reports with filters - caseId: {}, type: {}, dateRange: {} to {}",
                caseId, reportType, startDate, endDate);

        return ResponseEntity.ok(reportService.getReportsByFilters(caseId, reportType, startDate, endDate, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ReportResultDTO> updateReport(@PathVariable Long id, @Valid @RequestBody ReportDTO reportDTO) {
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

    @GetMapping("/cases/{caseId}/registration-form")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<byte[]> generateRegistrationForm(@PathVariable Long caseId) {
        byte[] pdfContent = reportService.generateRegistrationForm(caseId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition
                .builder("attachment")
                .filename("registration-form.pdf")
                .build());

        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }
}