package com.novavista.binaa.center.services.impl;

import com.novavista.binaa.center.dto.request.PaymentDTO;
import com.novavista.binaa.center.entity.Case;
import com.novavista.binaa.center.entity.Payment;
import com.novavista.binaa.center.entity.Session;
import com.novavista.binaa.center.enums.PaymentStatus;
import com.novavista.binaa.center.exceptions.ResourceNotFoundException;
import com.novavista.binaa.center.exceptions.ValidationException;
import com.novavista.binaa.center.repository.CaseRepository;
import com.novavista.binaa.center.repository.PaymentRepository;
import com.novavista.binaa.center.repository.SessionRepository;
import com.novavista.binaa.center.services.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


// PaymentServiceImpl.java
@Service
@Slf4j
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final CaseRepository caseRepository;
    private final SessionRepository sessionRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              CaseRepository caseRepository,
                              SessionRepository sessionRepository,
                              ModelMapper modelMapper) {
        this.paymentRepository = paymentRepository;
        this.caseRepository = caseRepository;
        this.sessionRepository = sessionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        log.info("Creating payment for case ID: {}", paymentDTO.getCaseId());
        validatePayment(paymentDTO);

        Case caseEntity = caseRepository.findById(paymentDTO.getCaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));

        Session session = null;
        if (paymentDTO.getSessionId() != null) {
            session = sessionRepository.findById(paymentDTO.getSessionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        }

        Payment payment = modelMapper.map(paymentDTO, Payment.class);
        payment.setCaseInfo(caseEntity);
        payment.setSession(session);
        payment.setPaymentDate(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);
        log.info("Created payment with ID: {}", savedPayment.getPaymentId());
        return modelMapper.map(savedPayment, PaymentDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDTO getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .map(payment -> modelMapper.map(payment, PaymentDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTO> getPaymentsByCase(Long caseId) {
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("Case not found"));
        return paymentRepository.findByCaseInfo(caseEntity).stream()
                .map(payment -> modelMapper.map(payment, PaymentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTO> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByPaymentStatus(status).stream()
                .map(payment -> modelMapper.map(payment, PaymentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDTO updatePayment(Long id, PaymentDTO paymentDTO) {
        log.info("Updating payment ID: {}", id);
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        validatePayment(paymentDTO);
        modelMapper.map(paymentDTO, existingPayment);
        existingPayment.setPaymentId(id);

        Payment updatedPayment = paymentRepository.save(existingPayment);
        log.info("Updated payment ID: {}", id);
        return modelMapper.map(updatedPayment, PaymentDTO.class);
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

    @Transactional(readOnly = true)
    public List<PaymentDTO> getPaymentsByDateRange(LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59, 999999999);

        return paymentRepository.findByPaymentDateBetween(startDateTime, endDateTime)
                .stream()
                .map(payment -> modelMapper.map(payment, PaymentDTO.class))
                .collect(Collectors.toList());
    }
}