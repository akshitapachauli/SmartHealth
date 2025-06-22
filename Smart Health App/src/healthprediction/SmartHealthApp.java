// SmartHealthApp.java - Main Application Entry Point
package com.healthprediction;

import com.healthprediction.ui.HealthPredictionGUI;
import com.healthprediction.service.DatabaseService;
import com.healthprediction.service.MLModelService;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class SmartHealthApp {
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
            
            // Initialize database
            DatabaseService.getInstance().initializeDatabase();
            
            // Initialize ML models
            MLModelService.getInstance().loadModels();
            
            // Launch GUI
            SwingUtilities.invokeLater(() -> {
                new HealthPredictionGUI().setVisible(true);
            });
            
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}