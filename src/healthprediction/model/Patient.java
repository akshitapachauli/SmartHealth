package com.healthprediction.model;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Patient {
    private int patientId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private List<HealthRecord> healthRecords;
    private List<Appointment> appointments;
    private String existingConditions; 
    private String allergies; 
    private String medications; 
    
    public Patient() {
        this.healthRecords = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }
    
    public Patient(String firstName, String lastName, String email, String phone, 
                   LocalDate dateOfBirth, String gender, String address) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
    }
    
    
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public List<HealthRecord> getHealthRecords() { return healthRecords; }
    public void setHealthRecords(List<HealthRecord> healthRecords) { this.healthRecords = healthRecords; }
    
    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }

    public String getExistingConditions() { return existingConditions; }
    public void setExistingConditions(String existingConditions) { this.existingConditions = existingConditions; }

    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }

    public String getMedications() { return medications; }
    public void setMedications(String medications) { this.medications = medications; }
    
    public int getAge() {
        if (dateOfBirth != null) {
            return java.time.Period.between(dateOfBirth, LocalDate.now()).getYears();
        }
        return 0;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "Patient{" +
               "patientId=" + patientId +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", email='" + email + '\'' +
               ", phone='" + phone + '\'' +
               ", dateOfBirth=" + dateOfBirth +
               ", gender='" + gender + '\'' +
               ", address='" + address + '\'' +
               '}';
    }
}