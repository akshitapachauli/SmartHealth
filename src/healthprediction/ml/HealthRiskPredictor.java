package com.healthprediction.ml;

import com.healthprediction.model.HealthRecord;
import com.healthprediction.model.Patient;

import java.time.LocalDate;
import java.util.Random;

public class HealthRiskPredictor {

    
    private boolean modelLoaded = false;

    public HealthRiskPredictor() {
        
        
    }

    public void loadModel() throws Exception {
        
        
        System.out.println("Loading HealthRiskPredictor model...");
        
        Thread.sleep(500);
        modelLoaded = true;
        System.out.println("HealthRiskPredictor model loaded.");
    }

    public String predictRisk(Patient patient, HealthRecord latestRecord) throws IllegalStateException {
        if (!modelLoaded) {
            throw new IllegalStateException("HealthRiskPredictor model not loaded. Call loadModel() first.");
        }

        if (latestRecord == null) {
            return "No health data available for prediction.";
        }

        
        
        

        double riskScore = 0.0;

        
        if (latestRecord.getHeartRate() > 100 || latestRecord.getHeartRate() < 60) {
            riskScore += 0.2; 
        }
        if (latestRecord.getSystolicBP() > 140 || latestRecord.getDiastolicBP() > 90) {
            riskScore += 0.3; 
        }
        if (latestRecord.getBloodSugar() > 120) {
            riskScore += 0.25; 
        }
        if (latestRecord.getCholesterol() > 200) {
            riskScore += 0.2; 
        }
        if ("Current".equalsIgnoreCase(latestRecord.getSmokingStatus())) {
            riskScore += 0.3; 
        }
        if ("None".equalsIgnoreCase(latestRecord.getExerciseFrequency())) {
            riskScore += 0.15; 
        }
        if (latestRecord.getStressLevel() > 7) {
            riskScore += 0.1; 
        }
        if (latestRecord.getTemperature() > 37.5) { 
            riskScore += 0.05;
        }

        
        if (patient.getDateOfBirth() != null) {
            int age = java.time.Period.between(patient.getDateOfBirth(), LocalDate.now()).getYears();
            if (age >= 60) {
                riskScore += 0.2; 
            } else if (age >= 40) {
                riskScore += 0.1; 
            }
        }

        if (patient.getAllergies() != null && !patient.getAllergies().isEmpty()) {
             riskScore += 0.05; 
        }
        if (patient.getMedications() != null && !patient.getMedications().isEmpty()) {
            
            riskScore += 0.1;
        }

        
        riskScore = Math.min(1.0, riskScore);

        
        if (riskScore > 0.7) {
            return "High Risk (Score: " + String.format("%.2f", riskScore) + ") - Immediate medical attention advised.";
        } else if (riskScore > 0.4) {
            return "Moderate Risk (Score: " + String.format("%.2f", riskScore) + ") - Consult a doctor soon for evaluation.";
        } else {
            return "Low Risk (Score: " + String.format("%.2f", riskScore) + ") - Maintain healthy lifestyle, routine check-up recommended.";
        }
    }

    public double getHealthScore(HealthRecord latestRecord) throws IllegalStateException {
        if (!modelLoaded) {
            throw new IllegalStateException("HealthRiskPredictor model not loaded. Call loadModel() first.");
        }
        if (latestRecord == null) {
            return 0.0; 
        }

        
        
        
        double score = 100.0;

        if (latestRecord.getHeartRate() > 90 || latestRecord.getHeartRate() < 70) score -= 10;
        if (latestRecord.getSystolicBP() > 130 || latestRecord.getDiastolicBP() > 85) score -= 15;
        if (latestRecord.getBloodSugar() > 100) score -= 15;
        if (latestRecord.getCholesterol() > 180) score -= 10;
        if ("Current".equalsIgnoreCase(latestRecord.getSmokingStatus())) score -= 25;
        if ("None".equalsIgnoreCase(latestRecord.getExerciseFrequency())) score -= 10;
        if (latestRecord.getStressLevel() > 5) score -= (latestRecord.getStressLevel() - 5) * 2; 

        return Math.max(0, Math.min(100, score)); 
    }

    public void retrainModel(java.util.List<HealthRecord> trainingData) {
        
        
        
        
        System.out.println("Retraining HealthRiskPredictor model with " + trainingData.size() + " records...");
        
        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("HealthRiskPredictor model retraining complete.");
        
        
        
    }
}