package com.expensetracker.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class FinancialHelpView {
    private VBox view;
    
    public FinancialHelpView() {
        initializeView();
    }
    
    private void initializeView() {
        view = new VBox(25);
        view.setPadding(new Insets(20));
        
        // Title
        Label title = new Label("🆘 Financial Help & Resources");
        title.getStyleClass().add("section-title");
        
        Label subtitle = new Label("Expert guidance and resources to improve your financial health");
        subtitle.getStyleClass().add("subsection-title");
        subtitle.setStyle("-fx-text-fill: #718096;");
        
        // Content container
        VBox contentContainer = new VBox(20);
        
        // Financial Basics Section
        VBox basicsSection = createSection("📚 Financial Basics", 
            new String[]{
                "💰 Understanding Income & Expenses",
                "Track all money coming in (salary, investments, gifts) and going out (rent, food, bills). " +
                "The difference is your savings. Aim for at least 20% savings rate.",
                
                "📊 Creating a Budget",
                "List all income sources and categorize expenses. Allocate amounts to each category. " +
                "Review monthly and adjust as needed. Use this app to track automatically!",
                
                "🏦 Types of Bank Accounts",
                "Savings Account: For daily transactions and emergency funds. Fixed Deposits: Higher interest, " +
                "locked for a period. Recurring Deposits: Monthly savings with good returns.",
                
                "💳 Credit Score Basics",
                "Ranges from 300-900. Above 750 is excellent. Affects loan approvals and interest rates. " +
                "Build it by paying bills on time and using credit responsibly."
            });
        
        // Investment Guidance Section
        VBox investmentSection = createSection("📈 Smart Investing", 
            new String[]{
                "🎯 Investment Options in India",
                "PPF (Public Provident Fund): Safe, tax-free returns ~7-8% annually. EPF (Employee Provident Fund): " +
                "Mandatory for salaried, good returns. Mutual Funds: Higher returns, moderate risk. " +
                "Stock Market: High returns potential, higher risk. Gold: Traditional, stable value.",
                
                "🔐 Risk Management",
                "Never invest all money in one place. Diversify across stocks, bonds, gold, and fixed income. " +
                "Young investors can take more risk. As you age, shift to safer options.",
                
                "⏰ Start Early - Power of Compounding",
                "₹5000/month invested at 12% returns: After 10 years = ₹11.5 lakhs, After 20 years = ₹49.5 lakhs, " +
                "After 30 years = ₹1.76 crores! Time is your biggest asset.",
                
                "📱 Investment Apps & Platforms",
                "Zerodha, Groww, ET Money for mutual funds and stocks. Paytm Money for gold. " +
                "Coin by Zerodha for direct mutual funds. Always research before investing!"
            });
        
        // Tax Planning Section
        VBox taxSection = createSection("🧾 Tax Planning", 
            new String[]{
                "💡 Tax Saving Investments (Section 80C)",
                "Save up to ₹1.5 lakhs annually: EPF, PPF, ELSS Mutual Funds, Life Insurance, " +
                "Home Loan Principal, NSC, Sukanya Samriddhi Yojana. Plan before March end!",
                
                "🏥 Health Insurance Benefits (Section 80D)",
                "Deduct up to ₹25,000 for self/family health insurance. Additional ₹50,000 for parents above 60. " +
                "Provides tax benefit + medical coverage!",
                
                "🏠 Home Loan Tax Benefits",
                "Deduct up to ₹2 lakhs on home loan interest (Section 24). Additional ₹1.5 lakhs on principal " +
                "(Section 80C). First-time buyers get extra ₹1.5 lakhs (Section 80EEA).",
                
                "📋 ITR Filing Basics",
                "File Income Tax Return by July 31st every year. Use ITR-1 for salary income. " +
                "Keep Form 16, bank statements, investment proofs ready. E-verify within 30 days!"
            });
        
        // Emergency Situations Section
        VBox emergencySection = createSection("🚨 Emergency Financial Help", 
            new String[]{
                "📞 Immediate Cash Needs",
                "Personal Loan: Quick approval, higher interest. Gold Loan: Lower interest, requires gold. " +
                "Credit Card Cash Advance: Last resort, very high interest. Ask family/friends first!",
                
                "🏦 Government Schemes",
                "PM Jan Dhan Yojana: Free bank account with insurance. Mudra Loan: Small business loans up to " +
                "₹10 lakhs. PM Awas Yojana: Home loan subsidies. Atal Pension Yojana: Old age security.",
                
                "💼 Job Loss Support",
                "File for EPF withdrawal online. Apply for ESIC unemployment benefits if eligible. " +
                "Update resume on Naukri, LinkedIn immediately. Consider freelancing or gig work temporarily.",
                
                "🏥 Medical Emergency Funding",
                "Use health insurance first. Check employer medical benefits. PM Jan Arogya Yojana covers " +
                "up to ₹5 lakhs. Crowdfunding platforms: Ketto, Milaap for large medical bills."
            });
        
        // Helpful Resources Section
        VBox resourcesSection = createSection("🌐 Helpful Resources", 
            new String[]{
                "📱 Financial Literacy Apps",
                "ET Money (expense tracking), Money View (financial health score), Walnut (automatic expense " +
                "tracker), CRED (credit card management), Paytm (payments & investments).",
                
                "📺 YouTube Channels (Hindi/English)",
                "Labour Law Advisor, CA Rachana Ranade, Pranjal Kamra, Zerodha Varsity, The Money Therapy, " +
                "Asset Yogi, Financial Education, FinnovationZ.",
                
                "📰 Financial News & Blogs",
                "MoneyControl, Economic Times, LiveMint, ValueResearchOnline, Freefincal, Capitalmind, " +
                "The Ken, Bloomberg Quint for daily updates.",
                
                "🎓 Free Learning Resources",
                "NSE Academy: Free stock market courses. NISM: Financial certifications. RBI Financial Education: " +
                "Government approved content. Khan Academy: Basic finance concepts."
            });
        
        contentContainer.getChildren().addAll(
            basicsSection, investmentSection, taxSection, emergencySection, resourcesSection
        );
        
        // Important Notice
        VBox noticeBox = new VBox(10);
        noticeBox.getStyleClass().add("notice-box");
        noticeBox.setPadding(new Insets(20));
        noticeBox.setStyle("-fx-background-color: #FFF9E6; -fx-border-color: #F59E0B; -fx-border-width: 2; " +
                          "-fx-border-radius: 10; -fx-background-radius: 10;");
        
        Label noticeTitle = new Label("⚠️ Important Disclaimer");
        noticeTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #92400E;");
        
        Label noticeText = new Label(
            "This information is for educational purposes only and should not be considered as financial advice. " +
            "Always consult with a certified financial advisor or CA before making major financial decisions. " +
            "Investment returns are subject to market risks. Past performance doesn't guarantee future results."
        );
        noticeText.setWrapText(true);
        noticeText.setStyle("-fx-text-fill: #78350F; -fx-font-size: 13px;");
        
        noticeBox.getChildren().addAll(noticeTitle, noticeText);
        contentContainer.getChildren().add(noticeBox);
        
        ScrollPane scrollPane = new ScrollPane(contentContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("help-scroll");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        view.getChildren().addAll(title, subtitle, scrollPane);
    }
    
    private VBox createSection(String sectionTitle, String[] items) {
        VBox section = new VBox(15);
        section.getStyleClass().add("help-section");
        section.setPadding(new Insets(20));
        
        Label titleLabel = new Label(sectionTitle);
        titleLabel.getStyleClass().add("help-section-title");
        
        VBox itemsContainer = new VBox(12);
        
        for (int i = 0; i < items.length; i += 2) {
            if (i + 1 < items.length) {
                VBox item = createHelpItem(items[i], items[i + 1]);
                itemsContainer.getChildren().add(item);
            }
        }
        
        section.getChildren().addAll(titleLabel, itemsContainer);
        return section;
    }
    
    private VBox createHelpItem(String itemTitle, String itemContent) {
        VBox item = new VBox(8);
        item.getStyleClass().add("help-item");
        
        Label titleLabel = new Label(itemTitle);
        titleLabel.getStyleClass().add("help-item-title");
        titleLabel.setWrapText(true);
        
        Label contentLabel = new Label(itemContent);
        contentLabel.getStyleClass().add("help-item-content");
        contentLabel.setWrapText(true);
        
        item.getChildren().addAll(titleLabel, contentLabel);
        return item;
    }
    
    public VBox getView() {
        return view;
    }
}
