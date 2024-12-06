package com.novavista.binaa.center.entity;

import com.novavista.binaa.center.enums.TreatmentPlanStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "treatment_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseInfo;

    @Column(columnDefinition = "TEXT")
    private String goals;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private TreatmentPlanStatus status;

    @Column(columnDefinition = "TEXT")
    private String progressNotes;

    @ManyToOne
    @JoinColumn(name = "specialist_id")
    private Staff specialist;
}
























