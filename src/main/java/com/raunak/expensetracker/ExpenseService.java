package com.raunak.expensetracker;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense addExpense(Expense expense) {

        if (expense.getAmount() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid expense data"
            );
        }

        if (expense.getDescription() == null || expense.getDescription().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid expense data"
            );
        }

        if (expense.getCategory() == null || expense.getCategory().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid expense data"
            );
        }

        return expenseRepository.save(expense);
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense getExpenseById(Long id) {

        Expense expense = expenseRepository.findById(id).orElse(null);

        if (expense == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Expense not found"
            );
        }

        return expense;
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public Expense updateExpense(Long id, Expense updatedExpense) {

        Expense expense = expenseRepository.findById(id).orElse(null);

        if (expense == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Expense not found"
            );
        }

        expense.setDate(updatedExpense.getDate());
        expense.setDescription(updatedExpense.getDescription());
        expense.setAmount(updatedExpense.getAmount());
        expense.setCategory(updatedExpense.getCategory());

        return expenseRepository.save(expense);
    }

    public double getTotalExpenses() {

        List<Expense> expenses = expenseRepository.findAll();

        double total = 0;

        for (Expense expense : expenses) {
            total += expense.getAmount();
        }

        return total;
    }

    public List<Expense> getExpensesByCategory(String category) {

        List<Expense> expenses = expenseRepository.findAll();
        List<Expense> result = new java.util.ArrayList<>();

        for (Expense expense : expenses) {
            if (expense.getCategory() != null &&
                    expense.getCategory().equalsIgnoreCase(category)) {

                result.add(expense);
            }
        }

        return result;
    }

    public double getMonthlyTotal(String month) {

        List<Expense> expenses = expenseRepository.findAll();

        double total = 0;

        for (Expense expense : expenses) {
            if (expense.getDate().startsWith(month)) {
                total += expense.getAmount();
            }
        }

        return total;
    }
}
