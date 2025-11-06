package com.expensetracker.data;

import com.expensetracker.model.Transaction;
import com.expensetracker.model.TransactionType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataManager {
    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = "transactions.json";
    private static DataManager instance;
    
    private List<Transaction> transactions;
    private Gson gson;
    
    private DataManager() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        this.transactions = new ArrayList<>();
        initializeDataDirectory();
        loadTransactions();
    }
    
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }
    
    private void initializeDataDirectory() {
        Path dataPath = Paths.get(DATA_DIR);
        if (!Files.exists(dataPath)) {
            try {
                Files.createDirectories(dataPath);
            } catch (IOException e) {
                System.err.println("Error creating data directory: " + e.getMessage());
            }
        }
    }
    
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        saveTransactions();
    }
    
    public void updateTransaction(Transaction transaction) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId().equals(transaction.getId())) {
                transactions.set(i, transaction);
                saveTransactions();
                break;
            }
        }
    }
    
    public void deleteTransaction(String id) {
        transactions.removeIf(t -> t.getId().equals(id));
        saveTransactions();
    }
    
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }
    
    public List<Transaction> getTransactionsByType(TransactionType type) {
        return transactions.stream()
                .filter(t -> t.getType() == type)
                .collect(Collectors.toList());
    }
    
    public List<Transaction> getTransactionsByDateRange(LocalDate start, LocalDate end) {
        return transactions.stream()
                .filter(t -> !t.getDate().isBefore(start) && !t.getDate().isAfter(end))
                .collect(Collectors.toList());
    }
    
    public double getTotalIncome() {
        return transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
    
    public double getTotalExpense() {
        return transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
    
    public double getBalance() {
        return getTotalIncome() - getTotalExpense();
    }
    
    private void saveTransactions() {
        try (Writer writer = new FileWriter(Paths.get(DATA_DIR, DATA_FILE).toFile())) {
            gson.toJson(transactions, writer);
        } catch (IOException e) {
            System.err.println("Error saving transactions: " + e.getMessage());
        }
    }
    
    private void loadTransactions() {
        File dataFile = Paths.get(DATA_DIR, DATA_FILE).toFile();
        if (dataFile.exists()) {
            try (Reader reader = new FileReader(dataFile)) {
                Type listType = new TypeToken<ArrayList<Transaction>>(){}.getType();
                List<Transaction> loaded = gson.fromJson(reader, listType);
                if (loaded != null) {
                    transactions = loaded;
                }
            } catch (IOException e) {
                System.err.println("Error loading transactions: " + e.getMessage());
            }
        }
    }
}
