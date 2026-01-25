package com.soniya.dao;

import com.soniya.expensetracker.model.Expense;

import java.sql.Connection;
import com.soniya.db.DBConnection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class ExpenseDAO {

    // CREATE: Add new Expense
    public void addExpense(Expense expense) {

        String sql = """
                INSERT INTO expenses (amount, category, description, expense_date )
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

        ) {

            ps.setDouble(1, expense.getAmount());
            ps.setString(2, expense.getCategory());
            ps.setString(3, expense.getDescription());
            ps.setDate(4, java.sql.Date.valueOf(expense.getDate()));

            ps.executeUpdate();

            // get generated id
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    expense.setId(id);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // READ: GEt all expenses
    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Expense expense = new Expense();
                expense.setId(rs.getInt("id"));
                expense.setAmount(rs.getDouble("amount"));
                expense.setCategory(rs.getString("category"));
                expense.setDescription(rs.getString("description"));
                expense.setDate(rs.getDate("expense_date").toLocalDate());
                expenses.add(expense);

            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching expenses", e);
        }
        return expenses;

    }

    // Delete expense by ID
    public void deleteExpenseById(int id) {
        String sql = "DELETE FROM expenses WHERE id=?";

        try (Connection conn = DBConnection.getConnection();

                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting expense", e);
        }
    }

    // Delete expense by category
    public void deleteExpensesByCategory(String category) {
        String sql = "DELETE FROM expenses WHERE category=?";

        try (Connection conn = DBConnection.getConnection();

                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting expenses of category", e);
        }
    }

    // edit method or Update

    public void updateExpense(Expense expense) {
        String sql = """
                UPDATE expenses SET amount = ?, category =?, description = ?, expense_date =? WHERE id =?
                """;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, expense.getAmount());
            stmt.setString(2, expense.getCategory());
            stmt.setString(3, expense.getDescription());
            stmt.setDate(4, Date.valueOf(expense.getDate()));
            stmt.setInt(5, expense.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating expenses", e);
        }
    }

}
