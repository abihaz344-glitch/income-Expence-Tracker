package com.expensetracker.model;

public enum TransactionType {
    INCOME("Income", "#4CAF50"),
    EXPENSE("Expense", "#F44336");
    
    private final String displayName;
    private final String color;
    
    TransactionType(String displayName, String color) {
        this.displayName = displayName;
        this.color = color;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getColor() {
        return color;
    }
}
