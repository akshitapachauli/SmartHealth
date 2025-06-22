// RecommendationEngine.java - ML component for Health Recommendations
package com.healthprediction.ml;

import com.healthprediction.model.HealthRecord;
import com.healthprediction.model.Patient;

import java.util.ArrayList;
import java.util.List;

public class RecommendationEngine {

    // Placeholder for a loaded ML model or rule set.
    private boolean modelLoaded = false;

    public RecommendationEngine() {
        // Constructor
    }

    public void loadModel() throws Exception {
        System.out.println("Loading RecommendationEngine model...");
        Thread.sleep(400); // Simulate loading time
        modelLoaded = true;
        System.out.println("RecommendationEngine model loaded.");
    }

    /**
     * Generates personalized health recommendations based on patient data and health records.
     * This is a rule-based placeholder. A real system might use collaborative filtering,
     * content-based recommendations, or deep learning models.
     *
     * @param patient The patient object.
     * @param latestRecord The latest health record for the patient.
     * @param riskScore The health risk score (e.g., from HealthRiskPredictor).
     * @param predictedDiseases A list of predicted diseases (e.g., from DiseasePredictor).
     * @return A list of tailored health recommendations.
     */
    public List<String> generateRecommendations(Patient patient, HealthRecord latestRecord,
                                               double riskScore, List<String> predictedDiseases)
                                               throws IllegalStateException {
        if (!modelLoaded) {
            throw new IllegalStateException("RecommendationEngine model not loaded. Call loadModel() first.");
        }

        List<String> recommendations = new ArrayList<>();

        // General healthy lifestyle recommendations
        recommendations.add("Maintain a balanced diet rich in fruits, vegetables, and whole grains.");
        recommendations.add("Aim for at least 30 minutes of moderate-intensity exercise most days of the week.");
        recommendations.add("Ensure 7-9 hours of quality sleep per night.");
        recommendations.add("Stay hydrated by drinking plenty of water throughout the day.");
        recommendations.add("Practice stress-reducing techniques like meditation or yoga.");

        // Recommendations based on latest health record
        if (latestRecord != null) {
            if (latestRecord.getSystolicBP() > 130 || latestRecord.getDiastolicBP() > 85) {
                recommendations.add("Monitor blood pressure regularly and consider low-sodium diet.");
            }
            if (latestRecord.getBloodSugar() > 100) {
                recommendations.add("Limit intake of sugary foods and refined carbohydrates.");
                recommendations.add("Consult a nutritionist for dietary guidance on blood sugar management.");
            }
            if (latestRecord.getCholesterol() > 180) {
                recommendations.add("Reduce saturated and trans fats in your diet. Increase fiber intake.");
            }
            if ("Current".equalsIgnoreCase(latestRecord.getSmokingStatus())) {
                recommendations.add("Strongly consider smoking cessation programs for overall health improvement.");
            }
            if ("None".equalsIgnoreCase(latestRecord.getExerciseFrequency())) {
                recommendations.add("Gradually introduce regular physical activity into your routine.");
            }
            if (latestRecord.getStressLevel() > 6) {
                recommendations.add("Explore stress management techniques like deep breathing or mindfulness.");
                recommendations.add("Consider speaking with a counselor if stress is overwhelming.");
            }
        }

        // Recommendations based on predicted diseases
        if (predictedDiseases != null && !predictedDiseases.isEmpty()) {
            for (String disease : predictedDiseases) {
                if (disease.contains("Hypertension") || disease.contains("Heart Disease")) {
                    recommendations.add("Schedule a consultation with a cardiologist for detailed assessment.");
                    recommendations.add("Follow a heart-healthy diet (DASH diet recommended).");
                } else if (disease.contains("Diabetes")) {
                    recommendations.add("Consult an endocrinologist for diabetes management plan.");
                    recommendations.add("Regular blood sugar monitoring is crucial.");
                } else if (disease.contains("Respiratory Infection")) {
                    recommendations.add("Rest adequately and avoid close contact with others to prevent spread.");
                    recommendations.add("Seek medical advice if symptoms worsen or persist.");
                }
                // Add more specific recommendations for other predicted diseases
            }
        }

        // Recommendations based on overall risk score
        if (riskScore > 0.7) {
            recommendations.add("URGENT: Consult a medical professional immediately for a comprehensive check-up.");
            recommendations.add("Do not delay any prescribed treatments or lifestyle changes.");
        } else if (riskScore > 0.4) {
            recommendations.add("Schedule a follow-up appointment with your primary care physician to discuss health risks.");
        }

        return recommendations;
    }

    public void retrainModel(List<Patient> patients, List<HealthRecord> healthRecords) {
        // This method would refine the recommendation logic.
        // For a rule-based system, it might mean updating the rules or weights.
        // For an ML-based system, it would involve training a recommendation model
        // based on patient history, outcomes, and successful interventions.
        System.out.println("Retraining RecommendationEngine model with patient and health record data...");
        try {
            Thread.sleep(2500); // Simulate computation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("RecommendationEngine model retraining complete.");
    }
}