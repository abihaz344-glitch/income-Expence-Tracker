# 💰 Expense Tracker - Complete User Guide

## 🎨 Application Interface

The application features a **modern, beautiful design** inspired by the BudgetPlanner UI you provided, with:

### Main Interface Components

```
┌─────────────────────────────────────────────────────────────────┐
│  💰 Budget Planner                    │  Main Content Area      │
│  ═══════════════════════                                        │
│                                        ┌──────────────────────┐ │
│  📊 Dashboard  ◄──(active)             │  Dashboard Overview  │ │
│  ➕ Add Transaction                    │                      │ │
│  📝 History                            │  Balance: $5,245.50  │ │
│  📈 Reports                            │  Income:  $8,500.00  │ │
│                                        │  Expense: $3,254.50  │ │
│                                        └──────────────────────┘ │
│  Purple Gradient Sidebar               Recent Transactions →   │
└─────────────────────────────────────────────────────────────────┘
```

## 🚀 Quick Feature Access

### Dashboard View (📊)
```
┌─────────────────────────────────────────────────────────┐
│  Dashboard Overview                                     │
│  ═══════════════════                                    │
│                                                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐ │
│  │ Total Balance│  │ Total Income │  │Total Expenses│ │
│  │   $5,245.50  │  │   $8,500.00  │  │   $3,254.50  │ │
│  └──────────────┘  └──────────────┘  └──────────────┘ │
│                                                          │
│  Recent Transactions                                    │
│  ┌─────────────────────────────────────────────────┐   │
│  │ 🍕 Food         Lunch at cafe      $25.50  ⚡   │   │
│  │ 🚗 Transport    Gas               $45.00  ⚡    │   │
│  │ 💰 Salary       Monthly pay     $5,000.00  💚   │   │
│  └─────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

### Add Transaction View (➕)
```
┌─────────────────────────────────────────────────────────┐
│  Add New Transaction                                    │
│  ════════════════════                                   │
│                                                          │
│  ┌──────────────────────────────────────────────────┐  │
│  │  Transaction Type                                │  │
│  │  ○ 💰 Income    ● 💸 Expense                    │  │
│  │                                                  │  │
│  │  Category                                        │  │
│  │  ▼ 🍕 Food                                      │  │
│  │                                                  │  │
│  │  Amount ($)                                      │  │
│  │  [________45.50________]                         │  │
│  │                                                  │  │
│  │  Description                                     │  │
│  │  [____Dinner at restaurant____]                  │  │
│  │                                                  │  │
│  │  Date                                            │  │
│  │  📅 [__Jan 24, 2025__]                          │  │
│  │                                                  │  │
│  │     [Add Transaction]  [Clear]                   │  │
│  └──────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
```

### History View (📝)
```
┌─────────────────────────────────────────────────────────┐
│  Transaction History                                    │
│  ════════════════════                                   │
│                                                          │
│  🔍 [Search transactions...]  ▼ All  🔄 Refresh        │
│                                                          │
│  ┌─────────────────────────────────────────────────┐   │
│  │ 🍕 Food         Dinner         Expense  $45.50 🗑│  │
│  │ 🚗 Transport    Gas fill       Expense  $60.00 🗑│  │
│  │ 💰 Salary       Monthly pay    Income $5000.00 🗑│  │
│  │ 🎬 Entertainment Movie night   Expense  $35.00 🗑│  │
│  │ 🏠 Rent         Monthly rent   Expense $1200.00🗑│  │
│  └─────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

