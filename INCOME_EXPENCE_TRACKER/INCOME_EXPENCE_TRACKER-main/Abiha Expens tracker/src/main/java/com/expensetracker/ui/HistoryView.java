package com.expensetracker.ui;

import com.expensetracker.data.DataManager;
import com.expensetracker.model.Transaction;
import com.expensetracker.model.TransactionType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;
import java.util.stream.Collectors;

public class HistoryView {
    private VBox view;
    private DataManager dataManager;
    private DashboardController dashboardController;
    private VBox transactionsList;
    private TextField searchField;
    private ComboBox<String> filterComboBox;
    
    public HistoryView(DashboardController dashboardController) {
        this.dataManager = DataManager.getInstance();
        this.dashboardController = dashboardController;
        initializeView();
    }
    
    private void initializeView() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        
        // Title
        Label title = new Label("Transaction History");
        title.getStyleClass().add("section-title");
        
        // Search and filter bar
        HBox filterBar = new HBox(15);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        
        searchField = new TextField();
        searchField.setPromptText("🔍 Search transactions...");
        searchField.getStyleClass().add("search-field");
        searchField.setPrefWidth(300);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterTransactions());
        
        filterComboBox = new ComboBox<>();
        filterComboBox.getItems().addAll("All", "Income", "Expenses");
        filterComboBox.setValue("All");
        filterComboBox.getStyleClass().add("filter-combo");
        filterComboBox.setOnAction(e -> filterTransactions());
        
        Button refreshButton = new Button("🔄 Refresh");
        refreshButton.getStyleClass().add("secondary-button");
        refreshButton.setOnAction(e -> loadTransactions());
        
        filterBar.getChildren().addAll(searchField, filterComboBox, refreshButton);
        
        // Transactions list
        transactionsList = new VBox(10);
        transactionsList.getStyleClass().add("history-list");
        
        ScrollPane scrollPane = new ScrollPane(transactionsList);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("history-scroll");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        view.getChildren().addAll(title, filterBar, scrollPane);
        
        loadTransactions();
    }
    
    private void loadTransactions() {
        transactionsList.getChildren().clear();
        List<Transaction> transactions = dataManager.getAllTransactions();
        
        if (transactions.isEmpty()) {
            Label emptyLabel = new Label("No transactions found. Start by adding your first transaction!");
            emptyLabel.getStyleClass().add("empty-message");
            transactionsList.getChildren().add(emptyLabel);
        } else {
            // Show in reverse order (newest first)
            for (int i = transactions.size() - 1; i >= 0; i--) {
                Transaction t = transactions.get(i);
                HBox item = createTransactionItem(t);
                transactionsList.getChildren().add(item);
            }
        }
    }
    
    private void filterTransactions() {
        String searchText = searchField.getText().toLowerCase();
        String filter = filterComboBox.getValue();
        
        List<Transaction> allTransactions = dataManager.getAllTransactions();
        List<Transaction> filtered = allTransactions.stream()
            .filter(t -> {
                // Filter by type
                if ("Income".equals(filter) && t.getType() != TransactionType.INCOME) {
                    return false;
                }
                if ("Expenses".equals(filter) && t.getType() != TransactionType.EXPENSE) {
                    return false;
                }
                
                // Filter by search text
                if (!searchText.isEmpty()) {
                    return t.getDescription().toLowerCase().contains(searchText) ||
                           t.getCategory().getDisplayName().toLowerCase().contains(searchText) ||
                           t.getFormattedAmount().toLowerCase().contains(searchText);
                }
                
                return true;
            })
            .collect(Collectors.toList());
        
        transactionsList.getChildren().clear();
        
        if (filtered.isEmpty()) {
            Label emptyLabel = new Label("No transactions match your search.");
            emptyLabel.getStyleClass().add("empty-message");
            transactionsList.getChildren().add(emptyLabel);
        } else {
            for (int i = filtered.size() - 1; i >= 0; i--) {
                Transaction t = filtered.get(i);
                HBox item = createTransactionItem(t);
                transactionsList.getChildren().add(item);
            }
        }
    }
    
    private HBox createTransactionItem(Transaction transaction) {
        HBox item = new HBox(15);
        item.getStyleClass().add("history-item");
        item.setPadding(new Insets(15));
        item.setAlignment(Pos.CENTER_LEFT);
        
        // Category icon
        Label iconLabel = new Label(transaction.getCategory().getEmoji());
        iconLabel.getStyleClass().add("transaction-icon");
        iconLabel.setStyle("-fx-background-color: " + transaction.getCategory().getColor() + ";");
        
        // Transaction details
        VBox details = new VBox(5);
        Label categoryLabel = new Label(transaction.getCategory().getDisplayName());
        categoryLabel.getStyleClass().add("transaction-category");
        Label descLabel = new Label(transaction.getDescription());
        descLabel.getStyleClass().add("transaction-description");
        details.getChildren().addAll(categoryLabel, descLabel);
        HBox.setHgrow(details, Priority.ALWAYS);
        
        // Type badge
        Label typeLabel = new Label(transaction.getType().getDisplayName());
        typeLabel.getStyleClass().add("type-badge");
        if (transaction.getType() == TransactionType.INCOME) {
            typeLabel.getStyleClass().add("income-badge");
        } else {
            typeLabel.getStyleClass().add("expense-badge");
        }
        
        // Date
        Label dateLabel = new Label(transaction.getFormattedDate());
        dateLabel.getStyleClass().add("transaction-date");
        
        // Amount
        Label amountLabel = new Label(transaction.getFormattedAmount());
        amountLabel.getStyleClass().add("transaction-amount");
        if (transaction.getType() == TransactionType.INCOME) {
            amountLabel.getStyleClass().add("income-amount");
        } else {
            amountLabel.getStyleClass().add("expense-amount");
        }
        
        // Delete button
        Button deleteBtn = new Button("🗑️");
        deleteBtn.getStyleClass().add("delete-button");
        deleteBtn.setOnAction(e -> handleDelete(transaction));
        
        item.getChildren().addAll(iconLabel, details, typeLabel, dateLabel, amountLabel, deleteBtn);
        return item;
    }
    
    private void handleDelete(Transaction transaction) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Transaction");
        alert.setHeaderText("Are you sure you want to delete this transaction?");
        alert.setContentText(transaction.getDescription() + " - " + transaction.getFormattedAmount());
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                dataManager.deleteTransaction(transaction.getId());
                loadTransactions();
                dashboardController.updateDashboardValues();
            }
        });
    }
    
    public VBox getView() {
        return view;
    }
}
