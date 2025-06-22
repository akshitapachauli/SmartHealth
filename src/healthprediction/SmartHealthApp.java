package com.healthprediction;

import com.healthprediction.ui.HealthPredictionGUI;
import com.healthprediction.service.DatabaseService;
import com.healthprediction.service.MLModelService;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class SmartHealthApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            DatabaseService.getInstance().initializeDatabase();

            MLModelService.getInstance().loadModels();

            SwingUtilities.invokeLater(() -> {
                new HealthPredictionGUI().setVisible(true);
            });

        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}