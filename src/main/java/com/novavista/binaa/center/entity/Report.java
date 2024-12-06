package com.novavista.binaa.center.entity;

import com.novavista.binaa.center.enums.TreatmentPlanStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseInfo;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    @Column(columnDefinition = "TEXT")
    private String reportContent;

    private LocalDate createdDate;
}