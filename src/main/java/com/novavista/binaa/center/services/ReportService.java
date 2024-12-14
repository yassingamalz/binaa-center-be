package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.request.ReportDTO;

import java.util.List;

public interface ReportService {
    ReportDTO createReport(ReportDTO reportDTO);
    ReportDTO getReportById(Long id);
    List<ReportDTO> getReportsByCase(Long caseId);
    ReportDTO updateReport(Long id, ReportDTO reportDTO);
    void deleteReport(Long id);
}
