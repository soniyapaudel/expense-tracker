package com.soniya.expensetracker.cli;

import com.soniya.expensetracker.model.Expense;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class ExpenseCLI {
    private final Scanner scanner = new Scanner(System.in);
    private List<Expense> expenses = new ArrayList<>();
    private final String DATA_FOLDER = "data";
    private final String FILE_NAME = DATA_FOLDER + "/expenses.txt";

    public ExpenseCLI() {
        loadExpenseFromFile();
    }

    public void start() {
        while (true) {
            System.out.println("\n Expense Tracker Menu ");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. View Total");
            System.out.println("4. Filter by Category");
            System.out.println("0. Exit");
            System.out.println("Enter choice:");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addExpense();
                    break;
                case 2:
                    viewExpenses();
                    break;
                case 3:
                    viewTotal();
                    break;
                case 4:
                    filterByCategory();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice, try again");
                    break;

            }

        }
    }

    private void addExpense() {
        System.out.println("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Enter Category: ");
        String category = scanner.nextLine();

        System.out.println("Enter description: ");
        String description = scanner.nextLine();

        Expense expense = new Expense(amount, category, description);
        expenses.add(expense);
        saveExpenseToFile(expense);

        System.out.println("Expense added succesfully!");
    }

    public void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }
        System.out.println("\n List of Expenses ");
        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.get(i);
            System.out.printf("%d. Amount: %2f, Category: %s, Description: %s%n", i + 1, e.getAmount(),
                    e.getCategory(), e.getDescription());
        }
    }

    private void viewTotal() {
        double total = 0;
        for (Expense e : expenses) {
            total += e.getAmount();
        }
        System.out.printf("Total Expense: %.2f%n", total);
    }

    private void filterByCategory() {
        System.out.println("Enter Category: ");
        String cat = scanner.nextLine();

        boolean found = false;
        int count = 1;
        for (Expense e : expenses) {
            if (e.getCategory().equalsIgnoreCase(cat)) {
                System.out.printf("%d. Amount: %2f, Category: %s, Description: %s%n", count++, e.getAmount(),
                        e.getCategory(), e.getDescription());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No expenses found for category: " + cat);
        }
    }

    private void saveExpenseToFile(Expense e) {
        try {
            // create folder if missing for storing the data that user input

            File folder = new File(DATA_FOLDER);
            if (!folder.exists())
                folder.mkdir();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
                writer.write(e.getAmount() + "," + e.getCategory() + "," + e.getDescription());
                writer.newLine();
            }

        } catch (IOException ex) {
            System.out.println("Error saving expense to file:" + ex.getMessage());
        }
    }

    private void loadExpenseFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 3);

                if (parts.length == 3) {
                    double amount = Double.parseDouble(parts[0]);
                    String category = parts[1];
                    String description = parts[2];
                    expenses.add(new Expense(amount, category, description));
                }
            }
        } catch (IOException ex) {
            System.out.println("Error loading expenses from file: " + ex.getMessage());
        }
    }
}