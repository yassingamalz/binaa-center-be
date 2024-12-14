package com.novavista.binaa.center.services.impl;

import com.novavista.binaa.center.dto.request.ReportDTO;
import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.entity.Report;
import com.novavista.binaa.center.entity.Session;
import com.novavista.binaa.center.exceptions.ResourceNotFoundException;
import com.novavista.binaa.center.exceptions.ValidationException;
import com.novavista.binaa.center.repository.CaseRepository;
import com.novavista.binaa.center.repository.ReportRepository;
import com.novavista.binaa.center.repository.SessionRepository;
import com.novavista.binaa.center.services.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final CaseRepository caseRepository;
    private final SessionRepository sessionRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository,
                             CaseRepository caseRepository,
                             SessionRepository sessionRepository,
                             ModelMapper modelMapper) {
        this.reportRepository = reportRepository;
        this.caseRepository = caseRepository;
        this.sessionRepository = sessionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ReportDTO createReport(ReportDTO reportDTO) {
        log.info("Creating report for case ID: {}", reportDTO.getCaseId());
        validateReport(reportDTO);

        Case caseEntity = caseRepository.findById(reportDTO.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));

        Session session = null;
        if (reportDTO.getSessionId() != null) {
            session = sessionRepository.findById(reportDTO.getSessionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        }

        Report report = modelMapper.map(reportDTO, Report.class);
        report.setCaseInfo(caseEntity);
        report.setSession(session);
        report.setCreatedDate(LocalDate.now());

        Report savedReport = reportRepository.save(report);
        log.info("Created report with ID: {}", savedReport.getReportId());
        return modelMapper.map(savedReport, ReportDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDTO getReportById(Long id) {
        return reportRepository.findById(id)
                .map(report -> modelMapper.map(report, ReportDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportDTO> getReportsByCase(Long caseId) {
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
        return reportRepository.findByCaseInfo(caseEntity).stream()
                .map(report -> modelMapper.map(report, ReportDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ReportDTO updateReport(Long id, ReportDTO reportDTO) {
        log.info("Updating report ID: {}", id);
        Report existingReport = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        validateReport(reportDTO);
        modelMapper.map(reportDTO, existingReport);
        existingReport.setReportId(id);

        Report updatedReport = reportRepository.save(existingReport);
        log.info("Updated report ID: {}", id);
        return modelMapper.map(updatedReport, ReportDTO.class);
    }

    @Override
    public void deleteReport(Long id) {
        log.info("Deleting report ID: {}", id);
        try {
            reportRepository.deleteById(id);
            log.info("Deleted report ID: {}", id);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to delete report: {}", e.getMessage());
            throw new RuntimeException("Cannot delete report");
        }
    }

    private void validateReport(ReportDTO reportDTO) {
        if (reportDTO.getReportContent() == null || reportDTO.getReportContent().trim().isEmpty()) {
            throw new ValidationException("Report content is required");
        }
    }
}