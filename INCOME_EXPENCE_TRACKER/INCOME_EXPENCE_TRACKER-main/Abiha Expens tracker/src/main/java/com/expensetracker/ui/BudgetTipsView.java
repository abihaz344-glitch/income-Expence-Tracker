package com.expensetracker.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class BudgetTipsView {
    private VBox view;
    
    public BudgetTipsView() {
        initializeView();
    }
    
    private void initializeView() {
        view = new VBox(25);
        view.setPadding(new Insets(20));
        
        // Title
        Label title = new Label("💡 Smart Budget Tips");
        title.getStyleClass().add("section-title");
        
        Label subtitle = new Label("Practical tips to save money and manage your finances better");
        subtitle.getStyleClass().add("subsection-title");
        subtitle.setStyle("-fx-text-fill: #718096;");
        
        // Tips container
        VBox tipsContainer = new VBox(15);
        
        // Create tip cards
        tipsContainer.getChildren().addAll(
            createTipCard("🎯 Follow the 50/30/20 Rule", 
                "Allocate 50% of income to needs, 30% to wants, and 20% to savings and debt repayment. " +
                "This simple rule helps maintain financial balance.", "#667EEA"),
            
            createTipCard("🏦 Build an Emergency Fund", 
                "Save at least 3-6 months of expenses in an emergency fund. Start small - even ₹500/month " +
                "adds up! Keep it in a separate savings account.", "#48BB78"),
            
            createTipCard("🛒 Use the 24-Hour Rule", 
                "Wait 24 hours before making non-essential purchases. This helps avoid impulse buying " +
                "and gives you time to decide if you really need it.", "#F56565"),
            
            createTipCard("📝 Track Every Expense", 
                "Use this app daily to record all transactions. Awareness is the first step to control. " +
                "Review your spending weekly to identify areas to cut back.", "#ED8936"),
            
            createTipCard("🍽️ Cook at Home More Often", 
                "Eating out can cost 3-4x more than home cooking. Plan meals, buy groceries in bulk, " +
                "and prep meals in advance. Save ₹5000-10000/month!", "#38B2AC"),
            
            createTipCard("💳 Pay Off High-Interest Debt First", 
                "Focus on clearing credit card debt and personal loans first as they have highest interest rates. " +
                "Use the avalanche method for maximum savings.", "#9F7AEA"),
            
            createTipCard("🔄 Automate Your Savings", 
                "Set up automatic transfers to savings account right after salary credit. " +
                "Pay yourself first - treat savings as a non-negotiable expense.", "#EC4899"),
            
            createTipCard("🎁 Use Cash for Discretionary Spending", 
                "Withdraw a fixed amount for entertainment, dining, and shopping. When cash runs out, " +
                "stop spending. It's harder to overspend with physical money.", "#10B981"),
            
            createTipCard("📱 Cancel Unused Subscriptions", 
                "Review all subscriptions monthly. Cancel streaming services, gym memberships, and apps " +
                "you don't use regularly. Save ₹2000-5000/month easily!", "#F59E0B"),
            
            createTipCard("🚇 Use Public Transport", 
                "Switching from cab/own vehicle to public transport even 2-3 days a week can save " +
                "₹3000-8000/month on fuel and maintenance.", "#6366F1"),
            
            createTipCard("💰 Set Specific Financial Goals", 
                "Define clear goals: emergency fund, vacation, new phone, etc. Having targets makes " +
                "saving purposeful and motivating. Track progress regularly!", "#8B5CF6"),
            
            createTipCard("🏷️ Compare Prices Before Buying", 
                "Always check prices across multiple stores and online platforms. Use price comparison " +
                "apps and wait for sales. Save 20-40% on major purchases!", "#EF4444")
        );
        
        ScrollPane scrollPane = new ScrollPane(tipsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("tips-scroll");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        view.getChildren().addAll(title, subtitle, scrollPane);
    }
    
    private VBox createTipCard(String tipTitle, String tipContent, String accentColor) {
        VBox card = new VBox(12);
        card.getStyleClass().add("tip-card");
        card.setPadding(new Insets(20));
        card.setStyle("-fx-border-color: " + accentColor + "; -fx-border-width: 0 0 0 4;");
        
        Label titleLabel = new Label(tipTitle);
        titleLabel.getStyleClass().add("tip-title");
        titleLabel.setWrapText(true);
        
        Label contentLabel = new Label(tipContent);
        contentLabel.getStyleClass().add("tip-content");
        contentLabel.setWrapText(true);
        
        card.getChildren().addAll(titleLabel, contentLabel);
        return card;
    }
    
    public VBox getView() {
        return view;
    }
}
