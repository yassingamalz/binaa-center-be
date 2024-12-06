package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.ExpenseDTO;
import com.novavista.binaa.center.enums.ExpenseCategory;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {
    ExpenseDTO createExpense(ExpenseDTO expenseDTO);
    ExpenseDTO getExpenseById(Long id);
    List<ExpenseDTO> getExpensesByCategory(ExpenseCategory category);
    List<ExpenseDTO> getExpensesByDateRange(LocalDate start, LocalDate end);
    ExpenseDTO updateExpense(Long id, ExpenseDTO expenseDTO);
    void deleteExpense(Long id);
}
