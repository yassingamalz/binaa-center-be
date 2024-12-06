package com.novavista.binaa.center.repository;

import com.novavista.binaa.center.entity.Expense;
import com.novavista.binaa.center.enums.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByCategory(ExpenseCategory category);
    List<Expense> findByDateBetween(LocalDate start, LocalDate end);
}