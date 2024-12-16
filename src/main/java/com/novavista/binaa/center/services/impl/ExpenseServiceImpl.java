package com.novavista.binaa.center.services.impl;

import com.novavista.binaa.center.dto.request.ExpenseDTO;
import com.novavista.binaa.center.dto.response.ExpenseResponseDTO;
import com.novavista.binaa.center.entity.Expense;
import com.novavista.binaa.center.entity.Staff;
import com.novavista.binaa.center.enums.ExpenseCategory;
import com.novavista.binaa.center.enums.PaymentMethod;
import com.novavista.binaa.center.exceptions.ResourceNotFoundException;
import com.novavista.binaa.center.exceptions.ValidationException;
import com.novavista.binaa.center.mapper.ExpenseMapper;
import com.novavista.binaa.center.repository.ExpenseRepository;
import com.novavista.binaa.center.repository.StaffRepository;
import com.novavista.binaa.center.services.ExpenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@Transactional
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final StaffRepository staffRepository;
    private final ExpenseMapper expenseMapper;

    @Autowired
    public ExpenseServiceImpl(
            ExpenseRepository expenseRepository,
            StaffRepository staffRepository,
            ExpenseMapper expenseMapper
    ) {
        this.expenseRepository = expenseRepository;
        this.staffRepository = staffRepository;
        this.expenseMapper = expenseMapper;
    }

    @Override
    public ExpenseResponseDTO createExpense(ExpenseDTO expenseDTO) {
        log.info("Creating new expense: {}", expenseDTO);

        // Validate input
        validateExpense(expenseDTO);

        // Resolve staff
        Expense expense = resolveStaff(expenseMapper.toEntity(expenseDTO));

        // Save and return
        Expense savedExpense = expenseRepository.save(expense);
        return expenseMapper.toResponseDto(savedExpense);
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponseDTO getExpenseById(Long id) {
        log.info("Fetching expense with ID: {}", id);
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with ID: " + id));
        return expenseMapper.toResponseDto(expense);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponseDTO> getAllExpenses() {
        log.info("Fetching all expenses");
        List<Expense> expenses = expenseRepository.findAll();
        return expenseMapper.toResponseDtoList(expenses);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponseDTO> getExpensesByDateRange(LocalDate start, LocalDate end) {
        log.info("Fetching expenses between {} and {}", start, end);
        List<Expense> expenses = expenseRepository.findByDateBetween(start, end);
        return expenseMapper.toResponseDtoList(expenses);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponseDTO> getExpensesByCategory(ExpenseCategory category) {
        log.info("Fetching expenses for category: {}", category);
        List<Expense> expenses = expenseRepository.findByCategory(category);
        return expenseMapper.toResponseDtoList(expenses);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponseDTO> getExpensesByPaymentMethod(PaymentMethod paymentMethod) {
        log.info("Fetching expenses by payment method: {}", paymentMethod);
        List<Expense> expenses = expenseRepository.findByPaymentMethod(paymentMethod);
        return expenseMapper.toResponseDtoList(expenses);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponseDTO> getExpensesByStaff(Long staffId) {
        log.info("Fetching expenses for staff ID: {}", staffId);
        List<Expense> expenses = expenseRepository.findByPaidBy_StaffId(staffId);
        return expenseMapper.toResponseDtoList(expenses);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponseDTO> getExpensesByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        log.info("Fetching expenses between {} and {} amount", minAmount, maxAmount);
        List<Expense> expenses = expenseRepository.findByAmountBetween(minAmount, maxAmount);
        return expenseMapper.toResponseDtoList(expenses);
    }

    @Override
    public ExpenseResponseDTO updateExpense(Long id, ExpenseDTO expenseDTO) {
        log.info("Updating expense with ID: {}", id);

        // Find existing expense
        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with ID: " + id));

        // Validate input
        validateExpense(expenseDTO);

        // Update expense fields
        updateExpenseFields(existingExpense, expenseDTO);

        // Save and return
        Expense updatedExpense = expenseRepository.save(existingExpense);
        return expenseMapper.toResponseDto(updatedExpense);
    }

    @Override
    public void deleteExpense(Long id) {
        log.info("Deleting expense with ID: {}", id);

        // Check if expense exists
        if (!expenseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Expense not found with ID: " + id);
        }

        try {
            expenseRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Cannot delete expense due to data integrity constraints", e);
        }
    }

    // Helper method to validate expense
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

        if (expenseDTO.getPaymentMethod() == null) {
            throw new ValidationException("Payment method is required");
        }
    }

    // Helper method to resolve staff
    private Expense resolveStaff(Expense expense) {
        if (expense.getPaidBy() != null && expense.getPaidBy().getStaffId() != null) {
            Staff staff = staffRepository.findById(expense.getPaidBy().getStaffId())
                    .orElseThrow(() -> new ValidationException("Invalid staff ID"));
            expense.setPaidBy(staff);
        }
        return expense;
    }

    // Helper method to update expense fields
    private void updateExpenseFields(Expense existingExpense, ExpenseDTO updateDTO) {
        if (updateDTO.getCategory() != null) {
            existingExpense.setCategory(updateDTO.getCategory());
        }

        if (updateDTO.getAmount() != null) {
            existingExpense.setAmount(updateDTO.getAmount());
        }

        if (updateDTO.getDate() != null) {
            existingExpense.setDate(updateDTO.getDate());
        }

        if (updateDTO.getDescription() != null) {
            existingExpense.setDescription(updateDTO.getDescription());
        }

        if (updateDTO.getReceiptPath() != null) {
            existingExpense.setReceiptPath(updateDTO.getReceiptPath());
        }

        if (updateDTO.getPaymentMethod() != null) {
            existingExpense.setPaymentMethod(updateDTO.getPaymentMethod());
        }

        // Handle staff update
        if (updateDTO.getPaidBy() != null) {
            Staff updatedStaff = staffRepository.findById(updateDTO.getPaidBy())
                    .orElseThrow(() -> new ValidationException("Invalid staff ID"));
            existingExpense.setPaidBy(updatedStaff);
        }
    }
}