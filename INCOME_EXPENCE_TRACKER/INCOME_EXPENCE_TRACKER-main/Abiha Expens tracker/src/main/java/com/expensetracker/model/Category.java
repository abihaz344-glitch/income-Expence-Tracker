package com.expensetracker.model;

public enum Category {
    // Expense Categories
    GROCERIES("Groceries", "🛒", "#FF6B6B"),
    RESTAURANT("Restaurant", "🍽️", "#FF8C42"),
    ENTERTAINMENT("Entertainment", "🎬", "#95E1D3"),
    TRAVEL("Travel", "✈️", "#6A9CFF"),
    TRANSPORTATION("Transportation", "🚗", "#F38181"),
    SUBSCRIPTION("Subscription", "📱", "#A78BFA"),
    COLLEGE_FEES("College Fees", "🎓", "#FCD34D"),
    RENT("Rent", "🏠", "#4ECDC4"),
    SHOPPING("Shopping", "🛍️", "#AA96DA"),
    HEALTHCARE("Healthcare", "💊", "#FCBAD3"),
    UTILITIES("Utilities", "💡", "#FFD93D"),
    OTHER_EXPENSE("Other", "🔧", "#C7CEEA"),
    
    // Income Categories
    SALARY("Salary", "💰", "#6BCF7F"),
    INVESTMENT("Investment", "📈", "#4D96FF"),
    GIFT("Gift", "🎁", "#FFB6C1"),
    OTHER_INCOME("Other Income", "💵", "#98D8C8");
    
    private final String displayName;
    private final String emoji;
    private final String color;
    
    Category(String displayName, String emoji, String color) {
        this.displayName = displayName;
        this.emoji = emoji;
        this.color = color;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getEmoji() {
        return emoji;
    }
    
    public String getColor() {
        return color;
    }
    
    public String getFullDisplay() {
        return emoji + " " + displayName;
    }
    
    public static Category[] getExpenseCategories() {
        return new Category[]{GROCERIES, RESTAURANT, ENTERTAINMENT, TRAVEL, TRANSPORTATION, 
                            SUBSCRIPTION, COLLEGE_FEES, RENT, SHOPPING, HEALTHCARE, UTILITIES, OTHER_EXPENSE};
    }
    
    public static Category[] getIncomeCategories() {
        return new Category[]{SALARY, INVESTMENT, GIFT, OTHER_INCOME};
    }
}
