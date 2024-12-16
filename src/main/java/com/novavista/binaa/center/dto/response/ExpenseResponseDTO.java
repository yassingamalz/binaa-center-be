package com.novavista.binaa.center.dto.response;

import com.novavista.binaa.center.dto.request.ExpenseDTO;
import com.novavista.binaa.center.enums.ExpenseCategory;
import com.novavista.binaa.center.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponseDTO extends ExpenseDTO {
    private String staffName;

    public ExpenseResponseDTO(
            Long expenseId,
            ExpenseCategory category,
            BigDecimal amount,
            LocalDate date,
            String description,
            String receiptPath,
            Long paidBy,
            PaymentMethod paymentMethod,
            String staffName
    ) {
        super(expenseId, category, amount, date, description, receiptPath, paidBy, paymentMethod);
        this.staffName = staffName;
    }
}