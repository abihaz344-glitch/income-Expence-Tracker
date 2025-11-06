package com.expensetracker.ui;

import com.expensetracker.data.DataManager;
import com.expensetracker.model.Category;
import com.expensetracker.model.Transaction;
import com.expensetracker.model.TransactionType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;

public class AddTransactionView {
    private VBox view;
    private DataManager dataManager;
    private DashboardController dashboardController;
    
    private ToggleGroup typeGroup;
    private ComboBox<Category> categoryComboBox;
    private TextField amountField;
    private TextField descriptionField;
    private DatePicker datePicker;
    
    public AddTransactionView(DashboardController dashboardController) {
        this.dataManager = DataManager.getInstance();
        this.dashboardController = dashboardController;
        initializeView();
    }
    
    private void initializeView() {
        view = new VBox(25);
        view.setPadding(new Insets(20));
        view.getStyleClass().add("add-transaction-view");
        
        // Title
        Label title = new Label("Add New Transaction");
        title.getStyleClass().add("section-title");
        
        // Form container
        VBox formContainer = new VBox(20);
        formContainer.setMaxWidth(600);
        formContainer.getStyleClass().add("form-container");
        formContainer.setPadding(new Insets(30));
        
        // Transaction Type Selection
        Label typeLabel = new Label("Transaction Type");
        typeLabel.getStyleClass().add("form-label");
        
        HBox typeButtons = new HBox(15);
        typeGroup = new ToggleGroup();
        
        RadioButton incomeRadio = new RadioButton("💰 Income");
        incomeRadio.setToggleGroup(typeGroup);
        incomeRadio.setUserData(TransactionType.INCOME);
        incomeRadio.getStyleClass().add("type-radio");
        
        RadioButton expenseRadio = new RadioButton("💸 Expense");
        expenseRadio.setToggleGroup(typeGroup);
        expenseRadio.setUserData(TransactionType.EXPENSE);
        expenseRadio.setSelected(true);
        expenseRadio.getStyleClass().add("type-radio");
        
        typeButtons.getChildren().addAll(incomeRadio, expenseRadio);
        
        // Category Selection
        Label categoryLabel = new Label("Category");
        categoryLabel.getStyleClass().add("form-label");
        
        categoryComboBox = new ComboBox<>();
        categoryComboBox.getStyleClass().add("form-input");
        categoryComboBox.setMaxWidth(Double.MAX_VALUE);
        categoryComboBox.setPromptText("Select a category");
        
        // Update categories when type changes
        typeGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateCategories((TransactionType) newVal.getUserData());
            }
        });
        updateCategories(TransactionType.EXPENSE);
        
        // Amount
        Label amountLabel = new Label("Amount (₹)");
        amountLabel.getStyleClass().add("form-label");
        
        amountField = new TextField();
        amountField.setPromptText("0.00");
        amountField.getStyleClass().add("form-input");
        
        // Description
        Label descLabel = new Label("Description");
        descLabel.getStyleClass().add("form-label");
        
        descriptionField = new TextField();
        descriptionField.setPromptText("Enter description");
        descriptionField.getStyleClass().add("form-input");
        
        // Date
        Label dateLabel = new Label("Date");
        dateLabel.getStyleClass().add("form-label");
        
        datePicker = new DatePicker(LocalDate.now());
        datePicker.getStyleClass().add("form-input");
        datePicker.setMaxWidth(Double.MAX_VALUE);
        
        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        
        Button addButton = new Button("Add Transaction");
        addButton.getStyleClass().add("primary-button");
        addButton.setOnAction(e -> handleAddTransaction());
        
        Button clearButton = new Button("Clear");
        clearButton.getStyleClass().add("secondary-button");
        clearButton.setOnAction(e -> clearForm());
        
        buttonBox.getChildren().addAll(addButton, clearButton);
        
        formContainer.getChildren().addAll(
            typeLabel, typeButtons,
            categoryLabel, categoryComboBox,
            amountLabel, amountField,
            descLabel, descriptionField,
            dateLabel, datePicker,
            buttonBox
        );
        
        view.getChildren().addAll(title, formContainer);
    }
    
    private void updateCategories(TransactionType type) {
        categoryComboBox.getItems().clear();
        if (type == TransactionType.INCOME) {
            categoryComboBox.getItems().addAll(Category.getIncomeCategories());
        } else {
            categoryComboBox.getItems().addAll(Category.getExpenseCategories());
        }
        categoryComboBox.setButtonCell(new CategoryListCell());
        categoryComboBox.setCellFactory(lv -> new CategoryListCell());
    }
    
    private void handleAddTransaction() {
        // Validate inputs
        if (typeGroup.getSelectedToggle() == null) {
            showAlert("Please select transaction type");
            return;
        }
        
        if (categoryComboBox.getValue() == null) {
            showAlert("Please select a category");
            return;
        }
        
        if (amountField.getText().trim().isEmpty()) {
            showAlert("Please enter an amount");
            return;
        }
        
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) {
                showAlert("Amount must be greater than 0");
                return;
            }
            
            TransactionType type = (TransactionType) typeGroup.getSelectedToggle().getUserData();
            Category category = categoryComboBox.getValue();
            String description = descriptionField.getText().trim();
            if (description.isEmpty()) {
                description = category.getDisplayName();
            }
            LocalDate date = datePicker.getValue();
            
            Transaction transaction = new Transaction(type, category, amount, description, date);
            dataManager.addTransaction(transaction);
            
            showSuccessAlert("Transaction added successfully!");
            clearForm();
            // Refresh entire dashboard to update charts automatically
            dashboardController.refreshCurrentView();
            
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid amount");
        }
    }
    
    private void clearForm() {
        typeGroup.selectToggle(typeGroup.getToggles().get(1)); // Select Expense
        categoryComboBox.setValue(null);
        amountField.clear();
        descriptionField.clear();
        datePicker.setValue(LocalDate.now());
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public VBox getView() {
        return view;
    }
    
    // Custom cell for category display
    private static class CategoryListCell extends ListCell<Category> {
        @Override
        protected void updateItem(Category category, boolean empty) {
            super.updateItem(category, empty);
            if (empty || category == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(category.getFullDisplay());
            }
        }
    }
}
