package com.novavista.binaa.center.dto.response;

import com.novavista.binaa.center.enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResultDTO {
    private Long reportId;
    private Long caseId;
    private Long sessionId;
    private String reportType;
    private String reportContent;
    private LocalDate createdDate;
    private String fileName;
    private String contentType;
    private Long fileSize;
    private ReportStatus status;
    private String errorMessage;
}