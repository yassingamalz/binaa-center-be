package com.novavista.binaa.center.repository;

import com.novavista.binaa.center.entity.Expense;
import com.novavista.binaa.center.enums.ExpenseCategory;
import com.novavista.binaa.center.enums.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByCategory(ExpenseCategory category);
    List<Expense> findByDateBetween(LocalDate start, LocalDate end);
    List<Expense> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);

    List<Expense> findByPaidBy_StaffId(Long staffId);

    List<Expense> findByPaymentMethod(PaymentMethod paymentMethod);
}