package com.healthprediction.service;

import com.healthprediction.model.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime; 
import java.util.List;
import java.util.ArrayList;

public class DatabaseService {
    private static DatabaseService instance;
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:health_prediction.db"; 
    
    private DatabaseService() {}
    
    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }
    
    public void initializeDatabase() {
        try {
            
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            createTables();
            insertSampleData(); 
            System.out.println("Database initialized successfully");
        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace(); 
        }
    }
    
    private void createTables() throws SQLException {
        Statement stmt = connection.createStatement();

        
        String createPatientsTable = """
            CREATE TABLE IF NOT EXISTS patients (
                patient_id INTEGER PRIMARY KEY AUTOINCREMENT,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                email TEXT UNIQUE,
                phone TEXT,
                date_of_birth TEXT,
                gender TEXT,
                address TEXT,
                existing_conditions TEXT,
                allergies TEXT,
                medications TEXT
            );
            """;
        stmt.execute(createPatientsTable);

        
        String createDoctorsTable = """
            CREATE TABLE IF NOT EXISTS doctors (
                doctor_id INTEGER PRIMARY KEY AUTOINCREMENT,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                email TEXT UNIQUE,
                phone TEXT,
                specialization TEXT,
                qualification TEXT,
                experience_years INTEGER,
                license_number TEXT UNIQUE,
                address TEXT,
                consultation_fee REAL,
                working_hours_start TEXT,
                working_hours_end TEXT,
                working_days TEXT,
                is_available INTEGER,
                department TEXT,
                biography TEXT,
                languages TEXT,
                image_url TEXT,
                join_date TEXT,
                rating REAL,
                total_patients INTEGER,
                is_active INTEGER
            );
            """;
        stmt.execute(createDoctorsTable);

        
        String createAppointmentsTable = """
            CREATE TABLE IF NOT EXISTS appointments (
                appointment_id INTEGER PRIMARY KEY AUTOINCREMENT,
                patient_id INTEGER NOT NULL,
                patient_name TEXT,
                doctor_id INTEGER NOT NULL,
                doctor_name TEXT,
                doctor_specialization TEXT,
                appointment_type TEXT,
                appointment_datetime TEXT NOT NULL,
                duration_minutes INTEGER,
                status TEXT,
                notes TEXT,
                symptoms TEXT,
                diagnosis TEXT,
                prescription TEXT,
                consultation_fee REAL,
                is_emergency INTEGER,
                created_date TEXT,
                last_modified TEXT,
                FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
                FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)
            );
            """;
        stmt.execute(createAppointmentsTable);

        
        String createHealthRecordsTable = """
            CREATE TABLE IF NOT EXISTS health_records (
                record_id INTEGER PRIMARY KEY AUTOINCREMENT,
                patient_id INTEGER NOT NULL,
                record_date TEXT NOT NULL,
                heart_rate REAL,
                systolic_bp REAL,
                diastolic_bp REAL,
                blood_sugar REAL,
                cholesterol REAL,
                temperature REAL,
                symptoms TEXT,
                diagnosis TEXT,
                medication TEXT,
                smoking_status TEXT,
                exercise_frequency TEXT,
                stress_level INTEGER,
                notes TEXT,
                FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
            );
            """;
        stmt.execute(createHealthRecordsTable);
        
        stmt.close();
    }

    private void insertSampleData() throws SQLException {
        
        Statement stmt = connection.createStatement();

        ResultSet rsPatients = stmt.executeQuery("SELECT COUNT(*) FROM patients;");
        if (rsPatients.next() && rsPatients.getInt(1) == 0) {
            System.out.println("Inserting sample patients...");
            Patient p1 = new Patient("John", "Doe", "john.doe@example.com", "123-456-7890",
                                    LocalDate.of(1985, 5, 15), "Male", "123 Main St");
            p1.setExistingConditions("Hypertension");
            p1.setAllergies("Pollen");
            p1.setMedications("Lisinopril");
            savePatient(p1);

            Patient p2 = new Patient("Jane", "Smith", "jane.smith@example.com", "098-765-4321",
                                    LocalDate.of(1992, 11, 22), "Female", "456 Oak Ave");
            p2.setExistingConditions("None");
            p2.setAllergies("None");
            p2.setMedications("None");
            savePatient(p2);

            Patient p3 = new Patient("Alice", "Johnson", "alice.j@example.com", "111-222-3333",
                                    LocalDate.of(1970, 3, 10), "Female", "789 Pine Rd");
            p3.setExistingConditions("Diabetes");
            p3.setAllergies("Penicillin");
            p3.setMedications("Metformin");
            savePatient(p3);
        }
        rsPatients.close();

        ResultSet rsDoctors = stmt.executeQuery("SELECT COUNT(*) FROM doctors;");
        if (rsDoctors.next() && rsDoctors.getInt(1) == 0) {
            System.out.println("Inserting sample doctors...");
            Doctor d1 = new Doctor();
            d1.setFirstName("Sarah");
            d1.setLastName("Smith");
            d1.setEmail("sarah.s@example.com");
            d1.setSpecialization("Cardiology");
            saveDoctor(d1);

            Doctor d2 = new Doctor();
            d2.setFirstName("Alex");
            d2.setLastName("Johnson");
            d2.setEmail("alex.j@example.com");
            d2.setSpecialization("General Medicine");
            saveDoctor(d2);

            Doctor d3 = new Doctor();
            d3.setFirstName("Emily");
            d3.setLastName("White");
            d3.setEmail("emily.w@example.com");
            d3.setSpecialization("Pediatrics");
            saveDoctor(d3);
        }
        rsDoctors.close();

        stmt.close();
        
    }

    
    public void savePatient(Patient patient) throws SQLException {
        String sql = "INSERT INTO patients (first_name, last_name, email, phone, date_of_birth, gender, address, existing_conditions, allergies, medications) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, patient.getFirstName());
            pstmt.setString(2, patient.getLastName());
            pstmt.setString(3, patient.getEmail());
            pstmt.setString(4, patient.getPhone());
            pstmt.setString(5, patient.getDateOfBirth().toString());
            pstmt.setString(6, patient.getGender());
            pstmt.setString(7, patient.getAddress());
            pstmt.setString(8, patient.getExistingConditions());
            pstmt.setString(9, patient.getAllergies());
            pstmt.setString(10, patient.getMedications());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    patient.setPatientId(generatedKeys.getInt(1));
                }
            }
            System.out.println("Patient saved: " + patient.getFullName());
        }
    }

    public Patient getPatientById(int id) throws SQLException {
        String sql = "SELECT * FROM patients WHERE patient_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToPatient(rs);
            }
        }
        return null;
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all patients: " + e.getMessage());
        }
        return patients;
    }

    public List<Patient> searchPatients(String searchTerm) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE first_name LIKE ? OR last_name LIKE ? OR email LIKE ? OR phone LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching patients: " + e.getMessage());
        }
        return patients;
    }

    public void updatePatient(Patient patient) throws SQLException {
        String sql = "UPDATE patients SET first_name=?, last_name=?, email=?, phone=?, date_of_birth=?, gender=?, address=?, existing_conditions=?, allergies=?, medications=? WHERE patient_id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, patient.getFirstName());
            pstmt.setString(2, patient.getLastName());
            pstmt.setString(3, patient.getEmail());
            pstmt.setString(4, patient.getPhone());
            pstmt.setString(5, patient.getDateOfBirth().toString());
            pstmt.setString(6, patient.getGender());
            pstmt.setString(7, patient.getAddress());
            pstmt.setString(8, patient.getExistingConditions());
            pstmt.setString(9, patient.getAllergies());
            pstmt.setString(10, patient.getMedications());
            pstmt.setInt(11, patient.getPatientId());
            pstmt.executeUpdate();
            System.out.println("Patient updated: " + patient.getFullName());
        }
    }

    public void deletePatient(int patientId) throws SQLException {
        String sql = "DELETE FROM patients WHERE patient_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            pstmt.executeUpdate();
            System.out.println("Patient deleted: " + patientId);
        }
    }

    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setPatientId(rs.getInt("patient_id"));
        patient.setFirstName(rs.getString("first_name"));
        patient.setLastName(rs.getString("last_name"));
        patient.setEmail(rs.getString("email"));
        patient.setPhone(rs.getString("phone"));
        patient.setDateOfBirth(LocalDate.parse(rs.getString("date_of_birth")));
        patient.setGender(rs.getString("gender"));
        patient.setAddress(rs.getString("address"));
        patient.setExistingConditions(rs.getString("existing_conditions"));
        patient.setAllergies(rs.getString("allergies"));
        patient.setMedications(rs.getString("medications"));
        return patient;
    }

    
    public void saveDoctor(Doctor doctor) throws SQLException {
        String sql = "INSERT INTO doctors (first_name, last_name, email, phone, specialization, qualification, experience_years, license_number, address, consultation_fee, working_hours_start, working_hours_end, working_days, is_available, department, biography, languages, image_url, join_date, rating, total_patients, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, doctor.getFirstName());
            pstmt.setString(2, doctor.getLastName());
            pstmt.setString(3, doctor.getEmail());
            pstmt.setString(4, doctor.getPhone());
            pstmt.setString(5, doctor.getSpecialization());
            pstmt.setString(6, doctor.getQualification());
            pstmt.setInt(7, doctor.getExperienceYears());
            pstmt.setString(8, doctor.getLicenseNumber());
            pstmt.setString(9, doctor.getAddress());
            pstmt.setDouble(10, doctor.getConsultationFee());
            pstmt.setString(11, doctor.getWorkingHoursStart() != null ? doctor.getWorkingHoursStart().toString() : null);
            pstmt.setString(12, doctor.getWorkingHoursEnd() != null ? doctor.getWorkingHoursEnd().toString() : null);
            pstmt.setString(13, String.join(",", doctor.getWorkingDays())); 
            pstmt.setInt(14, doctor.isAvailable() ? 1 : 0);
            pstmt.setString(15, doctor.getDepartment());
            pstmt.setString(16, doctor.getBiography());
            pstmt.setString(17, String.join(",", doctor.getLanguages())); 
            pstmt.setString(18, doctor.getImageUrl());
            pstmt.setString(19, doctor.getJoinDate() != null ? doctor.getJoinDate().toString() : null);
            pstmt.setDouble(20, doctor.getRating());
            pstmt.setInt(21, doctor.getTotalPatients());
            pstmt.setInt(22, doctor.isActive() ? 1 : 0);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    doctor.setDoctorId(generatedKeys.getInt(1));
                }
            }
            System.out.println("Doctor saved: " + doctor.getFullName());
        }
    }

    public Doctor getDoctorById(int id) throws SQLException {
        String sql = "SELECT * FROM doctors WHERE doctor_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToDoctor(rs);
            }
        }
        return null;
    }

    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                doctors.add(mapResultSetToDoctor(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all doctors: " + e.getMessage());
        }
        return doctors;
    }

    private Doctor mapResultSetToDoctor(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(rs.getInt("doctor_id"));
        doctor.setFirstName(rs.getString("first_name"));
        doctor.setLastName(rs.getString("last_name"));
        doctor.setEmail(rs.getString("email"));
        doctor.setPhone(rs.getString("phone"));
        doctor.setSpecialization(rs.getString("specialization"));
        doctor.setQualification(rs.getString("qualification"));
        doctor.setExperienceYears(rs.getInt("experience_years"));
        doctor.setLicenseNumber(rs.getString("license_number"));
        doctor.setAddress(rs.getString("address"));
        doctor.setConsultationFee(rs.getDouble("consultation_fee"));
        doctor.setWorkingHoursStart(rs.getString("working_hours_start") != null ? LocalTime.parse(rs.getString("working_hours_start")) : null);
        doctor.setWorkingHoursEnd(rs.getString("working_hours_end") != null ? LocalTime.parse(rs.getString("working_hours_end")) : null);
        doctor.setWorkingDays(List.of(rs.getString("working_days").split(",")));
        doctor.setAvailable(rs.getInt("is_available") == 1);
        doctor.setDepartment(rs.getString("department"));
        doctor.setBiography(rs.getString("biography"));
        doctor.setLanguages(List.of(rs.getString("languages").split(",")));
        doctor.setImageUrl(rs.getString("image_url"));
        doctor.setJoinDate(rs.getString("join_date") != null ? LocalDate.parse(rs.getString("join_date")) : null);
        doctor.setRating(rs.getDouble("rating"));
        doctor.setTotalPatients(rs.getInt("total_patients"));
        doctor.setActive(rs.getInt("is_active") == 1);
        return doctor;
    }

    
    public void saveAppointment(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointments (patient_id, patient_name, doctor_id, doctor_name, doctor_specialization, appointment_type, appointment_datetime, duration_minutes, status, notes, symptoms, diagnosis, prescription, consultation_fee, is_emergency, created_date, last_modified) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, appointment.getPatientId());
            pstmt.setString(2, appointment.getPatientName());
            pstmt.setInt(3, appointment.getDoctorId());
            pstmt.setString(4, appointment.getDoctorName());
            pstmt.setString(5, appointment.getDoctorSpecialization());
            pstmt.setString(6, appointment.getAppointmentType());
            pstmt.setString(7, appointment.getAppointmentDate().toString());
            pstmt.setInt(8, appointment.getDurationMinutes());
            pstmt.setString(9, appointment.getStatus());
            pstmt.setString(10, appointment.getNotes());
            pstmt.setString(11, appointment.getSymptoms());
            pstmt.setString(12, appointment.getDiagnosis());
            pstmt.setString(13, appointment.getPrescription());
            pstmt.setDouble(14, appointment.getConsultationFee());
            pstmt.setInt(15, appointment.isEmergency() ? 1 : 0);
            pstmt.setString(16, appointment.getCreatedDate().toString());
            pstmt.setString(17, appointment.getLastModified().toString());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    appointment.setAppointmentId(generatedKeys.getInt(1));
                }
            }
            System.out.println("Appointment saved: " + appointment.getAppointmentId());
        }
    }

    public Appointment getAppointmentById(int id) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE appointment_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToAppointment(rs);
            }
        }
        return null;
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all appointments: " + e.getMessage());
        }
        return appointments;
    }

    public void updateAppointment(Appointment appointment) throws SQLException {
        String sql = "UPDATE appointments SET patient_id=?, patient_name=?, doctor_id=?, doctor_name=?, doctor_specialization=?, appointment_type=?, appointment_datetime=?, duration_minutes=?, status=?, notes=?, symptoms=?, diagnosis=?, prescription=?, consultation_fee=?, is_emergency=?, last_modified=? WHERE appointment_id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, appointment.getPatientId());
            pstmt.setString(2, appointment.getPatientName());
            pstmt.setInt(3, appointment.getDoctorId());
            pstmt.setString(4, appointment.getDoctorName());
            pstmt.setString(5, appointment.getDoctorSpecialization());
            pstmt.setString(6, appointment.getAppointmentType());
            pstmt.setString(7, appointment.getAppointmentDate().toString());
            pstmt.setInt(8, appointment.getDurationMinutes());
            pstmt.setString(9, appointment.getStatus());
            pstmt.setString(10, appointment.getNotes());
            pstmt.setString(11, appointment.getSymptoms());
            pstmt.setString(12, appointment.getDiagnosis());
            pstmt.setString(13, appointment.getPrescription());
            pstmt.setDouble(14, appointment.getConsultationFee());
            pstmt.setInt(15, appointment.isEmergency() ? 1 : 0);
            pstmt.setString(16, LocalDateTime.now().toString()); 
            pstmt.setInt(17, appointment.getAppointmentId());
            pstmt.executeUpdate();
            System.out.println("Appointment updated: " + appointment.getAppointmentId());
        }
    }
    
    public void updateAppointmentStatus(int appointmentId, String status) throws SQLException {
        String sql = "UPDATE appointments SET status = ?, last_modified = ? WHERE appointment_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, LocalDateTime.now().toString());
            pstmt.setInt(3, appointmentId);
            pstmt.executeUpdate();
            System.out.println("Appointment " + appointmentId + " status updated to " + status);
        }
    }

    public void deleteAllAppointments() throws SQLException {
        String sql = "DELETE FROM appointments;";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("All appointments deleted from database.");
        }
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(rs.getInt("appointment_id"));
        appointment.setPatientId(rs.getInt("patient_id"));
        appointment.setPatientName(rs.getString("patient_name"));
        appointment.setDoctorId(rs.getInt("doctor_id"));
        appointment.setDoctorName(rs.getString("doctor_name"));
        appointment.setDoctorSpecialization(rs.getString("doctor_specialization"));
        appointment.setAppointmentType(rs.getString("appointment_type"));
        appointment.setAppointmentDate(LocalDateTime.parse(rs.getString("appointment_datetime")));
        appointment.setDurationMinutes(rs.getInt("duration_minutes"));
        appointment.setStatus(rs.getString("status"));
        appointment.setNotes(rs.getString("notes"));
        appointment.setSymptoms(rs.getString("symptoms"));
        appointment.setDiagnosis(rs.getString("diagnosis"));
        appointment.setPrescription(rs.getString("prescription"));
        appointment.setConsultationFee(rs.getDouble("consultation_fee"));
        appointment.setEmergency(rs.getInt("is_emergency") == 1);
        appointment.setCreatedDate(LocalDateTime.parse(rs.getString("created_date")));
        appointment.setLastModified(LocalDateTime.parse(rs.getString("last_modified")));
        return appointment;
    }

    
    public void saveHealthRecord(HealthRecord record) throws SQLException {
        String sql = "INSERT INTO health_records (patient_id, record_date, heart_rate, systolic_bp, diastolic_bp, blood_sugar, cholesterol, temperature, symptoms, diagnosis, medication, smoking_status, exercise_frequency, stress_level, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, record.getPatientId());
            pstmt.setString(2, record.getRecordDate().toString());
            pstmt.setDouble(3, record.getHeartRate());
            pstmt.setDouble(4, record.getSystolicBP());
            pstmt.setDouble(5, record.getDiastolicBP());
            pstmt.setDouble(6, record.getBloodSugar());
            pstmt.setDouble(7, record.getCholesterol());
            pstmt.setDouble(8, record.getTemperature());
            pstmt.setString(9, record.getSymptoms());
            pstmt.setString(10, record.getDiagnosis());
            pstmt.setString(11, record.getMedication());
            pstmt.setString(12, record.getSmokingStatus());
            pstmt.setString(13, record.getExerciseFrequency());
            pstmt.setInt(14, record.getStressLevel());
            pstmt.setString(15, record.getNotes());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    record.setRecordId(generatedKeys.getInt(1));
                }
            }
            System.out.println("Health record saved for patient " + record.getPatientId());
        }
    }

    public List<HealthRecord> getHealthRecordsForPatient(int patientId) throws SQLException {
        List<HealthRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM health_records WHERE patient_id = ? ORDER BY record_date DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                records.add(mapResultSetToHealthRecord(rs));
            }
        }
        return records;
    }

    public HealthRecord getLatestHealthRecordForPatient(int patientId) throws SQLException {
        String sql = "SELECT * FROM health_records WHERE patient_id = ? ORDER BY record_date DESC LIMIT 1";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToHealthRecord(rs);
            }
        }
        return null;
    }

    private HealthRecord mapResultSetToHealthRecord(ResultSet rs) throws SQLException {
        HealthRecord record = new HealthRecord();
        record.setRecordId(rs.getInt("record_id"));
        record.setPatientId(rs.getInt("patient_id"));
        record.setRecordDate(LocalDate.parse(rs.getString("record_date")));
        record.setHeartRate(rs.getDouble("heart_rate"));
        record.setSystolicBP(rs.getDouble("systolic_bp"));
        record.setDiastolicBP(rs.getDouble("diastolic_bp"));
        record.setBloodSugar(rs.getDouble("blood_sugar"));
        record.setCholesterol(rs.getDouble("cholesterol"));
        record.setTemperature(rs.getDouble("temperature"));
        record.setSymptoms(rs.getString("symptoms"));
        record.setDiagnosis(rs.getString("diagnosis"));
        record.setMedication(rs.getString("medication"));
        record.setSmokingStatus(rs.getString("smoking_status"));
        record.setExerciseFrequency(rs.getString("exercise_frequency"));
        record.setStressLevel(rs.getInt("stress_level"));
        record.setNotes(rs.getString("notes"));
        return record;
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    public List<Appointment> getAppointmentsForPatient(int patientId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE patient_id = ? ORDER BY appointment_datetime DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting appointments for patient: " + e.getMessage());
        }
        return appointments;
    }
}