package com.novavista.binaa.center.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private Long reportId;
    private Long caseId;
    private Long sessionId;
    private String reportContent;
    private LocalDate createdDate;
}



