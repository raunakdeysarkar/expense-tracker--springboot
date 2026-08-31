package com.raunak.expensetracker;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

 public ExpenseService(ExpenseRepository expenseRepository,
                      UserRepository userRepository) {
    this.expenseRepository = expenseRepository;
    this.userRepository = userRepository;
}
    private User getUser(String username) {

    return userRepository.findByUsername(username)
            .orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "User not found"
                    ));
}

    public Expense addExpense(Expense expense, String username) {

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
        User user = getUser(username);
         expense.setUser(user);

        return expenseRepository.save(expense);
    }

    public List<Expense> getAllExpenses(String username) {

    User user = getUser(username);

    return expenseRepository.findByUser(user);
    }

        public Expense getExpenseById(Long id, String username) {

        User user = getUser(username);

        Expense expense = expenseRepository
                .findByIdAndUser(id, user)
                .orElse(null);

        if (expense == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Expense not found"
            );
        }

        return expense;
    }
    public void deleteExpense(Long id, String username) {

        User user = getUser(username);

        Expense expense = expenseRepository
                .findByIdAndUser(id, user)
                .orElse(null);

        if (expense == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Expense not found"
            );
        }

        expenseRepository.delete(expense);
    }

    public Expense updateExpense(
            Long id,
            Expense updatedExpense,
            String username) {

        User user = getUser(username);

        Expense expense = expenseRepository
                .findByIdAndUser(id, user)
                .orElse(null);

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
        public double getTotalExpenses(String username) {

        User user = getUser(username);

        List<Expense> expenses =
                expenseRepository.findByUser(user);

        double total = 0;

        for (Expense expense : expenses) {
            total += expense.getAmount();
        }

        return total;
    }

        public List<Expense> getExpensesByCategory(
            String category,
            String username) {

        User user = getUser(username);

        List<Expense> expenses =
                expenseRepository.findByUser(user);

        List<Expense> result =
                new java.util.ArrayList<>();

        for (Expense expense : expenses) {

            if (expense.getCategory() != null &&
                    expense.getCategory()
                            .equalsIgnoreCase(category)) {

                result.add(expense);
            }
        }

        return result;
    }

        public double getMonthlyTotal(
            String month,
            String username) {

        User user = getUser(username);

        List<Expense> expenses =
                expenseRepository.findByUser(user);

        double total = 0;

        for (Expense expense : expenses) {

            if (expense.getDate() != null &&
                    expense.getDate().startsWith(month)) {

                total += expense.getAmount();
            }
        }

        return total;
    }

    public String exportExpensesToCsv(String username) {

    User user = getUser(username);

    List<Expense> expenses =
            expenseRepository.findByUser(user);

    StringBuilder csv = new StringBuilder();

    csv.append("Date,Description,Amount,Category\n");

    for (Expense expense : expenses) {

        csv.append(
                CsvUtil.toLine(
                        expense.getDate(),
                        expense.getDescription(),
                        String.valueOf(expense.getAmount()),
                        expense.getCategory()
                )
        );

        csv.append("\n");
    }

    return csv.toString();
}
}
