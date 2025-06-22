package com.healthprediction.service;

import com.healthprediction.model.HealthRecord;
import com.healthprediction.model.Patient;
import com.healthprediction.ml.HealthRiskPredictor;
import com.healthprediction.ml.DiseasePredictor;
import com.healthprediction.ml.RecommendationEngine;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.sql.SQLException; 

public class MLModelService {
    private static MLModelService instance;
    private HealthRiskPredictor riskPredictor;
    private DiseasePredictor diseasePredictor;
    private RecommendationEngine recommendationEngine;
    private DatabaseService dbService; 
    private boolean modelsLoaded = false;
    
    private MLModelService() {
        this.dbService = DatabaseService.getInstance(); 
    }
    
    public static MLModelService getInstance() {
        if (instance == null) {
            instance = new MLModelService();
        }
        return instance;
    }
    
    public void loadModels() {
        try {
            riskPredictor = new HealthRiskPredictor();
            diseasePredictor = new DiseasePredictor();
            recommendationEngine = new RecommendationEngine();
            
            riskPredictor.loadModel();
            diseasePredictor.loadModel();
            recommendationEngine.loadModel();
            
            modelsLoaded = true;
            System.out.println("ML models loaded successfully");
        } catch (Exception e) {
            System.err.println("Error loading ML models: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Map<String, Object> getPredictionAndRecommendations(int patientId) {
        Map<String, Object> results = new HashMap<>();
        if (!modelsLoaded) {
            results.put("error", "ML models not loaded. Please restart application.");
            return results;
        }

        try {
            Patient patient = dbService.getPatientById(patientId);
            if (patient == null) {
                results.put("error", "Patient not found with ID: " + patientId);
                return results;
            }

            HealthRecord latestRecord = dbService.getLatestHealthRecordForPatient(patientId);
            if (latestRecord == null) {
                results.put("error", "No health records found for patient ID: " + patientId);
                return results;
            }

            
            String healthRisk = riskPredictor.predictRisk(patient, latestRecord);
            double healthScore = riskPredictor.getHealthScore(latestRecord);
            
            
            List<String> predictedDiseases = diseasePredictor.predictDiseases(patient, latestRecord);

            
            List<String> recommendations = recommendationEngine.generateRecommendations(
                patient, latestRecord, healthScore / 100.0, predictedDiseases 
            );

            results.put("healthRisk", healthRisk);
            results.put("healthScore", String.format("%.2f", healthScore));
            results.put("predictedDiseases", predictedDiseases);
            results.put("recommendations", recommendations);

        } catch (SQLException e) {
            System.err.println("Database error during prediction: " + e.getMessage());
            e.printStackTrace();
            results.put("error", "Database error: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println("ML Model error: " + e.getMessage());
            e.printStackTrace();
            results.put("error", "ML Model not ready: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error during prediction: " + e.getMessage());
            e.printStackTrace();
            results.put("error", "An unexpected error occurred: " + e.getMessage());
        }
        return results;
    }
}