// DiseasePredictor.java - ML component for Disease Prediction
package com.healthprediction.ml;

import com.healthprediction.model.HealthRecord;
import com.healthprediction.model.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiseasePredictor {

    // Placeholder for a loaded ML model.
    private boolean modelLoaded = false;
    private Random random = new Random();

    public DiseasePredictor() {
        // Constructor
    }

    public void loadModel() throws Exception {
        System.out.println("Loading DiseasePredictor model...");
        Thread.sleep(700); // Simulate loading time
        modelLoaded = true;
        System.out.println("DiseasePredictor model loaded.");
    }

    /**
     * Predicts potential diseases based on the patient's latest health record.
     * This is a highly simplified, rule-based placeholder. A real system would use
     * complex ML models trained on vast datasets.
     *
     * @param patient The patient object.
     * @param latestRecord The latest health record for the patient.
     * @return A list of predicted diseases.
     */
    public List<String> predictDiseases(Patient patient, HealthRecord latestRecord) throws IllegalStateException {
        if (!modelLoaded) {
            throw new IllegalStateException("DiseasePredictor model not loaded. Call loadModel() first.");
        }

        List<String> predictedDiseases = new ArrayList<>();

        if (latestRecord == null) {
            predictedDiseases.add("No sufficient health data to predict diseases.");
            return predictedDiseases;
        }

        // --- Simplified Disease Prediction Rules (Placeholder) ---

        // Cardiovascular diseases
        if ((latestRecord.getSystolicBP() > 140 && latestRecord.getDiastolicBP() > 90) ||
            latestRecord.getCholesterol() > 240) {
            predictedDiseases.add("Hypertension/Heart Disease Risk");
        } else if (latestRecord.getSystolicBP() > 130 || latestRecord.getDiastolicBP() > 85) {
            predictedDiseases.add("Pre-hypertension/Elevated Cholesterol");
        }

        // Diabetes
        if (latestRecord.getBloodSugar() > 125) {
            predictedDiseases.add("Diabetes Mellitus Type 2 (High Sugar)");
        } else if (latestRecord.getBloodSugar() > 100) {
            predictedDiseases.add("Pre-diabetes");
        }

        // Respiratory issues (simple cough/fever example)
        if (latestRecord.getTemperature() > 38.0 && latestRecord.getSymptoms() != null &&
            latestRecord.getSymptoms().toLowerCase().contains("cough")) {
            predictedDiseases.add("Respiratory Infection (e.g., Flu/Common Cold)");
        }

        // General inflammation/infection
        if (latestRecord.getTemperature() > 37.8 && !predictedDiseases.contains("Respiratory Infection (e.g., Flu/Common Cold)")) {
             predictedDiseases.add("Possible General Infection");
        }


        // Add a random chance of a common ailment for demo purposes
        if (random.nextDouble() < 0.1) { // 10% chance
            predictedDiseases.add("Seasonal Allergies");
        }
        if (random.nextDouble() < 0.05) { // 5% chance
            predictedDiseases.add("Vitamin D Deficiency");
        }

        if (predictedDiseases.isEmpty()) {
            predictedDiseases.add("No specific disease risk predicted based on current data.");
        }

        return predictedDiseases;
    }

    public void retrainModel(java.util.List<HealthRecord> trainingData, java.util.List<Patient> patientsData) {
        // This method would train the disease prediction model.
        // It would likely involve feature engineering from HealthRecord and Patient data,
        // and then training a classification model (e.g., SVM, Decision Tree, Naive Bayes).
        System.out.println("Retraining DiseasePredictor model with " + trainingData.size() + " health records...");
        try {
            Thread.sleep(3000); // Simulate computation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("DiseasePredictor model retraining complete.");
    }
}