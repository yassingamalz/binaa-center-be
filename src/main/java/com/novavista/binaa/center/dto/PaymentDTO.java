package com.novavista.binaa.center.dto;

import com.novavista.binaa.center.enums.PaymentMethod;
import com.novavista.binaa.center.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long paymentId;
    private Long caseId;
    private Long sessionId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private PaymentMethod paymentMethod;
    private String invoiceNumber;
    private PaymentStatus paymentStatus;
}