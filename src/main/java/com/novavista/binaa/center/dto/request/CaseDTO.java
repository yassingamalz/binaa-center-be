package com.novavista.binaa.center.dto.request;

import com.novavista.binaa.center.enums.CaseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseDTO {
    private Long caseId;
    private String name;
    private Integer age;
    private String guardianName;
    private String contactNumber;
    private String specialNeeds;
    private LocalDate admissionDate;
    private CaseStatus status;
    private String emergencyContact;
}

