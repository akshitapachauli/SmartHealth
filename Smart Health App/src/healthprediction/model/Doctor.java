// Doctor.java - Doctor Model Class
package com.healthprediction.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Doctor {
    private int doctorId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String specialization;
    private String qualification;
    private int experienceYears;
    private String licenseNumber;
    private String address;
    private double consultationFee;
    private LocalTime workingHoursStart;
    private LocalTime workingHoursEnd;
    private List<String> workingDays;
    private boolean isAvailable;
    private String department;
    private String biography;
    private List<String> languages;
    private String imageUrl;
    private LocalDate joinDate;
    private double rating;
    private int totalPatients;
    private boolean isActive;
    
    public Doctor() {
        this.workingDays = new ArrayList<>();
        this.languages = new ArrayList<>();
        this.isAvailable = true;
        this.isActive = true;
        this.rating = 0.0;
        this.totalPatients = 0;
        this.joinDate = LocalDate.now();
        
        // Default working hours
        this.workingHoursStart = LocalTime.of(9, 0);
        this.workingHoursEnd = LocalTime.of(17, 0);
        
        // Default working days
        this.workingDays.add("Monday");
        this.workingDays.add("Tuesday");
        this.workingDays.add("Wednesday");
        this.workingDays.add("Thursday");
        this.workingDays.add("Friday");
    }
    
    public Doctor(String firstName, String lastName, String specialization, String email) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.email = email;
    }
    
    // Getters and Setters
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getFullName() { return firstName + " " + lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    
    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
    
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }
    
    public LocalTime getWorkingHoursStart() { return workingHoursStart; }
    public void setWorkingHoursStart(LocalTime workingHoursStart) { this.workingHoursStart = workingHoursStart; }
    
    public LocalTime getWorkingHoursEnd() { return workingHoursEnd; }
    public void setWorkingHoursEnd(LocalTime workingHoursEnd) { this.workingHoursEnd = workingHoursEnd; }
    
    public List<String> getWorkingDays() { return workingDays; }
    public void setWorkingDays(List<String> workingDays) { this.workingDays = workingDays; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }
    
    public List<String> getLanguages() { return languages; }
    public void setLanguages(List<String> languages) { this.languages = languages; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }
    
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    
    public int getTotalPatients() { return totalPatients; }
    public void setTotalPatients(int totalPatients) { this.totalPatients = totalPatients; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    @Override
    public String toString() {
        return "Doctor{" +
               "doctorId=" + doctorId +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", specialization='" + specialization + '\'' +
               '}';
    }
}