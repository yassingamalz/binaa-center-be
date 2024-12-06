package com.novavista.binaa.center.entity;

import com.novavista.binaa.center.enums.AssessmentStatus;
import com.novavista.binaa.center.enums.AssessmentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "assessments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assessmentId;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseInfo;

    @Enumerated(EnumType.STRING)
    private AssessmentType assessmentType;

    private BigDecimal score;
    private LocalDate assessmentDate;
    private LocalDate nextAssessmentDate;

    @ManyToOne
    @JoinColumn(name = "assessor_id")
    private Staff assessor;

    @Column(columnDefinition = "TEXT")
    private String recommendations;

    @Enumerated(EnumType.STRING)
    private AssessmentStatus status;
}