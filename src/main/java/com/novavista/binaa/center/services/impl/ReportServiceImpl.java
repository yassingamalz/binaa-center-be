package com.novavista.binaa.center.services.impl;

import com.novavista.binaa.center.dto.request.ReportDTO;
import com.novavista.binaa.center.dto.request.ReportGenerationRequestDTO;
import com.novavista.binaa.center.dto.response.ReportResultDTO;
import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.entity.Report;
import com.novavista.binaa.center.entity.Session;
import com.novavista.binaa.center.enums.ReportStatus;
import com.novavista.binaa.center.exceptions.ReportGenerationException;
import com.novavista.binaa.center.exceptions.ResourceNotFoundException;
import com.novavista.binaa.center.mapper.ReportMapper;
import com.novavista.binaa.center.repository.CaseRepository;
import com.novavista.binaa.center.repository.ReportRepository;
import com.novavista.binaa.center.repository.SessionRepository;
import com.novavista.binaa.center.services.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final CaseRepository caseRepository;
    private final SessionRepository sessionRepository;
    private final ReportMapper reportMapper;
    private final PDFService pdfService;


    @Autowired
    public ReportServiceImpl(
            ReportRepository reportRepository,
            CaseRepository caseRepository,
            SessionRepository sessionRepository,
            ReportMapper reportMapper,
            PDFService pdfService){
        this.reportRepository = reportRepository;
        this.caseRepository = caseRepository;
        this.sessionRepository = sessionRepository;
        this.reportMapper = reportMapper;
        this.pdfService = pdfService;
    }

    @Override
    public ReportResultDTO generateReport(ReportGenerationRequestDTO requestDTO) {
        log.info("Generating report for case ID: {}, type: {}",
                requestDTO.getCaseId(), requestDTO.getReportType());

        try {
            // Validate request and gather data
            Case caseEntity = validateAndGetCase(requestDTO.getCaseId());
            Session session = requestDTO.getSessionId() != null ?
                    validateAndGetSession(requestDTO.getSessionId()) : null;

            // Generate report content based on type
            String reportContent = generateReportContent(requestDTO, caseEntity, session);

            // Create and save report entity
            Report report = Report.builder()
                    .caseInfo(caseEntity)
                    .session(session)
                    .reportType(requestDTO.getReportType())
                    .reportContent(reportContent)
                    .createdDate(LocalDate.now())
                    .build();

            Report savedReport = reportRepository.save(report);

            // Map to result DTO with success status
            ReportResultDTO result = reportMapper.toResultDto(savedReport);
            result.setStatus(ReportStatus.GENERATED);

            log.info("Successfully generated report with ID: {}", savedReport.getReportId());
            return result;

        } catch (Exception e) {
            log.error("Failed to generate report: {}", e.getMessage());
            ReportResultDTO failedResult = new ReportResultDTO();
            failedResult.setStatus(ReportStatus.FAILED);
            failedResult.setErrorMessage(e.getMessage());
            return failedResult;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ReportResultDTO getReportById(Long id) {
        return reportRepository.findById(id)
                .map(reportMapper::toResultDto)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportResultDTO> getReportsByCase(Long caseId) {
        Case caseEntity = validateAndGetCase(caseId);
        List<Report> reports = reportRepository.findByCaseInfo(caseEntity);
        return reportMapper.toResultDtoList(reports);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportResultDTO> getReportsByFilters(
            Long caseId,
            String reportType,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable) {

        return reportRepository.findByFilters(caseId, reportType, startDate, endDate, pageable)
                .map(reportMapper::toResultDto);
    }

    @Override
    public ReportResultDTO updateReport(Long id, ReportDTO reportDTO) {
        log.info("Updating report ID: {}", id);
        Report existingReport = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        // Update fields
        existingReport.setReportContent(reportDTO.getReportContent());

        Report updatedReport = reportRepository.save(existingReport);
        return reportMapper.toResultDto(updatedReport);
    }

    @Override
    public void deleteReport(Long id) {
        log.info("Deleting report ID: {}", id);
        if (!reportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Report not found");
        }
        reportRepository.deleteById(id);
    }

    private Case validateAndGetCase(Long caseId) {
        return caseRepository.findById(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
    }

    private Session validateAndGetSession(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
    }

    private String generateReportContent(
            ReportGenerationRequestDTO requestDTO,
            Case caseEntity,
            Session session) {

        Map<String, Object> reportData = new HashMap<>();
        reportData.put("case", caseEntity);
        reportData.put("session", session);
        reportData.put("parameters", requestDTO.getParameters());

        return switch (requestDTO.getReportType()) {
            case PROGRESS -> generateProgressReport(reportData);
            case ASSESSMENT -> generateAssessmentReport(reportData);
            case ATTENDANCE -> generateAttendanceReport(reportData);
            default -> throw new ReportGenerationException("Unsupported report type: "
                    + requestDTO.getReportType());
        };
    }

    private String generateProgressReport(Map<String, Object> data) {
        Case caseInfo = (Case) data.get("case");
        StringBuilder report = new StringBuilder();
        report.append("تقرير التقدم\n");
        report.append("اسم الحالة: ").append(caseInfo.getName()).append("\n");
        // Add more report content...
        return report.toString();
    }

    private String generateAssessmentReport(Map<String, Object> data) {
        // Implement assessment report generation logic
        return "Assessment Report Content";
    }

    private String generateAttendanceReport(Map<String, Object> data) {
        // Implement attendance report generation logic
        return "Attendance Report Content";
    }

    @Override
    public byte[] generateRegistrationForm(Long caseId) {
        try {
            Case caseEntity = caseRepository.findById(caseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Case not found"));

            Map<String, String> formFields = new HashMap<>();
            formFields.put("caseID", String.valueOf(caseEntity.getCaseId()));
            formFields.put("childName", caseEntity.getName());
            formFields.put("birthDate", formatDate(caseEntity.getAdmissionDate()));
            formFields.put("guardianName", caseEntity.getGuardianName());
            formFields.put("phoneNumber", caseEntity.getContactNumber());
            formFields.put("gradeLevel", caseEntity.getGradeLevel());
            formFields.put("schoolName", caseEntity.getSchoolName());
            formFields.put("specialNeeds_1", caseEntity.getSpecialNeeds());
            formFields.put("medicalHistory_1", caseEntity.getMedicalHistory());
            formFields.put("admissionDate", formatDate(caseEntity.getAdmissionDate()));
            // Add any additional fields that match your PDF form fields

            return pdfService.fillFormTemplate("registration-form.pdf", formFields);

        } catch (IOException e) {
            log.error("Error generating registration form", e);
            throw new ReportGenerationException("Failed to generate registration form"+ e.getMessage());
        }
    }

    private String formatDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}