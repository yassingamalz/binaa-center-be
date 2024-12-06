package com.novavista.binaa.center.entity;

import com.novavista.binaa.center.enums.PointsStatus;
import com.novavista.binaa.center.enums.PointsTransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fidelity_points")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FidelityPoints {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointId;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseInfo;

    private Integer points;
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    private PointsTransactionType type;

    private String source;
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    private PointsStatus status;
}
