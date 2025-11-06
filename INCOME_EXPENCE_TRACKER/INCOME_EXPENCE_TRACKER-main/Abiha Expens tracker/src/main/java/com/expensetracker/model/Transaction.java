package com.expensetracker.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Transaction {
    private String id;
    private TransactionType type;
    private Category category;
    private double amount;
    private String description;
    private LocalDate date;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");
    
    public Transaction() {
        this.id = UUID.randomUUID().toString();
        this.date = LocalDate.now();
    }
    
    public Transaction(TransactionType type, Category category, double amount, String description, LocalDate date) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public TransactionType getType() {
        return type;
    }
    
    public void setType(TransactionType type) {
        this.type = type;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public String getFormattedDate() {
        return date.format(DATE_FORMATTER);
    }
    
    public String getFormattedAmount() {
        return String.format("₹%.2f", amount);
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s: ₹%.2f (%s)", 
                            getFormattedDate(), 
                            category.getDisplayName(), 
                            amount, 
                            description);
    }
}
