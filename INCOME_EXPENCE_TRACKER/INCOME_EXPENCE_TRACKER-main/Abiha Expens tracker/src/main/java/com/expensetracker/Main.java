package com.expensetracker;

import com.expensetracker.ui.DashboardController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            DashboardController dashboard = new DashboardController();
            Scene scene = new Scene(dashboard.getView(), 1200, 800);
            
            // Load CSS stylesheet
            String css = getClass().getResource("/styles/main.css").toExternalForm();
            scene.getStylesheets().add(css);
            
            primaryStage.setTitle("EXPENSE TRACKER");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(900);
            primaryStage.setMinHeight(600);
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
