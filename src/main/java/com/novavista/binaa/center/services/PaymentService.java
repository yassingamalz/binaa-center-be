package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.PaymentDTO;
import com.novavista.binaa.center.enums.PaymentStatus;

import java.util.List;

public interface PaymentService {
    PaymentDTO createPayment(PaymentDTO paymentDTO);
    PaymentDTO getPaymentById(Long id);
    List<PaymentDTO> getPaymentsByCase(Long caseId);
    List<PaymentDTO> getPaymentsByStatus(PaymentStatus status);
    PaymentDTO updatePayment(Long id, PaymentDTO paymentDTO);
    void deletePayment(Long id);
}
