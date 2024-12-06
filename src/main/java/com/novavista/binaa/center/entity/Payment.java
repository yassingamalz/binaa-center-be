package com.novavista.binaa.center.entity;

import com.novavista.binaa.center.enums.PaymentMethod;
import com.novavista.binaa.center.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseInfo;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String description;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String invoiceNumber;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private BigDecimal discountApplied;
    private BigDecimal taxAmount;
}