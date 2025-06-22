package com.healthprediction.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects; 

public class Appointment {
    private int appointmentId;
    private int patientId;
    private String patientName;
    private String doctorName;
    private int doctorId; 
    private String doctorSpecialization;
    private String appointmentType;
    private LocalDateTime appointmentDate;
    private int durationMinutes;
    private String status; 
    private String notes;
    private String symptoms;
    private String diagnosis;
    private String prescription;
    private double consultationFee;
    private boolean isEmergency;
    private LocalDateTime createdDate;
    private LocalDateTime lastModified;
    
    public Appointment() {
        this.createdDate = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
        this.status = "Scheduled";
        this.durationMinutes = 30; 
        this.isEmergency = false;
    }
    
    public Appointment(int patientId, String patientName, String doctorName, 
                      String appointmentType, LocalDateTime appointmentDate) {
        this();
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.appointmentType = appointmentType;
        this.appointmentDate = appointmentDate;
    }
    
    
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public String getDoctorSpecialization() { return doctorSpecialization; }
    public void setDoctorSpecialization(String doctorSpecialization) { this.doctorSpecialization = doctorSpecialization; }

    public String getAppointmentType() { return appointmentType; }
    public void setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; }

    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }

    public double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }

    public boolean isEmergency() { return isEmergency; }
    public void setEmergency(boolean emergency) { isEmergency = emergency; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getLastModified() { return lastModified; }
    public void setLastModified(LocalDateTime lastModified) { this.lastModified = lastModified; }

    public String getFormattedDate() {
        if (appointmentDate != null) {
            return appointmentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        return "N/A";
    }

    public boolean isCompleted() {
        return "Completed".equalsIgnoreCase(status);
    }
    
    public boolean isCancelled() {
        return "Cancelled".equalsIgnoreCase(status);
    }
    
    public boolean isScheduled() {
        return "Scheduled".equalsIgnoreCase(status);
    }
    
    public boolean isToday() {
        if (appointmentDate != null) {
            return appointmentDate.toLocalDate().equals(LocalDateTime.now().toLocalDate());
        }
        return false;
    }
    
    public boolean isPast() {
        if (appointmentDate != null) {
            return appointmentDate.isBefore(LocalDateTime.now());
        }
        return false;
    }
    
    public boolean isFuture() {
        if (appointmentDate != null) {
            return appointmentDate.isAfter(LocalDateTime.now());
        }
        return false;
    }
    
    @Override
    public String toString() {
        return String.format("Appointment{id=%d, patient='%s', doctor='%s', type='%s', date='%s', status='%s'}",
                appointmentId, patientName, doctorName, appointmentType, getFormattedDate(), status);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Appointment that = (Appointment) obj;
        return appointmentId == that.appointmentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointmentId);
    }
}