package com.novavista.binaa.center.entity;

import com.novavista.binaa.center.enums.ReportType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne
    @JoinColumn(name = "case_id", nullable = false)
    private Case caseInfo;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String reportContent;

    @Column(nullable = false)
    private LocalDate createdDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType reportType;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(length = 50)
    private String templateId;
}