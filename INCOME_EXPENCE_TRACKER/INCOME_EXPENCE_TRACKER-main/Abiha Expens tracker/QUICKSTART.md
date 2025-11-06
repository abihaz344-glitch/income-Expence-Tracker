# 🚀 Quick Start Guide - Expense Tracker

## Prerequisites

Before running the application, make sure you have:

1. **Java Development Kit (JDK) 11 or higher**
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Or use OpenJDK: https://adoptium.net/

2. **Apache Maven 3.6 or higher**
   - Download from: https://maven.apache.org/download.cgi
   - Add Maven to your system PATH

## Verify Installation

Open Command Prompt or PowerShell and run:

```bash
java -version
```
Should show Java 11 or higher.

```bash
mvn -version
```
Should show Maven 3.6 or higher.

## Running the Application

### Method 1: Using the Run Script (Easiest)

1. Navigate to the project folder:
   ```bash
   cd "C:\Users\dell\OneDrive\Desktop\Abiha Expens tracker"
   ```

2. Double-click `run.bat` or run in terminal:
   ```bash
   run.bat
   ```

### Method 2: Using Maven Command

1. Open Command Prompt or PowerShell

2. Navigate to project directory:
   ```bash
   cd "C:\Users\dell\OneDrive\Desktop\Abiha Expens tracker"
   ```

3. Run the application:
   ```bash
   mvn clean javafx:run
   ```

### Method 3: Build JAR and Run

1. Build the project:
   ```bash
   mvn clean package
   ```

2. Run the JAR:
   ```bash
   java -jar target/expense-tracker-1.0.0.jar
   ```

## First Time Setup

When you first run the application:

1. **Welcome Screen**: The dashboard will be empty
2. **Add Your First Transaction**:
   - Click "➕ Add Transaction" in the sidebar
   - Select Income or Expense
   - Choose a category
   - Enter amount (e.g., 1000)
   - Add description (e.g., "Monthly Salary")
   - Click "Add Transaction"

3. **Explore Features**:
   - Return to Dashboard to see your balance
   - Click "📝 History" to view all transactions
   - Click "📈 Reports" to see financial analysis

## Troubleshooting

### Application won't start

**Problem**: `JAVA_HOME not set` or `Maven not found`
- **Solution**: Add Java and Maven to your system PATH
- Restart your terminal/command prompt after installation

**Problem**: `JavaFX components not found`
- **Solution**: The Maven build will automatically download JavaFX dependencies
- Ensure you have internet connection during first build

**Problem**: `Module error` or compatibility issues
- **Solution**: Ensure you're using Java 11 or higher
- Try: `mvn clean install` then `mvn javafx:run`

### Window is too small or large

- Resize the window by dragging corners
- Minimum size: 900x600 pixels
- Recommended: 1200x800 pixels

### Data not saving

- Check if `data` folder exists in project directory
- Ensure write permissions for the project folder
- Data is saved automatically in `data/transactions.json`

## Project Structure

```
Abiha Expens tracker/
├── src/
│   ├── main/
│   │   ├── java/com/expensetracker/
│   │   │   ├── Main.java              # Application entry point
│   │   │   ├── model/                 # Data models
│   │   │   │   ├── Transaction.java
│   │   │   │   ├── Category.java
│   │   │   │   └── TransactionType.java
│   │   │   ├── data/                  # Data management
│   │   │   │   ├── DataManager.java
│   │   │   │   └── LocalDateAdapter.java
│   │   │   └── ui/                    # User interface
│   │   │       ├── DashboardController.java
│   │   │       ├── AddTransactionView.java
│   │   │       ├── HistoryView.java
│   │   │       └── ReportsView.java
│   │   └── resources/
│   │       └── styles/
│   │           └── main.css           # Stylesheet
├── data/                              # Auto-created data folder
│   └── transactions.json              # Your transaction data
├── pom.xml                            # Maven configuration
├── run.bat                            # Quick run script
├── README.md                          # Project overview
├── FEATURES.md                        # Detailed features guide
└── QUICKSTART.md                      # This file
```

## Usage Examples

### Example 1: Adding Income

1. Click "➕ Add Transaction"
2. Select "💰 Income" radio button
3. Choose category: "💰 Salary"
4. Enter amount: 5000
5. Description: "Monthly Salary - January"
6. Select date (defaults to today)
7. Click "Add Transaction"

### Example 2: Adding Expense

1. Click "➕ Add Transaction"
2. Select "💸 Expense" radio button
3. Choose category: "🍕 Food"
4. Enter amount: 45.50
5. Description: "Dinner at restaurant"
6. Click "Add Transaction"

### Example 3: Viewing Reports

1. Click "📈 Reports"
2. Select report type:
   - Daily: See today's transactions
   - Monthly: Current month overview
   - Yearly: Annual summary
   - By Category: Spending breakdown

### Example 4: Searching History

1. Click "📝 History"
2. Type in search box: "food"
3. Or use filter dropdown: "Expenses"
4. Results update automatically

## Data Backup

Your transaction data is stored in:
```
data/transactions.json
```

To backup:
1. Copy the entire `data` folder
2. Save to external drive or cloud storage
3. To restore: Replace the `data` folder

## Customization

### Change Colors

Edit `src/main/resources/styles/main.css`:
- Sidebar gradient: Line 14-15
- Income color: Line 144
- Expense color: Line 148
- Category colors: In `Category.java` enum

### Add New Categories

Edit `src/main/java/com/expensetracker/model/Category.java`:
```java
NEW_CATEGORY("Display Name", "🎯", "#HexColor"),
```

## Support & Help

For issues or questions:
1. Check the FEATURES.md file
2. Review error messages in console
3. Ensure all prerequisites are installed
4. Try `mvn clean install` to rebuild

## Next Steps

1. ✅ Run the application
2. ✅ Add some sample transactions
3. ✅ Explore all four views (Dashboard, Add, History, Reports)
4. ✅ Try searching and filtering
5. ✅ Generate different types of reports
6. ✅ Delete a transaction to see confirmation dialog

**Enjoy managing your finances! 💰✨**
