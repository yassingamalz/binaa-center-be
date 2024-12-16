package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.request.ExpenseDTO;
import com.novavista.binaa.center.dto.response.ExpenseResponseDTO;
import com.novavista.binaa.center.enums.ExpenseCategory;
import com.novavista.binaa.center.enums.PaymentMethod;
import com.novavista.binaa.center.exceptions.ResourceNotFoundException;
import com.novavista.binaa.center.exceptions.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {
    /**
     * Creates a new expense entry in the system.
     *
     * @param expenseDTO The expense data transfer object containing expense details
     * @return The created expense with system-generated identifiers and full details
     * @throws ValidationException If the expense data is invalid
     */
    ExpenseResponseDTO createExpense(ExpenseDTO expenseDTO);


    /**
     * Retrieves all expenses in the system.
     *
     * @return A list of all expenses with full details
     */
    List<ExpenseResponseDTO> getAllExpenses();

    /**
     * Retrieves a specific expense by its unique identifier.
     *
     * @param id The unique identifier of the expense
     * @return The expense details matching the given ID
     * @throws ResourceNotFoundException If no expense is found with the given ID
     */
    ExpenseResponseDTO getExpenseById(Long id);

    /**
     * Retrieves expenses within a specified date range.
     *
     * @param start The start date of the date range (inclusive)
     * @param end The end date of the date range (inclusive)
     * @return A list of expenses falling within the specified date range
     */
    List<ExpenseResponseDTO> getExpensesByDateRange(LocalDate start, LocalDate end);

    /**
     * Retrieves all expenses categorized under a specific expense category.
     *
     * @param category The expense category to filter by
     * @return A list of expenses matching the specified category
     */
    List<ExpenseResponseDTO> getExpensesByCategory(ExpenseCategory category);

    /**
     * Retrieves expenses filtered by payment method.
     *
     * @param paymentMethod The payment method to filter by
     * @return A list of expenses matching the specified payment method
     */
    List<ExpenseResponseDTO> getExpensesByPaymentMethod(PaymentMethod paymentMethod);

    /**
     * Retrieves expenses filtered by staff who made the expense.
     *
     * @param staffId The ID of the staff who made the expense
     * @return A list of expenses made by the specified staff
     */
    List<ExpenseResponseDTO> getExpensesByStaff(Long staffId);

    /**
     * Retrieves expenses within a specific amount range.
     *
     * @param minAmount The minimum amount (inclusive)
     * @param maxAmount The maximum amount (inclusive)
     * @return A list of expenses within the specified amount range
     */
    List<ExpenseResponseDTO> getExpensesByAmountRange(BigDecimal minAmount, BigDecimal maxAmount);

    /**
     * Updates an existing expense with new information.
     *
     * @param id The unique identifier of the expense to update
     * @param expenseDTO The expense data transfer object with updated details
     * @return The updated expense with modified information
     * @throws ResourceNotFoundException If no expense is found with the given ID
     * @throws ValidationException If the updated expense data is invalid
     */
    ExpenseResponseDTO updateExpense(Long id, ExpenseDTO expenseDTO);

    /**
     * Deletes an expense from the system.
     *
     * @param id The unique identifier of the expense to delete
     * @throws ResourceNotFoundException If no expense is found with the given ID
     * @throws RuntimeException If the expense cannot be deleted due to data integrity constraints
     */
    void deleteExpense(Long id);
}