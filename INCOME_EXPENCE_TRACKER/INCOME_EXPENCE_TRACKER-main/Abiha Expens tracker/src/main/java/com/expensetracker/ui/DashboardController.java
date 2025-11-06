package com.expensetracker.ui;

import com.expensetracker.data.DataManager;
import com.expensetracker.model.Category;
import com.expensetracker.model.Transaction;
import com.expensetracker.model.TransactionType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardController {
    private BorderPane view;
    private DataManager dataManager;
    private VBox contentArea;
    
    // Dashboard summary labels
    private Label balanceLabel;
    private Label incomeLabel;
    private Label expenseLabel;
    
    public DashboardController() {
        this.dataManager = DataManager.getInstance();
        initializeView();
    }
    
    private void initializeView() {
        view = new BorderPane();
        view.getStyleClass().add("root");
        
        // Create sidebar
        VBox sidebar = createSidebar();
        view.setLeft(sidebar);
        
        // Create main content area
        contentArea = new VBox(20);
        contentArea.setPadding(new Insets(30));
        contentArea.getStyleClass().add("content-area");
        
        showDashboard();
        
        ScrollPane scrollPane = new ScrollPane(contentArea);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        view.setCenter(scrollPane);
    }
    
    private VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(20));
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(250);
        
        // App title
        Label titleLabel = new Label("💰 EXPENSE TRACKER");
        titleLabel.getStyleClass().add("app-title");
        
        Region spacer1 = new Region();
        spacer1.setPrefHeight(30);
        
        // Navigation buttons - Simplified to 3 main sections
        Button dashboardBtn = createNavButton("📊 Dashboard", true);
        Button budgetTipsBtn = createNavButton("💡 Budget Tips", false);
        Button financialHelpBtn = createNavButton("🆘 Financial Help", false);
        
        // Button actions
        dashboardBtn.setOnAction(e -> {
            resetNavButtons(sidebar);
            dashboardBtn.getStyleClass().add("nav-button-active");
            showDashboard();
        });
        
        budgetTipsBtn.setOnAction(e -> {
            resetNavButtons(sidebar);
            budgetTipsBtn.getStyleClass().add("nav-button-active");
            showBudgetTips();
        });
        
        financialHelpBtn.setOnAction(e -> {
            resetNavButtons(sidebar);
            financialHelpBtn.getStyleClass().add("nav-button-active");
            showFinancialHelp();
        });
        
        sidebar.getChildren().addAll(
            titleLabel,
            spacer1,
            dashboardBtn,
            budgetTipsBtn,
            financialHelpBtn
        );
        
        return sidebar;
    }
    
    private Button createNavButton(String text, boolean active) {
        Button btn = new Button(text);
        btn.getStyleClass().add("nav-button");
        if (active) {
            btn.getStyleClass().add("nav-button-active");
        }
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        return btn;
    }
    
    private void resetNavButtons(VBox sidebar) {
        sidebar.getChildren().forEach(node -> {
            if (node instanceof Button) {
                node.getStyleClass().remove("nav-button-active");
            }
        });
    }
    
    private void showDashboard() {
        contentArea.getChildren().clear();
        
        // Search Bar
        HBox searchBar = createSearchBar();
        
        // Quick Add Transaction Section
        VBox addTransactionSection = createQuickAddTransaction();
        
        // Category Buttons
        VBox categorySection = createCategorySection();
        
        // Info boxes row (Upcoming Expenses, Average Daily Spending, Calendar)
        HBox infoBoxesRow = createInfoBoxesRow();
        
        // Summary cards
        HBox summaryCards = createSummaryCards();
        
        // Recent transactions
        VBox recentTransactions = createRecentTransactionsSection();
        
        // Charts Section
        Label chartsTitle = new Label("📊 Visual Analytics");
        chartsTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2D3748; -fx-padding: 20 0 10 0;");
        
        VBox lineChartBox = createSpendingTrendChart();
        HBox chartsRow = new HBox(20);
        chartsRow.getChildren().addAll(createCategoryPieChart(), createMonthlyBarChart());
        
        contentArea.getChildren().addAll(searchBar, addTransactionSection, categorySection, infoBoxesRow, summaryCards, recentTransactions, chartsTitle, lineChartBox, chartsRow);
        updateDashboardValues();
    }
    
    private HBox createSearchBar() {
        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setPadding(new Insets(10, 0, 10, 0));
        
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search transactions...");
        searchField.getStyleClass().add("dashboard-search");
        searchField.setPrefWidth(400);
        HBox.setHgrow(searchField, Priority.ALWAYS);
        
        Button searchButton = new Button("Search");
        searchButton.getStyleClass().add("search-button");
        
        searchButton.setOnAction(e -> {
            String query = searchField.getText().toLowerCase();
            if (!query.isEmpty()) {
                // Filter and show matching transactions
                showSearchResults(query);
            }
        });
        
        searchField.setOnAction(e -> searchButton.fire());
        
        searchBar.getChildren().addAll(searchField, searchButton);
        return searchBar;
    }
    
    private VBox createQuickAddTransaction() {
        VBox container = new VBox(15);
        container.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 15; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        container.setPadding(new Insets(20));
        
        Label title = new Label("➕ Add New Transaction");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2D3748;");
        
        // Type selection
        HBox typeBox = new HBox(15);
        typeBox.setAlignment(Pos.CENTER_LEFT);
        ToggleGroup typeGroup = new ToggleGroup();
        RadioButton incomeRadio = new RadioButton("💰 Income");
        incomeRadio.setToggleGroup(typeGroup);
        RadioButton expenseRadio = new RadioButton("💸 Expense");
        expenseRadio.setToggleGroup(typeGroup);
        expenseRadio.setSelected(true);
        typeBox.getChildren().addAll(incomeRadio, expenseRadio);
        
        // Form fields row
        HBox fieldsRow = new HBox(15);
        fieldsRow.setAlignment(Pos.CENTER_LEFT);
        
        TextField amountField = new TextField();
        amountField.setPromptText("Amount (₹)");
        amountField.setPrefWidth(150);
        
        ComboBox<Category> categoryCombo = new ComboBox<>();
        categoryCombo.setPromptText("Category");
        categoryCombo.setPrefWidth(200);
        
        TextField descField = new TextField();
        descField.setPromptText("Description");
        descField.setPrefWidth(250);
        
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setPrefWidth(150);
        
        Button addBtn = new Button("Add");
        addBtn.getStyleClass().add("primary-button");
        addBtn.setStyle("-fx-font-size: 14px; -fx-padding: 10 25;");
        
        fieldsRow.getChildren().addAll(amountField, categoryCombo, descField, datePicker, addBtn);
        
        // Update categories based on type
        Runnable updateCategories = () -> {
            categoryCombo.getItems().clear();
            if (incomeRadio.isSelected()) {
                categoryCombo.getItems().addAll(Category.getIncomeCategories());
            } else {
                categoryCombo.getItems().addAll(Category.getExpenseCategories());
            }
        };
        
        incomeRadio.setOnAction(e -> updateCategories.run());
        expenseRadio.setOnAction(e -> updateCategories.run());
        updateCategories.run(); // Initialize with expense categories
        
        // Add button action
        addBtn.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                Category category = categoryCombo.getValue();
                String description = descField.getText().trim();
                LocalDate date = datePicker.getValue();
                
                if (category == null) {
                    showAlert("Please select a category");
                    return;
                }
                
                if (description.isEmpty()) {
                    description = category.getDisplayName();
                }
                
                TransactionType type = incomeRadio.isSelected() ? TransactionType.INCOME : TransactionType.EXPENSE;
                Transaction transaction = new Transaction(type, category, amount, description, date);
                dataManager.addTransaction(transaction);
                
                // Clear form
                amountField.clear();
                categoryCombo.setValue(null);
                descField.clear();
                datePicker.setValue(LocalDate.now());
                expenseRadio.setSelected(true);
                
                // Refresh dashboard
                refreshCurrentView();
                
            } catch (NumberFormatException ex) {
                showAlert("Please enter a valid amount");
            }
        });
        
        container.getChildren().addAll(title, typeBox, fieldsRow);
        return container;
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private VBox createCategorySection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(10, 0, 10, 0));
        
        Label categoryTitle = new Label("Quick Categories");
        categoryTitle.getStyleClass().add("category-section-title");
        
        // Category buttons grid
        FlowPane categoryGrid = new FlowPane();
        categoryGrid.setHgap(10);
        categoryGrid.setVgap(10);
        categoryGrid.getStyleClass().add("category-grid");
        
        Category[] expenseCategories = Category.getExpenseCategories();
        for (Category cat : expenseCategories) {
            Button catButton = createCategoryButton(cat);
            categoryGrid.getChildren().add(catButton);
        }
        
        section.getChildren().addAll(categoryTitle, categoryGrid);
        return section;
    }
    
    private Button createCategoryButton(Category category) {
        Button btn = new Button(category.getEmoji() + " " + category.getDisplayName());
        btn.getStyleClass().add("category-button");
        btn.setStyle("-fx-border-color: " + category.getColor() + "; -fx-border-width: 2;");
        
        btn.setOnAction(e -> {
            // Filter transactions by this category
            showCategoryTransactions(category);
        });
        
        return btn;
    }
    
    private HBox createInfoBoxesRow() {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER);
        row.setPadding(new Insets(10, 0, 20, 0));
        
        // Upcoming Expenses Box
        VBox upcomingBox = createUpcomingExpensesBox();
        
        // Average Daily Spending Box
        VBox avgSpendingBox = createAvgSpendingBox();
        
        // Calendar Box
        VBox calendarBox = createCalendarBox();
        
        row.getChildren().addAll(upcomingBox, avgSpendingBox, calendarBox);
        return row;
    }
    
    private VBox createUpcomingExpensesBox() {
        VBox box = new VBox(10);
        box.getStyleClass().add("info-box");
        box.setStyle("-fx-background-color: linear-gradient(to bottom right, #667EEA, #764BA2);");
        box.setPadding(new Insets(20));
        box.setPrefWidth(250);
        HBox.setHgrow(box, Priority.ALWAYS);
        
        Label icon = new Label("📅");
        icon.setStyle("-fx-font-size: 32px;");
        
        Label titleLabel = new Label("Upcoming Expenses");
        titleLabel.getStyleClass().add("info-box-title");
        titleLabel.setStyle("-fx-text-fill: white;");
        
        // Calculate upcoming expenses (next 7 days)
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);
        List<Transaction> upcomingTransactions = dataManager.getTransactionsByDateRange(today, nextWeek)
            .stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .collect(Collectors.toList());
        
        double upcomingTotal = upcomingTransactions.stream()
            .mapToDouble(Transaction::getAmount)
            .sum();
        
        Label valueLabel = new Label(String.format("₹%.2f", upcomingTotal));
        valueLabel.getStyleClass().add("info-box-value");
        valueLabel.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");
        
        Label subLabel = new Label("Next 7 days");
        subLabel.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.8); -fx-font-size: 12px;");
        
        box.getChildren().addAll(icon, titleLabel, valueLabel, subLabel);
        return box;
    }
    
    private VBox createAvgSpendingBox() {
        VBox box = new VBox(10);
        box.getStyleClass().add("info-box");
        box.setStyle("-fx-background-color: linear-gradient(to bottom right, #F093FB, #F5576C);");
        box.setPadding(new Insets(20));
        box.setPrefWidth(250);
        HBox.setHgrow(box, Priority.ALWAYS);
        
        Label icon = new Label("💰");
        icon.setStyle("-fx-font-size: 32px;");
        
        Label titleLabel = new Label("Avg Daily Spending");
        titleLabel.getStyleClass().add("info-box-title");
        titleLabel.setStyle("-fx-text-fill: white;");
        
        // Calculate average daily spending for last 30 days
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysAgo = today.minusDays(30);
        List<Transaction> recentExpenses = dataManager.getTransactionsByDateRange(thirtyDaysAgo, today)
            .stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .collect(Collectors.toList());
        
        double totalExpense = recentExpenses.stream()
            .mapToDouble(Transaction::getAmount)
            .sum();
        double avgDaily = totalExpense / 30.0;
        
        Label valueLabel = new Label(String.format("₹%.2f", avgDaily));
        valueLabel.getStyleClass().add("info-box-value");
        valueLabel.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");
        
        Label subLabel = new Label("Last 30 days");
        subLabel.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.8); -fx-font-size: 12px;");
        
        box.getChildren().addAll(icon, titleLabel, valueLabel, subLabel);
        return box;
    }
    
    private VBox createCalendarBox() {
        VBox box = new VBox(10);
        box.getStyleClass().add("info-box");
        box.setStyle("-fx-background-color: linear-gradient(to bottom right, #4FACFE, #00F2FE);");
        box.setPadding(new Insets(20));
        box.setPrefWidth(250);
        box.setAlignment(Pos.CENTER);
        HBox.setHgrow(box, Priority.ALWAYS);
        
        Label icon = new Label("📆");
        icon.setStyle("-fx-font-size: 32px;");
        
        Label dateLabel = new Label();
        dateLabel.getStyleClass().add("calendar-date");
        dateLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label timeLabel = new Label();
        timeLabel.getStyleClass().add("calendar-time");
        timeLabel.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.9); -fx-font-size: 18px;");
        
        Label dayLabel = new Label();
        dayLabel.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.8); -fx-font-size: 14px;");
        
        // Update time every second
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            LocalDateTime now = LocalDateTime.now();
            dateLabel.setText(now.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
            timeLabel.setText(now.format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
            dayLabel.setText(now.format(DateTimeFormatter.ofPattern("EEEE")));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        
        // Initial update
        LocalDateTime now = LocalDateTime.now();
        dateLabel.setText(now.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        timeLabel.setText(now.format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
        dayLabel.setText(now.format(DateTimeFormatter.ofPattern("EEEE")));
        
        box.getChildren().addAll(icon, dateLabel, timeLabel, dayLabel);
        return box;
    }
    
    private void showSearchResults(String query) {
        HistoryView historyView = new HistoryView(this);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(historyView.getView());
        // The HistoryView will handle the search internally
    }
    
    private void showCategoryTransactions(Category category) {
        HistoryView historyView = new HistoryView(this);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(historyView.getView());
    }
    
    private HBox createSummaryCards() {
        HBox cardContainer = new HBox(20);
        cardContainer.setAlignment(Pos.CENTER);
        
        // Balance Card
        VBox balanceCard = createSummaryCard("Total Balance", "₹0.00", "balance-card");
        balanceLabel = (Label) balanceCard.getChildren().get(1);
        
        // Income Card
        VBox incomeCard = createSummaryCard("Total Income", "₹0.00", "income-card");
        incomeLabel = (Label) incomeCard.getChildren().get(1);
        
        // Expense Card
        VBox expenseCard = createSummaryCard("Total Expenses", "₹0.00", "expense-card");
        expenseLabel = (Label) expenseCard.getChildren().get(1);
        
        cardContainer.getChildren().addAll(balanceCard, incomeCard, expenseCard);
        return cardContainer;
    }
    
    private VBox createSummaryCard(String title, String value, String styleClass) {
        VBox card = new VBox(10);
        card.getStyleClass().addAll("summary-card", styleClass);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        HBox.setHgrow(card, Priority.ALWAYS);
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");
        
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("card-value");
        
        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }
    
    private VBox createRecentTransactionsSection() {
        VBox section = new VBox(15);
        
        Label sectionTitle = new Label("Recent Transactions");
        sectionTitle.getStyleClass().add("subsection-title");
        
        VBox transactionsList = new VBox(10);
        transactionsList.getStyleClass().add("transactions-list");
        
        List<Transaction> recentTransactions = dataManager.getAllTransactions();
        if (recentTransactions.isEmpty()) {
            Label emptyLabel = new Label("No transactions yet. Add your first transaction!");
            emptyLabel.getStyleClass().add("empty-message");
            transactionsList.getChildren().add(emptyLabel);
        } else {
            // Show last 5 transactions
            int count = Math.min(5, recentTransactions.size());
            for (int i = recentTransactions.size() - 1; i >= recentTransactions.size() - count; i--) {
                Transaction t = recentTransactions.get(i);
                HBox transactionItem = createTransactionItem(t);
                transactionsList.getChildren().add(transactionItem);
            }
        }
        
        section.getChildren().addAll(sectionTitle, transactionsList);
        return section;
    }
    
    private HBox createTransactionItem(Transaction transaction) {
        HBox item = new HBox(15);
        item.getStyleClass().add("transaction-item");
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
        
        item.getChildren().addAll(iconLabel, details, dateLabel, amountLabel);
        return item;
    }
    
    private void showAddTransaction() {
        AddTransactionView addView = new AddTransactionView(this);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(addView.getView());
    }
    
    private void showHistory() {
        HistoryView historyView = new HistoryView(this);
        contentArea.getChildren().clear();
        contentArea.getChildren().add(historyView.getView());
    }
    
    private void showReports() {
        ReportsView reportsView = new ReportsView();
        contentArea.getChildren().clear();
        contentArea.getChildren().add(reportsView.getView());
    }
    
    private void showBudgetTips() {
        BudgetTipsView tipsView = new BudgetTipsView();
        contentArea.getChildren().clear();
        contentArea.getChildren().add(tipsView.getView());
    }
    
    private void showFinancialHelp() {
        FinancialHelpView helpView = new FinancialHelpView();
        contentArea.getChildren().clear();
        contentArea.getChildren().add(helpView.getView());
    }
    
    public void updateDashboardValues() {
        double balance = dataManager.getBalance();
        double income = dataManager.getTotalIncome();
        double expense = dataManager.getTotalExpense();
        
        if (balanceLabel != null) {
            balanceLabel.setText(String.format("₹%.2f", balance));
        }
        if (incomeLabel != null) {
            incomeLabel.setText(String.format("₹%.2f", income));
        }
        if (expenseLabel != null) {
            expenseLabel.setText(String.format("₹%.2f", expense));
        }
    }
    
    public void refreshCurrentView() {
        // Refresh the dashboard to show updated data
        showDashboard();
    }
    
    private VBox createSpendingTrendChart() {
        VBox container = new VBox(15);
        container.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 15; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        
        Label chartTitle = new Label("📈 Spending Trend - Last 30 Days");
        chartTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2D3748;");
        
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount (₹)");
        
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setLegendSide(Side.TOP);
        lineChart.setPrefHeight(350);
        lineChart.setAnimated(true);
        
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expenses");
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");
        
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29);
        
        Map<LocalDate, Double> dailyExpenses = new HashMap<>();
        Map<LocalDate, Double> dailyIncome = new HashMap<>();
        
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dailyExpenses.put(date, 0.0);
            dailyIncome.put(date, 0.0);
        }
        
        List<Transaction> transactions = dataManager.getTransactionsByDateRange(startDate, endDate);
        for (Transaction t : transactions) {
            if (t.getType() == TransactionType.EXPENSE) {
                dailyExpenses.merge(t.getDate(), t.getAmount(), Double::sum);
            } else {
                dailyIncome.merge(t.getDate(), t.getAmount(), Double::sum);
            }
        }
        
        int count = 0;
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (count % 3 == 0 || date.equals(endDate)) {
                String dateStr = date.format(DateTimeFormatter.ofPattern("dd MMM"));
                expenseSeries.getData().add(new XYChart.Data<>(dateStr, dailyExpenses.get(date)));
                incomeSeries.getData().add(new XYChart.Data<>(dateStr, dailyIncome.get(date)));
            }
            count++;
        }
        
        lineChart.getData().addAll(expenseSeries, incomeSeries);
        container.getChildren().addAll(chartTitle, lineChart);
        return container;
    }
    
    private VBox createCategoryPieChart() {
        VBox container = new VBox(15);
        container.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 15; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        container.setPrefWidth(500);
        
        Label chartTitle = new Label("🍰 Expense Breakdown");
        chartTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2D3748;");
        
        PieChart pieChart = new PieChart();
        pieChart.setLegendSide(Side.RIGHT);
        pieChart.setPrefHeight(350);
        pieChart.setLabelsVisible(true);
        pieChart.setAnimated(true);
        
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        List<Transaction> monthTransactions = dataManager.getTransactionsByDateRange(startOfMonth, now);
        
        Map<Category, Double> categoryTotals = new HashMap<>();
        monthTransactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .forEach(t -> categoryTotals.merge(t.getCategory(), t.getAmount(), Double::sum));
        
        if (!categoryTotals.isEmpty()) {
            double total = categoryTotals.values().stream().mapToDouble(Double::doubleValue).sum();
            categoryTotals.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .forEach(entry -> {
                    double percentage = (entry.getValue() / total) * 100;
                    String label = String.format("%s (%.1f%%)", 
                        entry.getKey().getEmoji() + " " + entry.getKey().getDisplayName(), 
                        percentage);
                    pieChart.getData().add(new PieChart.Data(label, entry.getValue()));
                });
        } else {
            pieChart.getData().add(new PieChart.Data("No expenses yet", 1));
        }
        
        container.getChildren().addAll(chartTitle, pieChart);
        return container;
    }
    
    private VBox createMonthlyBarChart() {
        VBox container = new VBox(15);
        container.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 15; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        container.setPrefWidth(500);
        
        Label chartTitle = new Label("📊 Monthly Comparison - Last 6 Months");
        chartTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2D3748;");
        
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount (₹)");
        
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setLegendSide(Side.TOP);
        barChart.setPrefHeight(350);
        barChart.setAnimated(true);
        barChart.setCategoryGap(20);
        barChart.setBarGap(5);
        
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expenses");
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");
        
        YearMonth currentMonth = YearMonth.now();
        
        for (int i = 5; i >= 0; i--) {
            YearMonth month = currentMonth.minusMonths(i);
            LocalDate startDate = month.atDay(1);
            LocalDate endDate = month.atEndOfMonth();
            
            List<Transaction> monthTransactions = dataManager.getTransactionsByDateRange(startDate, endDate);
            
            double monthExpense = monthTransactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
            
            double monthIncome = monthTransactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
            
            String monthLabel = month.format(DateTimeFormatter.ofPattern("MMM yyyy"));
            expenseSeries.getData().add(new XYChart.Data<>(monthLabel, monthExpense));
            incomeSeries.getData().add(new XYChart.Data<>(monthLabel, monthIncome));
        }
        
        barChart.getData().addAll(incomeSeries, expenseSeries);
        container.getChildren().addAll(chartTitle, barChart);
        return container;
    }
    
    public BorderPane getView() {
        return view;
    }
}
