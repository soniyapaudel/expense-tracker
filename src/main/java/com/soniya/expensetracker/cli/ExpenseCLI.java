package com.soniya.expensetracker.cli;

import com.soniya.expensetracker.model.Expense;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class ExpenseCLI {
    private final Scanner scanner = new Scanner(System.in);
    private List<Expense> expenses = new ArrayList<>();

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
                    System.out.println("Enter amount: ");
                    double amount = scanner.nextDouble();

                    System.out.println("Enter Category: ");
                    String category = scanner.nextLine();

                    System.out.println("Enter description: ");
                    String description = scanner.nextLine();

                    Expense expense = new Expense(amount, category, description);
                    expenses.add(expense);

                    System.out.println("Expense added succesfully!");
                    break;
                case 2:
                    System.out.println("View Expense chosen");
                    break;
                case 3:
                    System.out.println("View Total chosen");
                    break;
                case 4:
                    System.out.println("Filter by Category chosen");
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
}