package com.novavista.binaa.center.dto.request;

import com.novavista.binaa.center.enums.TreatmentPlanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentPlanDTO {
    private Long planId;
    private Long caseId;
    private String goals;
    private LocalDate startDate;
    private LocalDate endDate;
    private TreatmentPlanStatus status;
    private Long specialistId;
}