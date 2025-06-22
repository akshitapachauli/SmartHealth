package com.healthprediction.model;

import java.time.LocalDate;

public class HealthRecord {
    private int recordId;
    private int patientId;
    private LocalDate recordDate;
    private double heartRate;
    private double systolicBP;
    private double diastolicBP;
    private double bloodSugar;
    private double cholesterol;
    private double temperature;
    private String symptoms;
    private String diagnosis;
    private String medication;
    private String smokingStatus; 
    private String exerciseFrequency; 
    private int stressLevel; 
    private String notes;

    public HealthRecord() {
        this.recordDate = LocalDate.now();
    }

    public HealthRecord(int patientId, double heartRate, double systolicBP, double diastolicBP,
                        double bloodSugar, double cholesterol) {
        this();
        this.patientId = patientId;
        this.heartRate = heartRate;
        this.systolicBP = systolicBP;
        this.diastolicBP = diastolicBP;
        this.bloodSugar = bloodSugar;
        this.cholesterol = cholesterol;
    }

    
    public int getRecordId() { return recordId; }
    public void setRecordId(int recordId) { this.recordId = recordId; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public LocalDate getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDate recordDate) { this.recordDate = recordDate; }

    public double getHeartRate() { return heartRate; }
    public void setHeartRate(double heartRate) { this.heartRate = heartRate; }

    public double getSystolicBP() { return systolicBP; }
    public void setSystolicBP(double systolicBP) { this.systolicBP = systolicBP; }

    public double getDiastolicBP() { return diastolicBP; }
    public void setDiastolicBP(double diastolicBP) { this.diastolicBP = diastolicBP; }

    public double getBloodSugar() { return bloodSugar; }
    public void setBloodSugar(double bloodSugar) { this.bloodSugar = bloodSugar; }

    public double getCholesterol() { return cholesterol; }
    public void setCholesterol(double cholesterol) { this.cholesterol = cholesterol; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getMedication() { return medication; }
    public void setMedication(String medication) { this.medication = medication; }

    public String getSmokingStatus() { return smokingStatus; }
    public void setSmokingStatus(String smokingStatus) { this.smokingStatus = smokingStatus; }

    public String getExerciseFrequency() { return exerciseFrequency; }
    public void setExerciseFrequency(String exerciseFrequency) { this.exerciseFrequency = exerciseFrequency; }

    public int getStressLevel() { return stressLevel; }
    public void setStressLevel(int stressLevel) { this.stressLevel = stressLevel; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "HealthRecord{" +
               "recordId=" + recordId +
               ", patientId=" + patientId +
               ", recordDate=" + recordDate +
               ", heartRate=" + heartRate +
               ", systolicBP=" + systolicBP +
               ", diastolicBP=" + diastolicBP +
               '}';
    }
}