package com.novavista.binaa.center.dto;

import com.novavista.binaa.center.enums.ExpenseCategory;
import com.novavista.binaa.center.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO {
    private Long expenseId;
    private ExpenseCategory category;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private String receiptPath;
    private Long paidBy;
    private PaymentMethod paymentMethod;
}