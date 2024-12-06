package com.novavista.binaa.center.dto;

import com.novavista.binaa.center.enums.AssessmentStatus;
import com.novavista.binaa.center.enums.AssessmentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentDTO {
    private Long assessmentId;
    private Long caseId;
    private AssessmentType assessmentType;
    private BigDecimal score;
    private LocalDate assessmentDate;
    private Long assessorId;
    private AssessmentStatus status;
}