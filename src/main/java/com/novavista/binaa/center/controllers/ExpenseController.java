package com.novavista.binaa.center.controllers;

import com.novavista.binaa.center.dto.request.ExpenseDTO;
import com.novavista.binaa.center.enums.ExpenseCategory;
import com.novavista.binaa.center.services.ExpenseService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/api/expenses")
@Slf4j
public class ExpenseController {
    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExpenseDTO> createExpense(@Valid @RequestBody ExpenseDTO expenseDTO) {
        log.info("Creating new expense");
        return new ResponseEntity<>(expenseService.createExpense(expenseDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExpenseDTO> getExpenseById(@PathVariable Long id) {
        log.info("Fetching expense with id: {}", id);
        return ResponseEntity.ok(expenseService.getExpenseById(id));
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByCategory(@PathVariable ExpenseCategory category) {
        log.info("Fetching expenses for category: {}", category);
        return ResponseEntity.ok(expenseService.getExpensesByCategory(category));
    }

    @GetMapping("/by-date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, fallbackPatterns = {"yyyy-MM-dd"}) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, fallbackPatterns = {"yyyy-MM-dd"}) LocalDate end) {
        log.info("Fetching expenses between {} and {}", start, end);
        return ResponseEntity.ok(expenseService.getExpensesByDateRange(start, end));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExpenseDTO> updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseDTO expenseDTO) {
        log.info("Updating expense with id: {}", id);
        return ResponseEntity.ok(expenseService.updateExpense(id, expenseDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        log.info("Deleting expense with id: {}", id);
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}