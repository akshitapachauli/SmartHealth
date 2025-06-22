// HealthRiskPredictor.java - ML component for Health Risk Prediction
package com.healthprediction.ml;

import com.healthprediction.model.HealthRecord;
import com.healthprediction.model.Patient;

import java.time.LocalDate;
import java.util.Random;

public class HealthRiskPredictor {

    // Placeholder for a loaded ML model. In a real application, this would be a complex model object.
    private boolean modelLoaded = false;

    public HealthRiskPredictor() {
        // Constructor, can be used for initial setup or
        // to indicate that the model needs to be loaded explicitly.
    }

    public void loadModel() throws Exception {
        // In a real application, this would load a pre-trained ML model
        // from a file (e.g., using Weka, scikit-learn's exported model, TensorFlow, etc.)
        System.out.println("Loading HealthRiskPredictor model...");
        // Simulate a delay for loading a complex model
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

        // --- Simplified Risk Prediction Logic (Placeholder) ---
        // In a real ML model, you would use features from patient and latestRecord
        // to feed into your trained model (e.g., Logistic Regression, Random Forest, Neural Network).

        double riskScore = 0.0;

        // Factors from HealthRecord
        if (latestRecord.getHeartRate() > 100 || latestRecord.getHeartRate() < 60) {
            riskScore += 0.2; // Abnormal heart rate
        }
        if (latestRecord.getSystolicBP() > 140 || latestRecord.getDiastolicBP() > 90) {
            riskScore += 0.3; // High blood pressure
        }
        if (latestRecord.getBloodSugar() > 120) {
            riskScore += 0.25; // High blood sugar
        }
        if (latestRecord.getCholesterol() > 200) {
            riskScore += 0.2; // High cholesterol
        }
        if ("Current".equalsIgnoreCase(latestRecord.getSmokingStatus())) {
            riskScore += 0.3; // Smoking is a major risk
        }
        if ("None".equalsIgnoreCase(latestRecord.getExerciseFrequency())) {
            riskScore += 0.15; // Lack of exercise
        }
        if (latestRecord.getStressLevel() > 7) {
            riskScore += 0.1; // High stress
        }
        if (latestRecord.getTemperature() > 37.5) { // Mild fever
            riskScore += 0.05;
        }

        // Factors from Patient (age, existing conditions)
        if (patient.getDateOfBirth() != null) {
            int age = java.time.Period.between(patient.getDateOfBirth(), LocalDate.now()).getYears();
            if (age >= 60) {
                riskScore += 0.2; // Higher risk for older age
            } else if (age >= 40) {
                riskScore += 0.1; // Moderate risk for middle age
            }
        }

        if (patient.getAllergies() != null && !patient.getAllergies().isEmpty()) {
             riskScore += 0.05; // Minor increase for allergies
        }
        if (patient.getMedications() != null && !patient.getMedications().isEmpty()) {
            // Assume patient on medication means they have a condition
            riskScore += 0.1;
        }

        // Ensure risk score is between 0 and 1
        riskScore = Math.min(1.0, riskScore);

        // Classify risk based on score
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
            return 0.0; // Cannot calculate score without data
        }

        // This is a simplified health score calculation (inverse of risk, but with
        // an emphasis on "good" values).
        // A more sophisticated model would assign weights or use ML regression.
        double score = 100.0;

        if (latestRecord.getHeartRate() > 90 || latestRecord.getHeartRate() < 70) score -= 10;
        if (latestRecord.getSystolicBP() > 130 || latestRecord.getDiastolicBP() > 85) score -= 15;
        if (latestRecord.getBloodSugar() > 100) score -= 15;
        if (latestRecord.getCholesterol() > 180) score -= 10;
        if ("Current".equalsIgnoreCase(latestRecord.getSmokingStatus())) score -= 25;
        if ("None".equalsIgnoreCase(latestRecord.getExerciseFrequency())) score -= 10;
        if (latestRecord.getStressLevel() > 5) score -= (latestRecord.getStressLevel() - 5) * 2; // Penalize more for higher stress

        return Math.max(0, Math.min(100, score)); // Ensure score is between 0 and 100
    }

    public void retrainModel(java.util.List<HealthRecord> trainingData) {
        // In a real application, this method would:
        // 1. Preprocess the trainingData.
        // 2. Train (or re-train) the underlying ML model.
        // 3. Save the updated model.
        System.out.println("Retraining HealthRiskPredictor model with " + trainingData.size() + " records...");
        // Simulate retraining process
        try {
            Thread.sleep(2000); // Simulate computation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("HealthRiskPredictor model retraining complete.");
        // After retraining, you might want to reload the model to ensure the new one is used
        // However, for this simple placeholder, we just acknowledge the retraining.
        // In a real system, `loadModel()` might be called internally or externally.
    }
}