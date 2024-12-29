package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.request.PaymentDTO;
import com.novavista.binaa.center.dto.response.PaymentResponseDTO;
import com.novavista.binaa.center.enums.PaymentStatus;

import java.time.LocalDate;
import java.util.List;

public interface PaymentService {
    PaymentResponseDTO createPayment(PaymentDTO paymentDTO);
    List<PaymentResponseDTO> getAllPayments();
    PaymentResponseDTO getPaymentById(Long id);
    List<PaymentResponseDTO> getPaymentsByCase(Long caseId);
    List<PaymentResponseDTO> getPaymentsByStatus(PaymentStatus status);
    List<PaymentResponseDTO> getPaymentsByDateRange(LocalDate start, LocalDate end);
    PaymentResponseDTO updatePayment(Long id, PaymentDTO paymentDTO);
    void deletePayment(Long id);
}