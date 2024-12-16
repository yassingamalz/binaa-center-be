package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.request.ReportDTO;
import com.novavista.binaa.center.dto.request.ReportGenerationRequestDTO;
import com.novavista.binaa.center.dto.response.ReportResultDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    ReportResultDTO generateReport(ReportGenerationRequestDTO requestDTO);

    ReportResultDTO getReportById(Long id);

    List<ReportResultDTO> getReportsByCase(Long caseId);

    Page<ReportResultDTO> getReportsByFilters(Long caseId, String reportType,
                                              LocalDate startDate, LocalDate endDate, Pageable pageable);

    ReportResultDTO updateReport(Long id, ReportDTO reportDTO);

    byte[] generateRegistrationForm(Long caseId);

    void deleteReport(Long id);
}