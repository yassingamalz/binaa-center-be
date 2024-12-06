package com.novavista.binaa.center.services.impl;

import com.novavista.binaa.center.dto.ExpenseDTO;
import com.novavista.binaa.center.entity.Expense;
import com.novavista.binaa.center.entity.Staff;
import com.novavista.binaa.center.enums.ExpenseCategory;
import com.novavista.binaa.center.exceptions.ResourceNotFoundException;
import com.novavista.binaa.center.exceptions.ValidationException;
import com.novavista.binaa.center.repository.ExpenseRepository;
import com.novavista.binaa.center.repository.StaffRepository;
import com.novavista.binaa.center.services.ExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


// ExpenseServiceImpl.java
@Service
@Slf4j
@Transactional
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final StaffRepository staffRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ExpenseServiceImpl(ExpenseRepository expenseRepository,
                              StaffRepository staffRepository,
                              ModelMapper modelMapper) {
        this.expenseRepository = expenseRepository;
        this.staffRepository = staffRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ExpenseDTO createExpense(ExpenseDTO expenseDTO) {
        log.info("Creating new expense of amount: {}", expenseDTO.getAmount());
        validateExpense(expenseDTO);

        Staff paidBy = staffRepository.findById(expenseDTO.getPaidBy())
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

        Expense expense = modelMapper.map(expenseDTO, Expense.class);
        expense.setPaidBy(paidBy);

        Expense savedExpense = expenseRepository.save(expense);
        log.info("Created expense with ID: {}", savedExpense.getExpenseId());
        return modelMapper.map(savedExpense, ExpenseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseDTO getExpenseById(Long id) {
        return expenseRepository.findById(id)
                .map(expense -> modelMapper.map(expense, ExpenseDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseDTO> getExpensesByCategory(ExpenseCategory category) {
        return expenseRepository.findByCategory(category).stream()
                .map(expense -> modelMapper.map(expense, ExpenseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseDTO> getExpensesByDateRange(LocalDate start, LocalDate end) {
        return expenseRepository.findByDateBetween(start, end).stream()
                .map(expense -> modelMapper.map(expense, ExpenseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseDTO updateExpense(Long id, ExpenseDTO expenseDTO) {
        log.info("Updating expense ID: {}", id);
        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        validateExpense(expenseDTO);
        modelMapper.map(expenseDTO, existingExpense);
        existingExpense.setExpenseId(id);

        Expense updatedExpense = expenseRepository.save(existingExpense);
        log.info("Updated expense ID: {}", id);
        return modelMapper.map(updatedExpense, ExpenseDTO.class);
    }

    @Override
    public void deleteExpense(Long id) {
        log.info("Deleting expense ID: {}", id);
        try {
            expenseRepository.deleteById(id);
            log.info("Deleted expense ID: {}", id);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to delete expense: {}", e.getMessage());
            throw new RuntimeException("Cannot delete expense");
        }
    }

    private void validateExpense(ExpenseDTO expenseDTO) {
        if (expenseDTO.getCategory() == null) {
            throw new ValidationException("Expense category is required");
        }
        if (expenseDTO.getAmount() == null || expenseDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Valid expense amount is required");
        }
        if (expenseDTO.getDate() == null) {
            throw new ValidationException("Expense date is required");
        }
    }
}