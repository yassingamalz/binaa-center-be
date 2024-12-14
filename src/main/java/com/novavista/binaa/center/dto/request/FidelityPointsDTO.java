package com.novavista.binaa.center.dto.request;

import com.novavista.binaa.center.enums.PointsStatus;
import com.novavista.binaa.center.enums.PointsTransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FidelityPointsDTO {
    private Long pointId;
    private Long caseId;
    private Integer points;
    private LocalDateTime transactionDate;
    private PointsTransactionType type;
    private String source;
    private LocalDate expiryDate;
    private PointsStatus status;
}