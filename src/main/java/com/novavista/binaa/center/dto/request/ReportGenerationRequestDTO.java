package com.novavista.binaa.center.dto.request;

import com.novavista.binaa.center.enums.ReportType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportGenerationRequestDTO {
    @NotNull
    private Long caseId;
    private Long sessionId;
    @NotNull
    private ReportType reportType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String templateId;
    private Map<String, Object> parameters;
}