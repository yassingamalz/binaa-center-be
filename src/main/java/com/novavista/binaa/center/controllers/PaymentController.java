package com.novavista.binaa.center.controllers;

import com.novavista.binaa.center.dto.request.PaymentDTO;
import com.novavista.binaa.center.dto.response.PaymentResponseDTO;
import com.novavista.binaa.center.enums.PaymentStatus;
import com.novavista.binaa.center.services.PaymentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        log.info("Fetching all payments");
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<PaymentResponseDTO> createPayment(@Valid @RequestBody PaymentDTO paymentDTO) {
        log.info("Creating new payment");
        return new ResponseEntity<>(paymentService.createPayment(paymentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable Long id) {
        log.info("Fetching payment with id: {}", id);
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/case/{caseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByCase(@PathVariable Long caseId) {
        log.info("Fetching payments for case: {}", caseId);
        return ResponseEntity.ok(paymentService.getPaymentsByCase(caseId));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        log.info("Fetching payments with status: {}", status);
        return ResponseEntity.ok(paymentService.getPaymentsByStatus(status));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponseDTO> updatePayment(@PathVariable Long id, @Valid @RequestBody PaymentDTO paymentDTO) {
        log.info("Updating payment with id: {}", id);
        return ResponseEntity.ok(paymentService.updatePayment(id, paymentDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        log.info("Deleting payment with id: {}", id);
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, fallbackPatterns = {"yyyy-MM-dd"}) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, fallbackPatterns = {"yyyy-MM-dd"}) LocalDate end) {
        log.info("Fetching payments between {} and {}", start, end);
        return ResponseEntity.ok(paymentService.getPaymentsByDateRange(start, end));
    }
}