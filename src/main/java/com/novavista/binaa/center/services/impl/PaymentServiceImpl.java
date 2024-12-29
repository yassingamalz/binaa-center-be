package com.novavista.binaa.center.services.impl;

import com.novavista.binaa.center.dto.request.PaymentDTO;
import com.novavista.binaa.center.dto.response.PaymentResponseDTO;
import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.entity.Payment;
import com.novavista.binaa.center.entity.Session;
import com.novavista.binaa.center.enums.PaymentStatus;
import com.novavista.binaa.center.exceptions.ResourceNotFoundException;
import com.novavista.binaa.center.exceptions.ValidationException;
import com.novavista.binaa.center.mapper.PaymentMapper;
import com.novavista.binaa.center.repository.CaseRepository;
import com.novavista.binaa.center.repository.PaymentRepository;
import com.novavista.binaa.center.repository.SessionRepository;
import com.novavista.binaa.center.services.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final CaseRepository caseRepository;
    private final SessionRepository sessionRepository;
    private final PaymentMapper paymentMapper;

    @Autowired
    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            CaseRepository caseRepository,
            SessionRepository sessionRepository,
            PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.caseRepository = caseRepository;
        this.sessionRepository = sessionRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public PaymentResponseDTO createPayment(PaymentDTO paymentDTO) {
        log.info("Creating payment for case ID: {}", paymentDTO.getCaseId());
        validatePayment(paymentDTO);

        Case caseEntity = caseRepository.findById(paymentDTO.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));

        Session session = null;
        if (paymentDTO.getSessionId() != null) {
            session = sessionRepository.findById(paymentDTO.getSessionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        }

        String invoiceNumber = generateInvoiceNumber();

        Payment payment = paymentMapper.toEntity(paymentDTO);
        payment.setCaseInfo(caseEntity);
        payment.setSession(session);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setInvoiceNumber(invoiceNumber);

        Payment savedPayment = paymentRepository.save(payment);
        log.info("Created payment with ID: {} and invoice number {}", savedPayment.getPaymentId(), invoiceNumber);
        return paymentMapper.toResponseDto(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getAllPayments() {
        log.info("Fetching all payments");
        return paymentMapper.toResponseDtoList(paymentRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .map(paymentMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getPaymentsByCase(Long caseId) {
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
        return paymentMapper.toResponseDtoList(paymentRepository.findByCaseInfo(caseEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getPaymentsByStatus(PaymentStatus status) {
        return paymentMapper.toResponseDtoList(paymentRepository.findByPaymentStatus(status));
    }

    @Override
    public PaymentResponseDTO updatePayment(Long id, PaymentDTO paymentDTO) {
        log.info("Updating payment ID: {}", id);
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        validatePayment(paymentDTO);

        Case caseEntity = caseRepository.findById(paymentDTO.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));

        Session session = null;
        if (paymentDTO.getSessionId() != null) {
            session = sessionRepository.findById(paymentDTO.getSessionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        }

        Payment payment = paymentMapper.toEntity(paymentDTO);
        payment.setPaymentId(id);
        payment.setCaseInfo(caseEntity);
        payment.setSession(session);
        payment.setInvoiceNumber(existingPayment.getInvoiceNumber());
        payment.setPaymentDate(existingPayment.getPaymentDate());

        Payment updatedPayment = paymentRepository.save(payment);
        log.info("Updated payment ID: {}", id);
        return paymentMapper.toResponseDto(updatedPayment);
    }

    @Override
    public void deletePayment(Long id) {
        log.info("Deleting payment ID: {}", id);
        try {
            paymentRepository.deleteById(id);
            log.info("Deleted payment ID: {}", id);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to delete payment: {}", e.getMessage());
            throw new RuntimeException("Cannot delete payment");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getPaymentsByDateRange(LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59, 999999999);

        return paymentMapper.toResponseDtoList(
                paymentRepository.findByPaymentDateBetween(startDateTime, endDateTime)
        );
    }

    private String generateInvoiceNumber() {
        String year = String.valueOf(LocalDate.now().getYear());
        long paymentCount = paymentRepository.countByPaymentDateBetween(
                LocalDateTime.of(LocalDate.now().getYear(), 1, 1, 0, 0),
                LocalDateTime.of(LocalDate.now().getYear(), 12, 31, 23, 59, 59)
        ) + 1;
        String sequentialNumber = String.format("%03d", paymentCount);
        return String.format("INV-%s-%s", year, sequentialNumber);
    }

    private void validatePayment(PaymentDTO paymentDTO) {
        if (paymentDTO.getAmount() == null || paymentDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Valid payment amount is required");
        }
        if (paymentDTO.getPaymentMethod() == null) {
            throw new ValidationException("Payment method is required");
        }
        if (paymentDTO.getPaymentStatus() == null) {
            throw new ValidationException("Payment status is required");
        }
    }
}