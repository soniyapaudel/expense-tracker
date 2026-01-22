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
import java.time.LocalDate;

public class ExpenseCLI {
    private final Scanner scanner = new Scanner(System.in);
    private List<Expense> expenses = new ArrayList<>();
    private final String DATA_FOLDER = "data";
    private final String FILE_NAME = DATA_FOLDER + "/expenses.txt";

    public ExpenseCLI() {
        loadExpenseFromFile();
    }

    private LocalDate readDate() {
        while (true) {
            try {
                return LocalDate.parse(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use yyyy-mm-dd:");
            }
        }
    }

    public void start() {
        while (true) {
            System.out.println("\n Expense Tracker Menu ");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. View Total");
            System.out.println("4. Filter by Category");
            System.out.println("5. Delete an Expense by Number");
            System.out.println("6. Delete all Expenses of a Category");
            System.out.println("7. Filter By Date");
            System.out.println("8. Edit Expense");
            System.out.println("9. Generate Expense Report");
            System.out.println("0. Exit");
            System.out.println("Enter choice:");

            int choice;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Please enter a number for switch !");
                scanner.nextLine();
                continue;
            }

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
                case 5:
                    deleteExpensesByNumber();
                    break;
                case 6:
                    deleteExpensesByCategory();
                    break;
                case 7:
                    filterByDate();
                    break;
                case 8:
                    editExpense();
                    break;
                case 9:
                    generateExpenseReport();
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

    // ======Add Expense===========

    private void addExpense() {
        System.out.println("Enter date (yyyy-mm-dd):");
        LocalDate date = readDate();

        System.out.println("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Enter Category: ");
        String category = scanner.nextLine();

        System.out.println("Enter description: ");
        String description = scanner.nextLine();

        Expense expense = new Expense(amount, category, description, date);
        expenses.add(expense);
        saveExpenseToFile(expense);

        System.out.println("Expense added succesfully!");
    }

    // ==========View expense==================

    public void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
            return;
        }
        System.out.println("\n List of Expenses ");
        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.get(i);
            System.out.printf("%d. Amount: %2f, Category: %s, Description: %s, Date: %s%n", i + 1, e.getAmount(),
                    e.getCategory(), e.getDescription(), e.getDate());
        }
    }

    // ====View Total===================

    private void viewTotal() {
        double total = 0;
        for (Expense e : expenses) {
            total += e.getAmount();
        }
        System.out.printf("Total Expense: %.2f%n", total);
    }

    // =========Filter By Category===============
    private void filterByCategory() {
        System.out.println("Enter Category: ");
        String cat = scanner.nextLine();

        boolean found = false;
        int count = 1;
        for (Expense e : expenses) {
            if (e.getCategory().equalsIgnoreCase(cat)) {
                System.out.printf("%d. Amount: %2f, Category: %s, Description: %s, Date: %s%n", count++,
                        e.getAmount(),
                        e.getCategory(), e.getDescription(), e.getDate());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No expenses found for category: " + cat);
        }
    }

    // ========Delete Expense By Number==============
    private void deleteExpensesByNumber() {
        viewExpenses();

        if (expenses.isEmpty())
            return;

        System.out.println("Enter the number of the expense to delete:");
        int number;
        try {
            number = Integer.parseInt(scanner.nextLine());
            if (number < 1 || number > expenses.size()) {
                System.out.println("Invalid number!");
                return;

            }
            Expense removed = expenses.remove(number - 1);
            saveAllExpensesToFile();
            System.out.println("Deleted expense: " + removed);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
        }
    }

    /// ==========Delete Expense By category ==============
    private void deleteExpensesByCategory() {
        System.out.println("Enter the category to delete all expenses from:");
        String cat = scanner.nextLine().trim();

        boolean anyRemoved = expenses.removeIf(e -> e.getCategory().trim().equalsIgnoreCase(cat));

        if (anyRemoved) {
            saveAllExpensesToFile();
            System.out.println("Deleted all expenses for category: " + cat);
        } else {
            System.out.println("No expenses found for category: " + cat);
        }
    }

    // =====Filter By date =================
    private void filterByDate() {
        System.out.println("Enter date (yyyy-mm-dd) ");
        LocalDate date = readDate();

        boolean found = false;
        int count = 1;
        for (Expense e : expenses) {
            if (e.getDate().equals(date)) {
                System.out.printf("%d. Amount: %2f, Category: %s, Description:%s, Date: %s%n", count++, e.getAmount(),
                        e.getCategory(), e.getDescription(), e.getDate());

                found = true;
            }
        }

        if (!found) {
            System.out.println("No expenses found for date: " + date);
        }
    }

    // ========= Edit Expense================

    private void editExpense() {
        viewExpenses();
        if (expenses.isEmpty())
            return;

        System.out.println("Enter expense number to edit:");
        int index;

        try {
            index = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number");
            return;
        }

        if (index < 0 || index >= expenses.size()) {
            System.out.println("Invalid expenses number");
            return;
        }

        Expense old = expenses.get(index);

        System.out.println("Enter new amount (press Enter to keep" + old.getAmount() + "):");
        String amountInput = scanner.nextLine();
        double amount = old.getAmount();
        if (!amountInput.isBlank()) {
            amount = Double.parseDouble(amountInput);
        }

        System.out.println("Enter new category(press Enter to keep '" + old.getCategory() + "'):");
        String categoryInput = scanner.nextLine();
        String category = old.getCategory();
        if (!categoryInput.isBlank()) {
            category = categoryInput;
        }

        System.out.println("Enter new description(press Enter to keep current):");
        String descriptionInput = scanner.nextLine();
        String description = old.getDescription();
        if (!descriptionInput.isBlank()) {
            description = descriptionInput;
        }

        System.out.println("Enter new date (yyyy-mm-dd) or press Enter to keep" + old.getDate() + " :");
        String dateInput = scanner.nextLine();
        LocalDate date = old.getDate();
        if (!dateInput.isBlank()) {
            while (true) {
                try {
                    date = LocalDate.parse(dateInput);
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid date format. Use yyyy-mm-dd:");
                    dateInput = scanner.nextLine();
                }
            }
        }

        expenses.set(index, new Expense(amount, category, description, date));
        saveAllExpensesToFile();

        System.out.println("Expense updated successfully");
    }

    // ==== Generate Expense Report======

    private void generateExpenseReport() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses to report.");
        }
        System.out.println("\n Expense Report");

        int count = 1;
        for (Expense e : expenses) {
            System.out.printf("%d.) Amount: %2f | Category:%s | Description: %s | Date: %s%n", count++, e.getAmount(),
                    e.getCategory(), e.getDescription(), e.getDate());
        }
    }
    // =========Save Expense================

    private void saveExpenseToFile(Expense e) {
        try {
            // create folder if missing for storing the data that user input

            File folder = new File(DATA_FOLDER);
            if (!folder.exists())
                folder.mkdir();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
                writer.write(e.getAmount() + "," + e.getCategory() + "," + e.getDescription() + "," + e.getDate());
                writer.newLine();
            }

        } catch (IOException ex) {
            System.out.println("Error saving expense to file:" + ex.getMessage());
        }
    }

    // ==========Load Expense==============

    private void loadExpenseFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 4);

                if (parts.length >= 3) {
                    double amount = Double.parseDouble(parts[0]);
                    String category = parts[1];
                    String description = parts[2];

                    LocalDate date;
                    if (parts.length >= 4) {
                        date = LocalDate.parse(parts[3]);
                    } else {
                        date = LocalDate.now();
                    }
                    expenses.add(new Expense(amount, category, description, date));
                }
            }
        } catch (IOException ex) {
            System.out.println("Error loading expenses from file: " + ex.getMessage());
        }
    }

    // ======= save All Expense===========

    private void saveAllExpensesToFile() {
        try {
            File folder = new File(DATA_FOLDER);
            if (!folder.exists())
                folder.mkdir();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
                for (Expense e : expenses) {
                    writer.write(e.getAmount() + "," + e.getCategory() + "," + e.getDescription() + "," + e.getDate());
                    writer.newLine();
                }
            }
        } catch (IOException ex) {
            System.out.println("Error saving expenses to file: " + ex.getMessage());
        }
    }

}