package com.novavista.binaa.center.dto.response;

import com.novavista.binaa.center.dto.request.PaymentDTO;
import com.novavista.binaa.center.enums.PaymentMethod;
import com.novavista.binaa.center.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO extends PaymentDTO {
    private String caseName;

    public PaymentResponseDTO(
            Long paymentId,
            Long caseId,
            Long sessionId,
            BigDecimal amount,
            LocalDateTime paymentDate,
            PaymentMethod paymentMethod,
            String invoiceNumber,
            PaymentStatus paymentStatus,
            String caseName
    ) {
        super(paymentId, caseId, sessionId, amount, paymentDate,
                paymentMethod, invoiceNumber, paymentStatus);
        this.caseName = caseName;
    }
}