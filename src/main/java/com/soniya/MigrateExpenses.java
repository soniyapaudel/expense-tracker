package com.soniya;

import com.soniya.dao.ExpenseDAO;
import com.soniya.expensetracker.model.Expense;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;

public class MigrateExpenses {

    public static void main(String[] args) {
        String filePath = "data/expenses.txt";
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("expenses.txt not found!");
            return;
        }

        ExpenseDAO dao = new ExpenseDAO();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 4);
                if (parts.length >= 4) {
                    double amount = Double.parseDouble(parts[0]);
                    String category = parts[1];
                    String description = parts[2];
                    LocalDate date = LocalDate.parse(parts[3]);

                    Expense expense = new Expense(amount, category, description, date);
                    dao.addExpense(expense);
                }

            }
            System.out.println("All old expenses migrated to database!");
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
