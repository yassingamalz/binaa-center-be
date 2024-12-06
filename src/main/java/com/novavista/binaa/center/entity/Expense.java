package com.novavista.binaa.center.entity;

import com.novavista.binaa.center.enums.ExpenseCategory;
import com.novavista.binaa.center.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


// entities/Expense.java
@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;

    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private String receiptPath;

    @ManyToOne
    @JoinColumn(name = "paid_by")
    private Staff paidBy;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
}