### Reports View (📈)
```
┌─────────────────────────────────────────────────────────┐
│  Financial Reports                                      │
│  ══════════════════                                     │
│                                                          │
│  ○ 📅 Daily  ● 📊 Monthly  ○ 📈 Yearly  ○ 🏷️ Category  │
│                                                          │
│  Monthly Report - January 2025                          │
│  ┌──────────────────────────────────────────────────┐  │
│  │  Income: $8,500  Expenses: $3,254  Net: $5,246  │  │
│  └──────────────────────────────────────────────────┘  │
│                                                          │
│  Category Breakdown                                     │
│  ┌─────────────────────────────────────────────────┐   │
│  │ 🏠 Rent          ████████████░░░░  $1,200       │   │
│  │ 🍕 Food          ████████░░░░░░░░  $850         │   │
│  │ 🚗 Transport     ████░░░░░░░░░░░░  $450         │   │
│  │ 🎬 Entertainment ███░░░░░░░░░░░░░  $354         │   │
│  └─────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

## 🎯 How to Use Each Feature

### 1. Adding Your First Transaction

**Step-by-Step:**
1. Click **"➕ Add Transaction"** in the sidebar
2. Choose type: **Income** or **Expense**
3. Select appropriate **Category** from dropdown
4. Enter the **Amount** (e.g., 45.50)
5. Add a **Description** (optional but recommended)
6. Pick the **Date** (defaults to today)
7. Click **"Add Transaction"** button
8. See success message ✅

**Example Scenarios:**
- **Salary**: Income → 💰 Salary → $5000 → "Monthly salary"
- **Grocery**: Expense → 🍕 Food → $85.50 → "Weekly groceries"
- **Gas**: Expense → 🚗 Transport → $45 → "Gas fill-up"

### 2. Viewing Your Financial Status

**Dashboard Features:**
- **Balance Card** (Blue border): Shows net worth
- **Income Card** (Green border): Total money earned
- **Expense Card** (Red border): Total money spent
- **Recent Activity**: Last 5 transactions at a glance

**Color Coding:**
- 💚 Green amounts = Income (money coming in)
- ❤️ Red amounts = Expenses (money going out)

### 3. Managing Transaction History

**Search & Filter:**
- **Search Box**: Type keywords to find transactions
  - Search by: description, category name, or amount
  - Results update in real-time
  
- **Filter Dropdown**: Quick category filtering
  - "All" - Show everything
  - "Income" - Only money coming in
  - "Expenses" - Only money going out

**Delete Transactions:**
1. Click the 🗑️ button on any transaction
2. Confirm deletion in the popup
3. Transaction removed immediately
4. Dashboard updates automatically

### 4. Generating Reports

**Daily Report:**
- Shows today's financial activity
- Perfect for daily review
- See income, expenses, and net for the day

**Monthly Report:**
- Current month overview
- Category-wise breakdown
- Identifies spending patterns
- Shows top expense categories

**Yearly Report:**
- Annual financial summary
- Month-by-month comparison
- Average monthly expenses
- Total transaction count
- Best for long-term planning

**Category Analysis:**
- Deep dive into each category
- Percentage breakdown
- Visual progress bars
- Both income and expense categories

## 💡 Pro Tips

### Best Practices
1. **Daily Entry**: Add transactions daily for accuracy
2. **Use Descriptions**: Make future searching easier
3. **Proper Categories**: Choose the right category
4. **Regular Reviews**: Check monthly reports
5. **Backup Data**: Copy the `data` folder regularly

### Keyboard Shortcuts
- **Tab**: Move between form fields
- **Enter**: Submit forms
- **Esc**: Close dialogs

### Data Management
- **Auto-Save**: Everything saves automatically
- **Data Location**: `data/transactions.json`
- **Portable**: Easy to backup or transfer
- **Privacy**: All data stored locally

## 🎨 Visual Design Elements

### Color Meanings
| Color | Usage | Meaning |
|-------|-------|---------|
| 💜 Purple | Navigation, Balance | Primary brand color |
| 💚 Green | Income, Positive | Money earned |
| ❤️ Red | Expenses, Negative | Money spent |
| 🔵 Blue | Information | Neutral info |

### Category Icons & Colors
Each category has:
- **Emoji Icon**: Visual recognition
- **Custom Color**: Quick identification
- **Rounded Badge**: Modern look

### UI Elements
- **Cards**: White with rounded corners
- **Shadows**: Subtle depth effect
- **Gradients**: Purple gradient sidebar
- **Hover Effects**: Interactive feedback

## 📊 Understanding Your Reports

### Reading the Monthly Report

**Summary Section:**
```
Income: $8,500 ← Total money earned
Expenses: $3,254 ← Total money spent  
Net: $5,246 ← Your savings this month
```

**Category Breakdown:**
Shows where your money goes:
- Sorted by highest spending first
- Helps identify areas to cut back
- Visual representation of distribution

### Interpreting Percentages
```
🍕 Food ████████░░ 35% $850
```
- **35%** = Food is 35% of total expenses
- **$850** = Actual amount spent on food
- **Progress bar** = Visual representation

## 🔧 Customization Options

### Adding Custom Categories
Edit `Category.java` to add new categories with custom icons and colors.

### Changing Color Scheme
Edit `main.css` to customize:
- Sidebar colors
- Card borders
- Text colors
- Button styles

### Modifying Reports
Extend `ReportsView.java` to add:
- Custom date ranges
- New chart types
- Export features

## 📱 Interface Responsiveness

**Window Sizes:**
- Minimum: 900x600 pixels
- Recommended: 1200x800 pixels
- Fully resizable
- Scrollable content areas

**Adaptive Layout:**
- Content adapts to window size
- Scrollbars appear when needed
- Maintains readability at all sizes

## 🎓 Educational Value

**Learn About:**
- Personal finance management
- Income vs. Expense tracking
- Budget categorization
- Financial reporting
- Data analysis basics

**Skills Developed:**
- Financial awareness
- Organized record-keeping
- Spending pattern recognition
- Budget planning

---

**Happy tracking! May your income always exceed your expenses! 💰✨**
