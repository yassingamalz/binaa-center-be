package com.novavista.binaa.center.entity;

import com.novavista.binaa.center.enums.CaseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "cases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Case {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long caseId;
    private String name;
    private Integer age;
    private String guardianName;
    private String contactNumber;
    @Column(columnDefinition = "TEXT")
    private String specialNeeds;
    private LocalDate admissionDate;
    @Enumerated(EnumType.STRING)
    private CaseStatus status;
    private String emergencyContact;
    @Column(columnDefinition = "TEXT")
    private String medicalHistory;
    @Column(columnDefinition = "TEXT")
    private String currentMedications;
    @Column(columnDefinition = "TEXT")
    private String allergies;
    private String schoolName;
    private String gradeLevel;
    private String primaryDiagnosis;
    private String secondaryDiagnosis;
    @Column(columnDefinition = "TEXT")
    private String insuranceInfo;
}
