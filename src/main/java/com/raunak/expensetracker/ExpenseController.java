package com.raunak.expensetracker;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public Expense addExpense(
        @RequestBody Expense expense,
        Authentication authentication) {

    String username = authentication.getName();

    return expenseService.addExpense(expense, username);
    }   

    @GetMapping
    public List<Expense> getAllExpenses(Authentication authentication) {

    String username = authentication.getName();

    return expenseService.getAllExpenses(username);
}

    @GetMapping("/{id}")
    public Expense getExpenseById(
            @PathVariable Long id,
            Authentication authentication) {

        String username = authentication.getName();

        return expenseService.getExpenseById(id, username);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(
            @PathVariable Long id,
            Authentication authentication) {

        String username = authentication.getName();

    expenseService.deleteExpense(id, username);
}
    @PutMapping("/{id}")
    public Expense updateExpense(
            @PathVariable Long id,
            @RequestBody Expense updatedExpense,
            Authentication authentication) {

        String username = authentication.getName();

        return expenseService.updateExpense(
                id,
                updatedExpense,
                username
        );
    }

        @GetMapping("/summary")
    public double getTotalExpenses(Authentication authentication) {

        String username = authentication.getName();

        return expenseService.getTotalExpenses(username);
    }

        @GetMapping("/category/{category}")
    public List<Expense> getExpensesByCategory(
            @PathVariable String category,
            Authentication authentication) {

        String username = authentication.getName();

        return expenseService.getExpensesByCategory(
                category,
                username
        );
    }
        @GetMapping("/month/{month}")
public double getMonthlyTotal(
        @PathVariable String month,
        Authentication authentication) {

    String username = authentication.getName();

    return expenseService.getMonthlyTotal(
            month,
            username
    );
}

@GetMapping("/export")
public org.springframework.http.ResponseEntity<String> exportExpenses(
        Authentication authentication) {

    String username = authentication.getName();

    String csv = expenseService.exportExpensesToCsv(username);

    return org.springframework.http.ResponseEntity.ok()
            .header(
                    "Content-Disposition",
                    "attachment; filename=expenses.csv"
            )
            .header(
                    "Content-Type",
                    "text/csv"
            )
            .body(csv);
}
    
}
