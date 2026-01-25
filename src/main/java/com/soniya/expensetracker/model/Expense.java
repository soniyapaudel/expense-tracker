package com.soniya.expensetracker.model;

import java.time.LocalDate;

public class Expense {

    // Default constructor
    public Expense() {

    }

    private int id; // from DB
    private double amount;
    private String category;
    private String description;
    private LocalDate date;

    public Expense(int id, double amount, String category, String description, LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;

    }

    // constructor without id for adding expenses

    public Expense(double amount, String category, String description, LocalDate date) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
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

    public LocalDate getDate() {
        return date;
    }

    // Setter

    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Amount: " + amount + ", Category: " + category + ", Description: " + description;
    }
}
