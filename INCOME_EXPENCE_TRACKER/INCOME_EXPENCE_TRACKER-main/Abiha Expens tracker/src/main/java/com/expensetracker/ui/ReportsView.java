package com.expensetracker.ui;

import com.expensetracker.data.DataManager;
import com.expensetracker.model.Category;
import com.expensetracker.model.Transaction;
import com.expensetracker.model.TransactionType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportsView {
    private VBox view;
    private DataManager dataManager;
    private VBox reportContent;
    private String currentReportType = "Daily";
    
    public ReportsView() {
        this.dataManager = DataManager.getInstance();
        initializeView();
    }
    
    private void initializeView() {
        view = new VBox(20);
        view.setPadding(new Insets(20));
        
        // Title and Download Button Header
        HBox headerBox = new HBox(20);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label("Financial Reports");
        title.getStyleClass().add("section-title");
        HBox.setHgrow(title, Priority.ALWAYS);
        
        Button downloadBtn = new Button("📥 Download PDF Report");
        downloadBtn.getStyleClass().add("primary-button");
        downloadBtn.setOnAction(e -> downloadCurrentReportAsPDF());
        
        headerBox.getChildren().addAll(title, downloadBtn);
        
        // Report type selector
        HBox reportSelector = new HBox(15);
        reportSelector.setAlignment(Pos.CENTER_LEFT);
        
        Label selectorLabel = new Label("Select Report Type:");
        selectorLabel.getStyleClass().add("form-label");
        
        ToggleGroup reportGroup = new ToggleGroup();
        
        RadioButton dailyRadio = new RadioButton("📅 Daily");
        dailyRadio.setToggleGroup(reportGroup);
        dailyRadio.setSelected(true);
        dailyRadio.getStyleClass().add("report-radio");
        
        RadioButton monthlyRadio = new RadioButton("📊 Monthly");
        monthlyRadio.setToggleGroup(reportGroup);
        monthlyRadio.getStyleClass().add("report-radio");
        
        RadioButton yearlyRadio = new RadioButton("📈 Yearly");
        yearlyRadio.setToggleGroup(reportGroup);
        yearlyRadio.getStyleClass().add("report-radio");
        
        RadioButton categoryRadio = new RadioButton("🏷️ By Category");
        categoryRadio.setToggleGroup(reportGroup);
        categoryRadio.getStyleClass().add("report-radio");
        
        RadioButton chartsRadio = new RadioButton("📊 Charts");
        chartsRadio.setToggleGroup(reportGroup);
        chartsRadio.getStyleClass().add("report-radio");
        
        reportSelector.getChildren().addAll(selectorLabel, dailyRadio, monthlyRadio, yearlyRadio, categoryRadio, chartsRadio);
        
        // Report content area
        reportContent = new VBox(15);
        reportContent.getStyleClass().add("report-content");
        
        ScrollPane scrollPane = new ScrollPane(reportContent);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("report-scroll");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        // Update report when selection changes
        reportGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == dailyRadio) {
                currentReportType = "Daily";
                showDailyReport();
            } else if (newVal == monthlyRadio) {
                currentReportType = "Monthly";
                showMonthlyReport();
            } else if (newVal == yearlyRadio) {
                currentReportType = "Yearly";
                showYearlyReport();
            } else if (newVal == categoryRadio) {
                currentReportType = "Category";
                showCategoryReport();
            } else if (newVal == chartsRadio) {
                currentReportType = "Charts";
                showChartsView();
            }
        });
        
        view.getChildren().addAll(headerBox, reportSelector, scrollPane);
        
        // Show daily report by default
        showDailyReport();
    }
    
    private void showDailyReport() {
        reportContent.getChildren().clear();
        
        Label reportTitle = new Label("Daily Report - " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        reportTitle.getStyleClass().add("report-title");
        
        LocalDate today = LocalDate.now();
        List<Transaction> todayTransactions = dataManager.getTransactionsByDateRange(today, today);
        
        VBox summaryBox = createDailySummary(todayTransactions);
        VBox transactionsList = createTransactionsList(todayTransactions, "Today's Transactions");
        
        reportContent.getChildren().addAll(reportTitle, summaryBox, transactionsList);
    }
    
    private void showMonthlyReport() {
        reportContent.getChildren().clear();
        
        YearMonth currentMonth = YearMonth.now();
        Label reportTitle = new Label("Monthly Report - " + currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        reportTitle.getStyleClass().add("report-title");
        
        LocalDate startOfMonth = currentMonth.atDay(1);
        LocalDate endOfMonth = currentMonth.atEndOfMonth();
        
        List<Transaction> monthTransactions = dataManager.getTransactionsByDateRange(startOfMonth, endOfMonth);
        
        VBox summaryBox = createMonthlySummary(monthTransactions);
        VBox categoryBreakdown = createCategoryBreakdown(monthTransactions);
        VBox transactionsList = createTransactionsList(monthTransactions, "This Month's Transactions");
        
        reportContent.getChildren().addAll(reportTitle, summaryBox, categoryBreakdown, transactionsList);
    }
    
    private void showYearlyReport() {
        reportContent.getChildren().clear();
        
        int currentYear = LocalDate.now().getYear();
        Label reportTitle = new Label("Yearly Report - " + currentYear);
        reportTitle.getStyleClass().add("report-title");
        
        LocalDate startOfYear = LocalDate.of(currentYear, 1, 1);
        LocalDate endOfYear = LocalDate.of(currentYear, 12, 31);
        
        List<Transaction> yearTransactions = dataManager.getTransactionsByDateRange(startOfYear, endOfYear);
        
        VBox summaryBox = createYearlySummary(yearTransactions);
        VBox monthlyBreakdown = createMonthlyBreakdown(yearTransactions, currentYear);
        VBox categoryBreakdown = createCategoryBreakdown(yearTransactions);
        
        reportContent.getChildren().addAll(reportTitle, summaryBox, monthlyBreakdown, categoryBreakdown);
    }
    
    private void showCategoryReport() {
        reportContent.getChildren().clear();
        
        Label reportTitle = new Label("Category-wise Analysis");
        reportTitle.getStyleClass().add("report-title");
        
        List<Transaction> allTransactions = dataManager.getAllTransactions();
        VBox categoryAnalysis = createDetailedCategoryAnalysis(allTransactions);
        
        reportContent.getChildren().addAll(reportTitle, categoryAnalysis);
    }
    
    private VBox createDailySummary(List<Transaction> transactions) {
        VBox summary = new VBox(15);
        summary.getStyleClass().add("report-summary");
        summary.setPadding(new Insets(20));
        
        double income = transactions.stream()
            .filter(t -> t.getType() == TransactionType.INCOME)
            .mapToDouble(Transaction::getAmount)
            .sum();
            
        double expense = transactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .mapToDouble(Transaction::getAmount)
            .sum();
        
        HBox statsBox = new HBox(20);
        statsBox.setAlignment(Pos.CENTER);
        
        VBox incomeBox = createStatBox("Income", String.format("₹%.2f", income), "income-stat");
        VBox expenseBox = createStatBox("Expenses", String.format("₹%.2f", expense), "expense-stat");
        VBox netBox = createStatBox("Net", String.format("₹%.2f", income - expense), "net-stat");
        
        statsBox.getChildren().addAll(incomeBox, expenseBox, netBox);
        summary.getChildren().add(statsBox);
        
        return summary;
    }
    
    private VBox createMonthlySummary(List<Transaction> transactions) {
        return createDailySummary(transactions); // Same structure, different data
    }
    
    private VBox createYearlySummary(List<Transaction> transactions) {
        VBox summary = new VBox(15);
        summary.getStyleClass().add("report-summary");
        summary.setPadding(new Insets(20));
        
        double income = transactions.stream()
            .filter(t -> t.getType() == TransactionType.INCOME)
            .mapToDouble(Transaction::getAmount)
            .sum();
            
        double expense = transactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .mapToDouble(Transaction::getAmount)
            .sum();
        
        int transactionCount = transactions.size();
        double avgMonthlyExpense = expense / 12;
        
        HBox statsBox = new HBox(20);
        statsBox.setAlignment(Pos.CENTER);
        
        VBox incomeBox = createStatBox("Total Income", String.format("₹%.2f", income), "income-stat");
        VBox expenseBox = createStatBox("Total Expenses", String.format("₹%.2f", expense), "expense-stat");
        VBox avgBox = createStatBox("Avg Monthly", String.format("₹%.2f", avgMonthlyExpense), "avg-stat");
        VBox countBox = createStatBox("Transactions", String.valueOf(transactionCount), "count-stat");
        
        statsBox.getChildren().addAll(incomeBox, expenseBox, avgBox, countBox);
        summary.getChildren().add(statsBox);
        
        return summary;
    }
    
    private VBox createStatBox(String label, String value, String styleClass) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        box.getStyleClass().addAll("stat-box", styleClass);
        box.setPadding(new Insets(15));
        HBox.setHgrow(box, Priority.ALWAYS);
        
        Label titleLabel = new Label(label);
        titleLabel.getStyleClass().add("stat-label");
        
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("stat-value");
        
        box.getChildren().addAll(titleLabel, valueLabel);
        return box;
    }
    
    private VBox createCategoryBreakdown(List<Transaction> transactions) {
        VBox breakdown = new VBox(10);
        breakdown.setPadding(new Insets(20));
        
        Label title = new Label("Category Breakdown");
        title.getStyleClass().add("subsection-title");
        
        Map<Category, Double> categoryTotals = new HashMap<>();
        
        transactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .forEach(t -> {
                categoryTotals.merge(t.getCategory(), t.getAmount(), Double::sum);
            });
        
        VBox categoriesBox = new VBox(8);
        
        categoryTotals.entrySet().stream()
            .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
            .forEach(entry -> {
                HBox categoryItem = createCategoryItem(entry.getKey(), entry.getValue());
                categoriesBox.getChildren().add(categoryItem);
            });
        
        if (categoriesBox.getChildren().isEmpty()) {
            Label emptyLabel = new Label("No expense data available");
            emptyLabel.getStyleClass().add("empty-message");
            categoriesBox.getChildren().add(emptyLabel);
        }
        
        breakdown.getChildren().addAll(title, categoriesBox);
        return breakdown;
    }
    
    private HBox createCategoryItem(Category category, double amount) {
        HBox item = new HBox(10);
        item.setAlignment(Pos.CENTER_LEFT);
        item.getStyleClass().add("category-item");
        item.setPadding(new Insets(10));
        
        Label icon = new Label(category.getEmoji());
        icon.getStyleClass().add("category-icon-small");
        icon.setStyle("-fx-background-color: " + category.getColor() + ";");
        
        Label nameLabel = new Label(category.getDisplayName());
        nameLabel.getStyleClass().add("category-name");
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        
        Label amountLabel = new Label(String.format("₹%.2f", amount));
        amountLabel.getStyleClass().add("category-amount");
        
        item.getChildren().addAll(icon, nameLabel, amountLabel);
        return item;
    }
    
    private VBox createMonthlyBreakdown(List<Transaction> transactions, int year) {
        VBox breakdown = new VBox(10);
        breakdown.setPadding(new Insets(20));
        
        Label title = new Label("Monthly Breakdown");
        title.getStyleClass().add("subsection-title");
        
        VBox monthsBox = new VBox(8);
        
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDate start = yearMonth.atDay(1);
            LocalDate end = yearMonth.atEndOfMonth();
            
            List<Transaction> monthTransactions = transactions.stream()
                .filter(t -> !t.getDate().isBefore(start) && !t.getDate().isAfter(end))
                .collect(Collectors.toList());
            
            double monthIncome = monthTransactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
                
            double monthExpense = monthTransactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
            
            if (monthIncome > 0 || monthExpense > 0) {
                HBox monthItem = createMonthItem(yearMonth, monthIncome, monthExpense);
                monthsBox.getChildren().add(monthItem);
            }
        }
        
        if (monthsBox.getChildren().isEmpty()) {
            Label emptyLabel = new Label("No data available for this year");
            emptyLabel.getStyleClass().add("empty-message");
            monthsBox.getChildren().add(emptyLabel);
        }
        
        breakdown.getChildren().addAll(title, monthsBox);
        return breakdown;
    }
    
    private HBox createMonthItem(YearMonth month, double income, double expense) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.getStyleClass().add("month-item");
        item.setPadding(new Insets(12));
        
        Label monthLabel = new Label(month.format(DateTimeFormatter.ofPattern("MMMM")));
        monthLabel.getStyleClass().add("month-name");
        monthLabel.setPrefWidth(100);
        
        VBox incomeBox = new VBox(2);
        Label incomeTitle = new Label("Income");
        incomeTitle.getStyleClass().add("mini-label");
        Label incomeValue = new Label(String.format("₹%.2f", income));
        incomeValue.getStyleClass().addAll("mini-value", "income-text");
        incomeBox.getChildren().addAll(incomeTitle, incomeValue);
        
        VBox expenseBox = new VBox(2);
        Label expenseTitle = new Label("Expense");
        expenseTitle.getStyleClass().add("mini-label");
        Label expenseValue = new Label(String.format("₹%.2f", expense));
        expenseValue.getStyleClass().addAll("mini-value", "expense-text");
        expenseBox.getChildren().addAll(expenseTitle, expenseValue);
        
        VBox netBox = new VBox(2);
        Label netTitle = new Label("Net");
        netTitle.getStyleClass().add("mini-label");
        Label netValue = new Label(String.format("₹%.2f", income - expense));
        netValue.getStyleClass().add("mini-value");
        netBox.getChildren().addAll(netTitle, netValue);
        
        item.getChildren().addAll(monthLabel, incomeBox, expenseBox, netBox);
        return item;
    }
    
    private VBox createDetailedCategoryAnalysis(List<Transaction> transactions) {
        VBox analysis = new VBox(20);
        analysis.setPadding(new Insets(20));
        
        // Expense categories
        Label expenseTitle = new Label("Expense Categories");
        expenseTitle.getStyleClass().add("subsection-title");
        
        VBox expenseCategories = new VBox(8);
        Map<Category, Double> expenseTotals = new HashMap<>();
        
        transactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .forEach(t -> expenseTotals.merge(t.getCategory(), t.getAmount(), Double::sum));
        
        double totalExpense = expenseTotals.values().stream().mapToDouble(Double::doubleValue).sum();
        
        expenseTotals.entrySet().stream()
            .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
            .forEach(entry -> {
                double percentage = totalExpense > 0 ? (entry.getValue() / totalExpense) * 100 : 0;
                HBox categoryItem = createDetailedCategoryItem(entry.getKey(), entry.getValue(), percentage);
                expenseCategories.getChildren().add(categoryItem);
            });
        
        // Income categories
        Label incomeTitle = new Label("Income Categories");
        incomeTitle.getStyleClass().add("subsection-title");
        
        VBox incomeCategories = new VBox(8);
        Map<Category, Double> incomeTotals = new HashMap<>();
        
        transactions.stream()
            .filter(t -> t.getType() == TransactionType.INCOME)
            .forEach(t -> incomeTotals.merge(t.getCategory(), t.getAmount(), Double::sum));
        
        double totalIncome = incomeTotals.values().stream().mapToDouble(Double::doubleValue).sum();
        
        incomeTotals.entrySet().stream()
            .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
            .forEach(entry -> {
                double percentage = totalIncome > 0 ? (entry.getValue() / totalIncome) * 100 : 0;
                HBox categoryItem = createDetailedCategoryItem(entry.getKey(), entry.getValue(), percentage);
                incomeCategories.getChildren().add(categoryItem);
            });
        
        analysis.getChildren().addAll(expenseTitle, expenseCategories, incomeTitle, incomeCategories);
        return analysis;
    }
    
    private HBox createDetailedCategoryItem(Category category, double amount, double percentage) {
        HBox item = new HBox(10);
        item.setAlignment(Pos.CENTER_LEFT);
        item.getStyleClass().add("detailed-category-item");
        item.setPadding(new Insets(12));
        
        Label icon = new Label(category.getEmoji());
        icon.getStyleClass().add("category-icon-small");
        icon.setStyle("-fx-background-color: " + category.getColor() + ";");
        
        Label nameLabel = new Label(category.getDisplayName());
        nameLabel.getStyleClass().add("category-name");
        nameLabel.setPrefWidth(150);
        
        // Progress bar
        ProgressBar progressBar = new ProgressBar(percentage / 100);
        progressBar.setPrefWidth(200);
        progressBar.getStyleClass().add("category-progress");
        
        Label percentLabel = new Label(String.format("%.1f%%", percentage));
        percentLabel.getStyleClass().add("percent-label");
        percentLabel.setPrefWidth(60);
        
        Label amountLabel = new Label(String.format("₹%.2f", amount));
        amountLabel.getStyleClass().add("category-amount");
        
        HBox.setHgrow(progressBar, Priority.ALWAYS);
        
        item.getChildren().addAll(icon, nameLabel, progressBar, percentLabel, amountLabel);
        return item;
    }
    
    private VBox createTransactionsList(List<Transaction> transactions, String title) {
        VBox list = new VBox(10);
        list.setPadding(new Insets(20));
        
        Label listTitle = new Label(title);
        listTitle.getStyleClass().add("subsection-title");
        
        VBox transBox = new VBox(8);
        
        if (transactions.isEmpty()) {
            Label emptyLabel = new Label("No transactions for this period");
            emptyLabel.getStyleClass().add("empty-message");
            transBox.getChildren().add(emptyLabel);
        } else {
            // Show last 10 transactions
            int count = Math.min(10, transactions.size());
            for (int i = transactions.size() - 1; i >= Math.max(0, transactions.size() - count); i--) {
                Transaction t = transactions.get(i);
                HBox item = createSimpleTransactionItem(t);
                transBox.getChildren().add(item);
            }
            
            if (transactions.size() > 10) {
                Label moreLabel = new Label("... and " + (transactions.size() - 10) + " more transactions");
                moreLabel.getStyleClass().add("more-label");
                transBox.getChildren().add(moreLabel);
            }
        }
        
        list.getChildren().addAll(listTitle, transBox);
        return list;
    }
    
    private HBox createSimpleTransactionItem(Transaction transaction) {
        HBox item = new HBox(10);
        item.setAlignment(Pos.CENTER_LEFT);
        item.getStyleClass().add("simple-transaction-item");
        item.setPadding(new Insets(8));
        
        Label icon = new Label(transaction.getCategory().getEmoji());
        icon.getStyleClass().add("small-icon");
        
        Label categoryLabel = new Label(transaction.getCategory().getDisplayName());
        categoryLabel.getStyleClass().add("small-category");
        categoryLabel.setPrefWidth(120);
        
        Label descLabel = new Label(transaction.getDescription());
        descLabel.getStyleClass().add("small-desc");
        HBox.setHgrow(descLabel, Priority.ALWAYS);
        
        Label dateLabel = new Label(transaction.getFormattedDate());
        dateLabel.getStyleClass().add("small-date");
        
        Label amountLabel = new Label(transaction.getFormattedAmount());
        amountLabel.getStyleClass().add("small-amount");
        if (transaction.getType() == TransactionType.INCOME) {
            amountLabel.getStyleClass().add("income-amount");
        } else {
            amountLabel.getStyleClass().add("expense-amount");
        }
        
        item.getChildren().addAll(icon, categoryLabel, descLabel, dateLabel, amountLabel);
        return item;
    }
    
    private void downloadCurrentReport() {
        String fileName = "Report_" + currentReportType + "_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter("reports/" + fileName))) {
            // Create reports directory if it doesn't exist
            new java.io.File("reports").mkdirs();
            
            writer.println("=".repeat(80));
            writer.println("               EXPENSE TRACKER - " + currentReportType.toUpperCase() + " REPORT");
            writer.println("=".repeat(80));
            writer.println("Generated on: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
            writer.println("=".repeat(80));
            writer.println();
            
            // Generate report based on type
            switch (currentReportType) {
                case "Daily":
                    generateDailyReportText(writer);
                    break;
                case "Monthly":
                    generateMonthlyReportText(writer);
                    break;
                case "Yearly":
                    generateYearlyReportText(writer);
                    break;
                case "Category":
                    generateCategoryReportText(writer);
                    break;
            }
            
            writer.println();
            writer.println("=".repeat(80));
            writer.println("                    End of Report");
            writer.println("=".repeat(80));
            
            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Report Downloaded");
            alert.setHeaderText("Success!");
            alert.setContentText("Report saved to: reports/" + fileName);
            alert.showAndWait();
            
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to download report");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    private void generateDailyReportText(PrintWriter writer) {
        LocalDate today = LocalDate.now();
        List<Transaction> transactions = dataManager.getTransactionsByDateRange(today, today);
        
        writer.println("DAILY REPORT - " + today.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        writer.println("-".repeat(80));
        writer.println();
        
        double income = transactions.stream().filter(t -> t.getType() == TransactionType.INCOME).mapToDouble(Transaction::getAmount).sum();
        double expense = transactions.stream().filter(t -> t.getType() == TransactionType.EXPENSE).mapToDouble(Transaction::getAmount).sum();
        
        writer.printf("Total Income:    ₹%.2f%n", income);
        writer.printf("Total Expenses:  ₹%.2f%n", expense);
        writer.printf("Net Balance:     ₹%.2f%n", income - expense);
        writer.println();
        
        if (!transactions.isEmpty()) {
            writer.println("TRANSACTIONS:");
            writer.println("-".repeat(80));
            for (Transaction t : transactions) {
                writer.printf("%-15s %-30s %10s  ₹%.2f%n", 
                    t.getCategory().getDisplayName(), 
                    t.getDescription(), 
                    t.getType().getDisplayName(),
                    t.getAmount());
            }
        } else {
            writer.println("No transactions for today.");
        }
    }
    
    private void generateMonthlyReportText(PrintWriter writer) {
        YearMonth currentMonth = YearMonth.now();
        LocalDate start = currentMonth.atDay(1);
        LocalDate end = currentMonth.atEndOfMonth();
        List<Transaction> transactions = dataManager.getTransactionsByDateRange(start, end);
        
        writer.println("MONTHLY REPORT - " + currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        writer.println("-".repeat(80));
        writer.println();
        
        double income = transactions.stream().filter(t -> t.getType() == TransactionType.INCOME).mapToDouble(Transaction::getAmount).sum();
        double expense = transactions.stream().filter(t -> t.getType() == TransactionType.EXPENSE).mapToDouble(Transaction::getAmount).sum();
        
        writer.printf("Total Income:    ₹%.2f%n", income);
        writer.printf("Total Expenses:  ₹%.2f%n", expense);
        writer.printf("Net Balance:     ₹%.2f%n", income - expense);
        writer.println();
        
        // Category breakdown
        Map<Category, Double> categoryTotals = new HashMap<>();
        transactions.stream().filter(t -> t.getType() == TransactionType.EXPENSE)
            .forEach(t -> categoryTotals.merge(t.getCategory(), t.getAmount(), Double::sum));
        
        if (!categoryTotals.isEmpty()) {
            writer.println("CATEGORY BREAKDOWN:");
            writer.println("-".repeat(80));
            categoryTotals.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .forEach(entry -> writer.printf("%-25s ₹%.2f%n", entry.getKey().getDisplayName(), entry.getValue()));
        }
    }
    
    private void generateYearlyReportText(PrintWriter writer) {
        int year = LocalDate.now().getYear();
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        List<Transaction> transactions = dataManager.getTransactionsByDateRange(start, end);
        
        writer.println("YEARLY REPORT - " + year);
        writer.println("-".repeat(80));
        writer.println();
        
        double income = transactions.stream().filter(t -> t.getType() == TransactionType.INCOME).mapToDouble(Transaction::getAmount).sum();
        double expense = transactions.stream().filter(t -> t.getType() == TransactionType.EXPENSE).mapToDouble(Transaction::getAmount).sum();
        
        writer.printf("Total Income:        ₹%.2f%n", income);
        writer.printf("Total Expenses:      ₹%.2f%n", expense);
        writer.printf("Net Balance:         ₹%.2f%n", income - expense);
        writer.printf("Avg Monthly Expense: ₹%.2f%n", expense / 12);
        writer.printf("Total Transactions:  %d%n", transactions.size());
        writer.println();
        
        writer.println("MONTHLY BREAKDOWN:");
        writer.println("-".repeat(80));
        writer.printf("%-12s %15s %15s %15s%n", "Month", "Income", "Expense", "Net");
        writer.println("-".repeat(80));
        
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDate monthStart = yearMonth.atDay(1);
            LocalDate monthEnd = yearMonth.atEndOfMonth();
            
            List<Transaction> monthTrans = transactions.stream()
                .filter(t -> !t.getDate().isBefore(monthStart) && !t.getDate().isAfter(monthEnd))
                .collect(Collectors.toList());
            
            double monthIncome = monthTrans.stream().filter(t -> t.getType() == TransactionType.INCOME).mapToDouble(Transaction::getAmount).sum();
            double monthExpense = monthTrans.stream().filter(t -> t.getType() == TransactionType.EXPENSE).mapToDouble(Transaction::getAmount).sum();
            
            if (monthIncome > 0 || monthExpense > 0) {
                writer.printf("%-12s %15.2f %15.2f %15.2f%n", 
                    yearMonth.format(DateTimeFormatter.ofPattern("MMMM")),
                    monthIncome, monthExpense, monthIncome - monthExpense);
            }
        }
    }
    
    private void generateCategoryReportText(PrintWriter writer) {
        List<Transaction> transactions = dataManager.getAllTransactions();
        
        writer.println("CATEGORY-WISE ANALYSIS");
        writer.println("-".repeat(80));
        writer.println();
        
        // Expense Categories
        writer.println("EXPENSE CATEGORIES:");
        writer.println("-".repeat(80));
        Map<Category, Double> expenseTotals = new HashMap<>();
        transactions.stream().filter(t -> t.getType() == TransactionType.EXPENSE)
            .forEach(t -> expenseTotals.merge(t.getCategory(), t.getAmount(), Double::sum));
        
        double totalExpense = expenseTotals.values().stream().mapToDouble(Double::doubleValue).sum();
        
        if (!expenseTotals.isEmpty()) {
            writer.printf("%-25s %15s %15s%n", "Category", "Amount", "Percentage");
            writer.println("-".repeat(80));
            expenseTotals.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .forEach(entry -> {
                    double percentage = totalExpense > 0 ? (entry.getValue() / totalExpense) * 100 : 0;
                    writer.printf("%-25s ₹%14.2f %14.1f%%%n", 
                        entry.getKey().getDisplayName(), entry.getValue(), percentage);
                });
        }
        
        writer.println();
        
        // Income Categories
        writer.println("INCOME CATEGORIES:");
        writer.println("-".repeat(80));
        Map<Category, Double> incomeTotals = new HashMap<>();
        transactions.stream().filter(t -> t.getType() == TransactionType.INCOME)
            .forEach(t -> incomeTotals.merge(t.getCategory(), t.getAmount(), Double::sum));
        
        double totalIncome = incomeTotals.values().stream().mapToDouble(Double::doubleValue).sum();
        
        if (!incomeTotals.isEmpty()) {
            writer.printf("%-25s %15s %15s%n", "Category", "Amount", "Percentage");
            writer.println("-".repeat(80));
            incomeTotals.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .forEach(entry -> {
                    double percentage = totalIncome > 0 ? (entry.getValue() / totalIncome) * 100 : 0;
                    writer.printf("%-25s ₹%14.2f %14.1f%%%n", 
                        entry.getKey().getDisplayName(), entry.getValue(), percentage);
                });
        }
    }
    
    private void showChartsView() {
        reportContent.getChildren().clear();
        
        Label reportTitle = new Label("📊 Visual Analytics - Charts & Graphs");
        reportTitle.getStyleClass().add("report-title");
        reportTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2D3748;");
        
        // Charts container
        VBox chartsContainer = new VBox(30);
        chartsContainer.setPadding(new Insets(20));
        
        // Spending Trend Line Chart
        VBox lineChartBox = createSpendingTrendChart();
        
        // Category Breakdown Pie Chart
        VBox pieChartBox = createCategoryPieChart();
        
        // Monthly Comparison Bar Chart
        VBox barChartBox = createMonthlyBarChart();
        
        chartsContainer.getChildren().addAll(lineChartBox, pieChartBox, barChartBox);
        reportContent.getChildren().addAll(reportTitle, chartsContainer);
    }
    
    private VBox createSpendingTrendChart() {
        VBox container = new VBox(15);
        container.getStyleClass().add("chart-container");
        container.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 15; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        
        Label chartTitle = new Label("📈 Spending Trend - Last 30 Days");
        chartTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2D3748;");
        
        // Create line chart
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount (₹)");
        
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("");
        lineChart.setLegendVisible(true);
        lineChart.setLegendSide(Side.TOP);
        lineChart.setPrefHeight(400);
        lineChart.setAnimated(true);
        
        // Expense series
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expenses");
        
        // Income series
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");
        
        // Get last 30 days data
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29);
        
        Map<LocalDate, Double> dailyExpenses = new HashMap<>();
        Map<LocalDate, Double> dailyIncome = new HashMap<>();
        
        // Initialize all dates with 0
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dailyExpenses.put(date, 0.0);
            dailyIncome.put(date, 0.0);
        }
        
        // Get transactions and aggregate by date
        List<Transaction> transactions = dataManager.getTransactionsByDateRange(startDate, endDate);
        for (Transaction t : transactions) {
            if (t.getType() == TransactionType.EXPENSE) {
                dailyExpenses.merge(t.getDate(), t.getAmount(), Double::sum);
            } else {
                dailyIncome.merge(t.getDate(), t.getAmount(), Double::sum);
            }
        }
        
        // Add data points (show every 3rd day to avoid clutter)
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
        container.getStyleClass().add("chart-container");
        container.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 15; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        
        Label chartTitle = new Label("🍰 Expense Breakdown by Category");
        chartTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2D3748;");
        
        // Create pie chart
        PieChart pieChart = new PieChart();
        pieChart.setTitle("");
        pieChart.setLegendSide(Side.RIGHT);
        pieChart.setPrefHeight(400);
        pieChart.setLabelsVisible(true);
        pieChart.setAnimated(true);
        
        // Get current month data
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        List<Transaction> monthTransactions = dataManager.getTransactionsByDateRange(startOfMonth, now);
        
        // Calculate category totals
        Map<Category, Double> categoryTotals = new HashMap<>();
        monthTransactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .forEach(t -> categoryTotals.merge(t.getCategory(), t.getAmount(), Double::sum));
        
        // Add data to pie chart
        if (!categoryTotals.isEmpty()) {
            double total = categoryTotals.values().stream().mapToDouble(Double::doubleValue).sum();
            
            categoryTotals.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .forEach(entry -> {
                    double percentage = (entry.getValue() / total) * 100;
                    String label = String.format("%s (%.1f%%)", 
                        entry.getKey().getEmoji() + " " + entry.getKey().getDisplayName(), 
                        percentage);
                    PieChart.Data slice = new PieChart.Data(label, entry.getValue());
                    pieChart.getData().add(slice);
                });
        } else {
            pieChart.getData().add(new PieChart.Data("No expenses yet", 1));
        }
        
        container.getChildren().addAll(chartTitle, pieChart);
        return container;
    }
    
    private VBox createMonthlyBarChart() {
        VBox container = new VBox(15);
        container.getStyleClass().add("chart-container");
        container.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 15; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        
        Label chartTitle = new Label("📊 Monthly Comparison - Last 6 Months");
        chartTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2D3748;");
        
        // Create bar chart
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount (₹)");
        
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("");
        barChart.setLegendVisible(true);
        barChart.setLegendSide(Side.TOP);
        barChart.setPrefHeight(400);
        barChart.setAnimated(true);
        barChart.setCategoryGap(20);
        barChart.setBarGap(5);
        
        // Expense series
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expenses");
        
        // Income series
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");
        
        // Get last 6 months data
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
    
    private void downloadCurrentReportAsPDF() {
        String fileName = "Report_" + currentReportType + "_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
        
        try {
            // Create reports directory if it doesn't exist
            new java.io.File("reports").mkdirs();
            
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream("reports/" + fileName));
            document.open();
            
            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
            Paragraph title = new Paragraph("EXPENSE TRACKER - " + currentReportType.toUpperCase() + " REPORT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);
            document.add(title);
            
            // Add generation date
            Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            Paragraph date = new Paragraph("Generated on: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm a")), dateFont);
            date.setAlignment(Element.ALIGN_CENTER);
            date.setSpacingAfter(20);
            document.add(date);
            
            // Add horizontal line
            document.add(new Paragraph("_".repeat(80)));
            document.add(new Paragraph(" "));
            
            // Generate report based on type
            switch (currentReportType) {
                case "Daily":
                    generateDailyReportPDF(document);
                    break;
                case "Monthly":
                    generateMonthlyReportPDF(document);
                    break;
                case "Yearly":
                    generateYearlyReportPDF(document);
                    break;
                case "Category":
                    generateCategoryReportPDF(document);
                    break;
                case "Charts":
                    generateChartsSummaryPDF(document);
                    break;
            }
            
            // Add footer
            document.add(new Paragraph(" "));
            document.add(new Paragraph("_".repeat(80)));
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, BaseColor.GRAY);
            Paragraph footer = new Paragraph("End of Report - Generated by Expense Tracker App", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);
            
            document.close();
            
            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PDF Downloaded");
            alert.setHeaderText("Success!");
            alert.setContentText("PDF report saved to: reports/" + fileName);
            alert.showAndWait();
            
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Download Error");
            alert.setHeaderText("Failed to generate PDF");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    private void generateDailyReportPDF(Document document) throws DocumentException {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.DARK_GRAY);
        document.add(new Paragraph("Daily Report - " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")), headerFont));
        document.add(new Paragraph(" "));
        
        LocalDate today = LocalDate.now();
        List<Transaction> todayTransactions = dataManager.getTransactionsByDateRange(today, today);
        
        if (todayTransactions.isEmpty()) {
            document.add(new Paragraph("No transactions for today."));
            return;
        }
        
        // Create table
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        
        // Add headers
        Font headerCellFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE);
        PdfPCell cell;
        
        cell = new PdfPCell(new Phrase("Category", headerCellFont));
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Description", headerCellFont));
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Type", headerCellFont));
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Amount", headerCellFont));
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        table.addCell(cell);
        
        // Add data
        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        double totalIncome = 0;
        double totalExpense = 0;
        
        for (Transaction t : todayTransactions) {
            table.addCell(new Phrase(t.getCategory().getEmoji() + " " + t.getCategory().getDisplayName(), dataFont));
            table.addCell(new Phrase(t.getDescription(), dataFont));
            table.addCell(new Phrase(t.getType().toString(), dataFont));
            table.addCell(new Phrase(String.format("₹%.2f", t.getAmount()), dataFont));
            
            if (t.getType() == TransactionType.INCOME) {
                totalIncome += t.getAmount();
            } else {
                totalExpense += t.getAmount();
            }
        }
        
        document.add(table);
        
        // Add summary
        document.add(new Paragraph(" "));
        Font summaryFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        document.add(new Paragraph("Summary:", summaryFont));
        document.add(new Paragraph("Total Income: ₹" + String.format("%.2f", totalIncome)));
        document.add(new Paragraph("Total Expense: ₹" + String.format("%.2f", totalExpense)));
        document.add(new Paragraph("Net Balance: ₹" + String.format("%.2f", (totalIncome - totalExpense))));
    }
    
    private void generateMonthlyReportPDF(Document document) throws DocumentException {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.DARK_GRAY);
        YearMonth currentMonth = YearMonth.now();
        document.add(new Paragraph("Monthly Report - " + currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")), headerFont));
        document.add(new Paragraph(" "));
        
        LocalDate startDate = currentMonth.atDay(1);
        LocalDate endDate = currentMonth.atEndOfMonth();
        List<Transaction> transactions = dataManager.getTransactionsByDateRange(startDate, endDate);
        
        if (transactions.isEmpty()) {
            document.add(new Paragraph("No transactions for this month."));
            return;
        }
        
        double totalIncome = transactions.stream()
            .filter(t -> t.getType() == TransactionType.INCOME)
            .mapToDouble(Transaction::getAmount).sum();
        
        double totalExpense = transactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .mapToDouble(Transaction::getAmount).sum();
        
        // Summary
        Font summaryFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        document.add(new Paragraph("Financial Summary:", summaryFont));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Total Income: ₹" + String.format("%.2f", totalIncome)));
        document.add(new Paragraph("Total Expense: ₹" + String.format("%.2f", totalExpense)));
        document.add(new Paragraph("Net Savings: ₹" + String.format("%.2f", (totalIncome - totalExpense))));
        document.add(new Paragraph("Total Transactions: " + transactions.size()));
    }
    
    private void generateYearlyReportPDF(Document document) throws DocumentException {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.DARK_GRAY);
        int currentYear = LocalDate.now().getYear();
        document.add(new Paragraph("Yearly Report - " + currentYear, headerFont));
        document.add(new Paragraph(" "));
        
        LocalDate startDate = LocalDate.of(currentYear, 1, 1);
        LocalDate endDate = LocalDate.of(currentYear, 12, 31);
        List<Transaction> transactions = dataManager.getTransactionsByDateRange(startDate, endDate);
        
        if (transactions.isEmpty()) {
            document.add(new Paragraph("No transactions for this year."));
            return;
        }
        
        double totalIncome = transactions.stream()
            .filter(t -> t.getType() == TransactionType.INCOME)
            .mapToDouble(Transaction::getAmount).sum();
        
        double totalExpense = transactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .mapToDouble(Transaction::getAmount).sum();
        
        Font summaryFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        document.add(new Paragraph("Annual Financial Summary:", summaryFont));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Total Income: ₹" + String.format("%.2f", totalIncome)));
        document.add(new Paragraph("Total Expense: ₹" + String.format("%.2f", totalExpense)));
        document.add(new Paragraph("Net Savings: ₹" + String.format("%.2f", (totalIncome - totalExpense))));
        document.add(new Paragraph("Average Monthly Income: ₹" + String.format("%.2f", totalIncome / 12)));
        document.add(new Paragraph("Average Monthly Expense: ₹" + String.format("%.2f", totalExpense / 12)));
    }
    
    private void generateCategoryReportPDF(Document document) throws DocumentException {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.DARK_GRAY);
        document.add(new Paragraph("Category-wise Report", headerFont));
        document.add(new Paragraph(" "));
        
        List<Transaction> allTransactions = dataManager.getAllTransactions();
        if (allTransactions.isEmpty()) {
            document.add(new Paragraph("No transactions available."));
            return;
        }
        
        // Expense by category
        Map<Category, Double> expenseTotals = new HashMap<>();
        allTransactions.stream().filter(t -> t.getType() == TransactionType.EXPENSE)
            .forEach(t -> expenseTotals.merge(t.getCategory(), t.getAmount(), Double::sum));
        
        if (!expenseTotals.isEmpty()) {
            Font subHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            document.add(new Paragraph("Expenses by Category:", subHeaderFont));
            document.add(new Paragraph(" "));
            
            double totalExpense = expenseTotals.values().stream().mapToDouble(Double::doubleValue).sum();
            
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(80);
            table.setSpacingBefore(10f);
            
            // Headers
            Font headerCellFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE);
            PdfPCell cell = new PdfPCell(new Phrase("Category", headerCellFont));
            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase("Amount", headerCellFont));
            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase("Percentage", headerCellFont));
            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            table.addCell(cell);
            
            // Data
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            expenseTotals.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .forEach(entry -> {
                    double percentage = (entry.getValue() / totalExpense) * 100;
                    table.addCell(new Phrase(entry.getKey().getEmoji() + " " + entry.getKey().getDisplayName(), dataFont));
                    table.addCell(new Phrase(String.format("₹%.2f", entry.getValue()), dataFont));
                    table.addCell(new Phrase(String.format("%.1f%%", percentage), dataFont));
                });
            
            document.add(table);
        }
    }
    
    private void generateChartsSummaryPDF(Document document) throws DocumentException {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.DARK_GRAY);
        document.add(new Paragraph("Visual Analytics Summary", headerFont));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("This report includes data from Line Chart, Pie Chart, and Bar Chart visualizations."));
        document.add(new Paragraph(" "));
        
        // Last 30 days summary
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29);
        List<Transaction> transactions = dataManager.getTransactionsByDateRange(startDate, endDate);
        
        double totalIncome = transactions.stream()
            .filter(t -> t.getType() == TransactionType.INCOME)
            .mapToDouble(Transaction::getAmount).sum();
        
        double totalExpense = transactions.stream()
            .filter(t -> t.getType() == TransactionType.EXPENSE)
            .mapToDouble(Transaction::getAmount).sum();
        
        Font summaryFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        document.add(new Paragraph("Last 30 Days Summary:", summaryFont));
        document.add(new Paragraph("Total Income: ₹" + String.format("%.2f", totalIncome)));
        document.add(new Paragraph("Total Expense: ₹" + String.format("%.2f", totalExpense)));
        document.add(new Paragraph("Average Daily Spending: ₹" + String.format("%.2f", totalExpense / 30)));
    }
    
    public VBox getView() {
        return view;
    }
}
