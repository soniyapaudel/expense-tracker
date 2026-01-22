package com.soniya.expensetracker.model;
import java.time.LocalDate;

public class Expense {

    private double amount;
    private String category;
    private String description;
    private LocalDate date;

    public Expense(double amount, String category, String description, LocalDate date) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;

    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }
    public LocalDate getDate(){
        return date;
    }

    @Override
    public String toString() {
        return "Amount: " + amount + ", Category: " + category + ", Description: " + description;
    }

}
