// AppointmentService.java - Appointment Service Class
package com.healthprediction.service;

import com.healthprediction.model.Appointment;
import com.healthprediction.model.Patient; // This import is not directly used in the provided snippet but good to keep if used elsewhere.
import com.healthprediction.model.Doctor; // Added import for Doctor

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AppointmentService {
    private DatabaseService dbService;
    private List<Appointment> appointments;
    private int nextAppointmentId;
    
    public AppointmentService() {
        this.dbService = DatabaseService.getInstance();
        this.appointments = new ArrayList<>();
        this.nextAppointmentId = 1;
        initializeSampleAppointments(); // This will interact with the DB
    }
    
    private void initializeSampleAppointments() {
        // Clear existing appointments to avoid duplicates on multiple runs
        try {
            dbService.deleteAllAppointments(); // Assuming such a method exists or can be added
        } catch (Exception e) {
            System.err.println("Could not clear old appointments: " + e.getMessage());
        }

        // Add some sample appointments for demonstration, persisting to DB
        // Fetch existing patients and doctors to link appointments
        List<Patient> patients = dbService.getAllPatients();
        List<Doctor> doctors = dbService.getAllDoctors(); // Assuming a getAllDoctors method exists

        if (patients.isEmpty() || doctors.isEmpty()) {
            System.out.println("Not enough sample patients or doctors to create sample appointments.");
            return;
        }

        try {
            Appointment apt1 = new Appointment(patients.get(0).getPatientId(), patients.get(0).getFirstName() + " " + patients.get(0).getLastName(),
                                             doctors.get(0).getFullName(), "Consultation", LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
            apt1.setDoctorId(doctors.get(0).getDoctorId());
            apt1.setDoctorSpecialization(doctors.get(0).getSpecialization());
            apt1.setConsultationFee(150.0);
            apt1.setDurationMinutes(45);
            dbService.saveAppointment(apt1); // Save to DB

            Appointment apt2 = new Appointment(patients.get(1).getPatientId(), patients.get(1).getFirstName() + " " + patients.get(1).getLastName(),
                                             doctors.get(1).getFullName(), "Follow-up", LocalDateTime.now().plusDays(2).withHour(11).withMinute(30));
            apt2.setDoctorId(doctors.get(1).getDoctorId());
            apt2.setDoctorSpecialization(doctors.get(1).getSpecialization());
            apt2.setConsultationFee(100.0);
            apt2.setStatus("Scheduled");
            dbService.saveAppointment(apt2); // Save to DB

            Appointment apt3 = new Appointment(patients.get(0).getPatientId(), patients.get(0).getFirstName() + " " + patients.get(0).getLastName(),
                                             doctors.get(0).getFullName(), "Check-up", LocalDateTime.now().minusDays(5).withHour(9).withMinute(0));
            apt3.setDoctorId(doctors.get(0).getDoctorId());
            apt3.setDoctorSpecialization(doctors.get(0).getSpecialization());
            apt3.setConsultationFee(120.0);
            apt3.setStatus("Completed");
            dbService.saveAppointment(apt3); // Save to DB

            System.out.println("Sample appointments initialized and saved to DB.");
        } catch (Exception e) {
            System.err.println("Error initializing sample appointments: " + e.getMessage());
            e.printStackTrace();
        }

        // After saving, load all from DB to the in-memory list
        loadAppointmentsFromDatabase();
    }

    public void loadAppointmentsFromDatabase() {
        try {
            this.appointments = dbService.getAllAppointments();
            if (!this.appointments.isEmpty()) {
                this.nextAppointmentId = this.appointments.stream()
                                            .mapToInt(Appointment::getAppointmentId)
                                            .max().orElse(0) + 1;
            } else {
                this.nextAppointmentId = 1;
            }
        } catch (Exception e) {
            System.err.println("Error loading appointments from database: " + e.getMessage());
            this.appointments = new ArrayList<>(); // Initialize to empty list on error
        }
    }

    public void addAppointment(Appointment appointment) {
        try {
            appointment.setAppointmentId(nextAppointmentId++);
            dbService.saveAppointment(appointment);
            this.appointments.add(appointment);
            System.out.println("Appointment added: " + appointment);
        } catch (Exception e) {
            System.err.println("Error adding appointment: " + e.getMessage());
        }
    }

    public Appointment getAppointmentById(int id) {
        return appointments.stream()
                .filter(apt -> apt.getAppointmentId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Appointment> getAllAppointments() {
        // Ensure the in-memory list is up-to-date with DB if needed,
        // or rely on explicit calls to loadAppointmentsFromDatabase()
        return new ArrayList<>(appointments); // Return a copy to prevent external modification
    }

    public List<Appointment> getAppointmentsForPatient(int patientId) {
        return appointments.stream()
                .filter(apt -> apt.getPatientId() == patientId)
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsForDoctor(int doctorId) {
        return appointments.stream()
                .filter(apt -> apt.getDoctorId() == doctorId)
                .collect(Collectors.toList());
    }

    public void updateAppointment(Appointment appointment) {
        try {
            dbService.updateAppointment(appointment); // Update in DB
            // Update in-memory list
            appointments.replaceAll(apt -> apt.getAppointmentId() == appointment.getAppointmentId() ? appointment : apt);
            System.out.println("Appointment updated: " + appointment);
        } catch (Exception e) {
            System.err.println("Error updating appointment: " + e.getMessage());
        }
    }

    public void cancelAppointment(int appointmentId) {
        try {
            dbService.updateAppointmentStatus(appointmentId, "Cancelled"); // Update status in DB
            Appointment apt = getAppointmentById(appointmentId);
            if (apt != null) {
                apt.setStatus("Cancelled"); // Update in-memory
                System.out.println("Appointment " + appointmentId + " cancelled.");
            }
        } catch (Exception e) {
            System.err.println("Error cancelling appointment: " + e.getMessage());
        }
    }

    public void completeAppointment(int appointmentId) {
        try {
            dbService.updateAppointmentStatus(appointmentId, "Completed"); // Update status in DB
            Appointment apt = getAppointmentById(appointmentId);
            if (apt != null) {
                apt.setStatus("Completed"); // Update in-memory
                System.out.println("Appointment " + appointmentId + " marked as completed.");
            }
        } catch (Exception e) {
            System.err.println("Error completing appointment: " + e.getMessage());
        }
    }

    // --- Analytics Methods (Using in-memory data for simplicity, can query DB directly) ---

    public Map<String, Integer> getAppointmentStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("total", appointments.size());
        stats.put("scheduled", (int) appointments.stream().filter(Appointment::isScheduled).count());
        stats.put("completed", (int) appointments.stream().filter(Appointment::isCompleted).count());
        stats.put("cancelled", (int) appointments.stream().filter(Appointment::isCancelled).count());
        stats.put("today", (int) appointments.stream().filter(Appointment::isToday).count());
        stats.put("upcoming", getUpcomingAppointments(7).size());
        stats.put("overdue", getOverdueAppointments().size());
        
        return stats;
    }

    public List<Appointment> getUpcomingAppointments(int daysAhead) {
        LocalDateTime now = LocalDateTime.now();
        return appointments.stream()
                .filter(apt -> apt.getAppointmentDate().isAfter(now) &&
                               apt.getAppointmentDate().isBefore(now.plusDays(daysAhead + 1)))
                .filter(Appointment::isScheduled) // Only count scheduled upcoming
                .collect(Collectors.toList());
    }

    public List<Appointment> getOverdueAppointments() {
        LocalDateTime now = LocalDateTime.now();
        return appointments.stream()
                .filter(apt -> apt.getAppointmentDate().isBefore(now) && apt.isScheduled())
                .collect(Collectors.toList());
    }
    
    public Map<String, Integer> getAppointmentsByType() {
        return appointments.stream()
                .collect(Collectors.groupingBy(
                    Appointment::getAppointmentType,
                    Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
                ));
    }
    
    public Map<String, Integer> getAppointmentsByDoctor() {
        return appointments.stream()
                .collect(Collectors.groupingBy(
                    Appointment::getDoctorName,
                    Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
                ));
    }
    
    public double getTotalRevenue() {
        return appointments.stream()
                .filter(Appointment::isCompleted)
                .mapToDouble(Appointment::getConsultationFee)
                .sum();
    }
    
    public double getRevenueByPeriod(LocalDate startDate, LocalDate endDate) {
        return appointments.stream()
                .filter(apt -> apt.isCompleted())
                .filter(apt -> {
                    LocalDate aptDate = apt.getAppointmentDate().toLocalDate();
                    return !aptDate.isBefore(startDate) && !aptDate.isAfter(endDate);
                })
                .mapToDouble(Appointment::getConsultationFee)
                .sum();
    }
}