package com.healthprediction.ui;

import com.healthprediction.model.Patient;
import com.healthprediction.model.Appointment;
import com.healthprediction.model.Doctor; 
import com.healthprediction.model.HealthRecord; 
import com.healthprediction.service.DatabaseService;
import com.healthprediction.service.MLModelService;
import com.healthprediction.service.AppointmentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter; 
import java.awt.event.MouseEvent; 
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException; 
import java.util.List;
import java.util.Map;
import java.util.Vector; 

public class HealthPredictionGUI extends JFrame {
    private DatabaseService dbService;
    private MLModelService mlService;
    private AppointmentService aptService;

    
    private JTabbedPane tabbedPane;

    
    private JTextField txtPatientFirstName, txtPatientLastName, txtPatientEmail, txtPatientPhone, txtPatientAddress;
    private JComboBox<String> cmbPatientGender;
    private JFormattedTextField txtPatientDOB; 
    private JTextField txtExistingConditions, txtAllergies, txtMedications; 
    private JButton btnRegisterPatient, btnUpdatePatient, btnDeletePatient, btnSearchPatient, btnViewPatientDetails;
    private JTable patientTable;
    private DefaultTableModel patientTableModel;
    private JTextField txtPatientSearch;

    
    private JComboBox<String> cmbPredictionPatient; 
    private JTextField txtHeartRate, txtSystolicBP, txtDiastolicBP, txtBloodSugar, txtCholesterol, txtTemperature;
    private JTextField txtSymptoms, txtDiagnosis, txtMedicationTaken, txtSmokingStatus, txtExerciseFrequency, txtStressLevel;
    private JButton btnSubmitHealthRecord, btnPredictHealth;
    private JTextArea taPredictionResults;
    private JTable healthRecordTable;
    private DefaultTableModel healthRecordTableModel;

    
    private JComboBox<String> cmbAppointmentPatient, cmbAppointmentDoctor, cmbAppointmentType;
    private JFormattedTextField txtAppointmentDate, txtAppointmentTime; 
    private JButton btnScheduleAppointment, btnRefreshAppointments, btnCompleteAppointment, btnCancelAppointment;
    private JTable appointmentTable;
    private DefaultTableModel appointmentTableModel;

    public HealthPredictionGUI() {
        dbService = DatabaseService.getInstance();
        mlService = MLModelService.getInstance();
        aptService = new AppointmentService();

        setTitle("Smart Health Prediction & Appointment System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        add(tabbedPane);

        createPatientManagementTab();
        createHealthPredictionTab();
        createAppointmentManagementTab();

        loadPatientsIntoTable();
        loadDoctorsIntoCombobox();
        loadAppointmentsIntoTable();
        loadPatientsIntoPredictionCombobox();
        loadPatientsIntoAppointmentCombobox(); // <-- ADD THIS LINE
    }

    private void createPatientManagementTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        
        JPanel formPanel = new JPanel(new GridLayout(0, 4, 10, 5)); 
        formPanel.setBorder(BorderFactory.createTitledBorder("Patient Information"));

        txtPatientFirstName = new JTextField(15);
        txtPatientLastName = new JTextField(15);
        txtPatientEmail = new JTextField(15);
        txtPatientPhone = new JTextField(15);
        txtPatientAddress = new JTextField(15);
        txtPatientDOB = new JFormattedTextField(DateTimeFormatter.ofPattern("yyyy-MM-dd").toFormat());
        txtPatientDOB.setColumns(15);
        txtExistingConditions = new JTextField(15);
        txtAllergies = new JTextField(15);
        txtMedications = new JTextField(15);

        cmbPatientGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});

        formPanel.add(new JLabel("First Name:"));
        formPanel.add(txtPatientFirstName);
        formPanel.add(new JLabel("Last Name:"));
        formPanel.add(txtPatientLastName);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtPatientEmail);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(txtPatientPhone);
        formPanel.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        formPanel.add(txtPatientDOB);
        formPanel.add(new JLabel("Gender:"));
        formPanel.add(cmbPatientGender);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(txtPatientAddress);
        formPanel.add(new JLabel("Existing Conditions:")); 
        formPanel.add(txtExistingConditions);
        formPanel.add(new JLabel("Allergies:")); 
        formPanel.add(txtAllergies);
        formPanel.add(new JLabel("Medications:")); 
        formPanel.add(txtMedications);

        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnRegisterPatient = new JButton("Register Patient");
        btnUpdatePatient = new JButton("Update Patient");
        btnDeletePatient = new JButton("Delete Patient");
        btnSearchPatient = new JButton("Search Patients");
        txtPatientSearch = new JTextField(20);
        btnViewPatientDetails = new JButton("View Details");

        buttonPanel.add(btnRegisterPatient);
        buttonPanel.add(btnUpdatePatient);
        buttonPanel.add(btnDeletePatient);
        buttonPanel.add(txtPatientSearch);
        buttonPanel.add(btnSearchPatient);
        buttonPanel.add(btnViewPatientDetails);

        
        patientTableModel = new DefaultTableModel(new String[]{"ID", "First Name", "Last Name", "Email", "Phone", "Gender", "DOB"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        patientTable = new JTable(patientTableModel);
        JScrollPane scrollPane = new JScrollPane(patientTable);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Patient Management", panel);

        
        btnRegisterPatient.addActionListener(e -> registerPatient());
        btnUpdatePatient.addActionListener(e -> updatePatient());
        btnDeletePatient.addActionListener(e -> deletePatient());
        btnSearchPatient.addActionListener(e -> searchPatients(txtPatientSearch.getText()));
        btnViewPatientDetails.addActionListener(e -> viewPatientDetails());

        patientTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    fillPatientFormFromTable();
                }
            }
        });
    }

    private void loadPatientsIntoTable() {
        patientTableModel.setRowCount(0); 
        List<Patient> patients = dbService.getAllPatients();
        for (Patient p : patients) {
            patientTableModel.addRow(new Object[]{
                p.getPatientId(),
                p.getFirstName(),
                p.getLastName(),
                p.getEmail(),
                p.getPhone(),
                p.getGender(),
                p.getDateOfBirth()
            });
        }
    }

    private void registerPatient() {
        try {
            String firstName = txtPatientFirstName.getText();
            String lastName = txtPatientLastName.getText();
            String email = txtPatientEmail.getText();
            String phone = txtPatientPhone.getText();
            LocalDate dob = LocalDate.parse(txtPatientDOB.getText());
            String gender = (String) cmbPatientGender.getSelectedItem();
            String address = txtPatientAddress.getText();
            String existingConditions = txtExistingConditions.getText();
            String allergies = txtAllergies.getText();
            String medications = txtMedications.getText();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || dob == null) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields (First Name, Last Name, Email, DOB).", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Patient newPatient = new Patient(firstName, lastName, email, phone, dob, gender, address);
            newPatient.setExistingConditions(existingConditions);
            newPatient.setAllergies(allergies);
            newPatient.setMedications(medications);

            dbService.savePatient(newPatient);
            JOptionPane.showMessageDialog(this, "Patient registered successfully!");
            clearPatientForm();
            loadPatientsIntoTable(); 
            loadPatientsIntoPredictionCombobox(); 
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid Date of Birth format. Please use YYYY-MM-DD.", "Date Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error registering patient: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void fillPatientFormFromTable() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow >= 0) {
            int patientId = (Integer) patientTableModel.getValueAt(selectedRow, 0);
            try {
                Patient patient = dbService.getPatientById(patientId);
                if (patient != null) {
                    txtPatientFirstName.setText(patient.getFirstName());
                    txtPatientLastName.setText(patient.getLastName());
                    txtPatientEmail.setText(patient.getEmail());
                    txtPatientPhone.setText(patient.getPhone());
                    txtPatientDOB.setText(patient.getDateOfBirth().toString());
                    cmbPatientGender.setSelectedItem(patient.getGender());
                    txtPatientAddress.setText(patient.getAddress());
                    txtExistingConditions.setText(patient.getExistingConditions());
                    txtAllergies.setText(patient.getAllergies());
                    txtMedications.setText(patient.getMedications());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading patient details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void updatePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a patient to update.", "No Patient Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int patientId = (Integer) patientTableModel.getValueAt(selectedRow, 0);
            Patient patientToUpdate = dbService.getPatientById(patientId);

            if (patientToUpdate == null) {
                JOptionPane.showMessageDialog(this, "Selected patient not found in database.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            
            patientToUpdate.setFirstName(txtPatientFirstName.getText());
            patientToUpdate.setLastName(txtPatientLastName.getText());
            patientToUpdate.setEmail(txtPatientEmail.getText());
            patientToUpdate.setPhone(txtPatientPhone.getText());
            patientToUpdate.setDateOfBirth(LocalDate.parse(txtPatientDOB.getText()));
            patientToUpdate.setGender((String) cmbPatientGender.getSelectedItem());
            patientToUpdate.setAddress(txtPatientAddress.getText());
            patientToUpdate.setExistingConditions(txtExistingConditions.getText());
            patientToUpdate.setAllergies(txtAllergies.getText());
            patientToUpdate.setMedications(txtMedications.getText());

            dbService.updatePatient(patientToUpdate);
            JOptionPane.showMessageDialog(this, "Patient updated successfully!");
            clearPatientForm();
            loadPatientsIntoTable(); 
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid Date of Birth format. Please use YYYY-MM-DD.", "Date Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating patient: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete.", "No Patient Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this patient and all associated data (health records, appointments)?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int patientId = (Integer) patientTableModel.getValueAt(selectedRow, 0);
                dbService.deletePatient(patientId); 
                JOptionPane.showMessageDialog(this, "Patient deleted successfully!");
                clearPatientForm();
                loadPatientsIntoTable(); 
                loadPatientsIntoPredictionCombobox(); 
                loadAppointmentsIntoTable(); 
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting patient: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void searchPatients(String searchTerm) {
        List<Patient> patients = dbService.searchPatients(searchTerm);
        patientTableModel.setRowCount(0); 
        for (Patient p : patients) {
            patientTableModel.addRow(new Object[]{
                p.getPatientId(),
                p.getFirstName(),
                p.getLastName(),
                p.getEmail(),
                p.getPhone(),
                p.getGender(),
                p.getDateOfBirth()
            });
        }
        if (patients.isEmpty() && !searchTerm.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No patients found for '" + searchTerm + "'.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void viewPatientDetails() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a patient to view details.", "No Patient Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int patientId = (Integer) patientTableModel.getValueAt(selectedRow, 0);
            Patient patient = dbService.getPatientById(patientId);
            if (patient != null) {
                
                List<HealthRecord> healthRecords = dbService.getHealthRecordsForPatient(patientId);
                List<Appointment> appointments = dbService.getAppointmentsForPatient(patientId);

                StringBuilder details = new StringBuilder();
                details.append("Patient ID: ").append(patient.getPatientId()).append("\n");
                details.append("Name: ").append(patient.getFirstName()).append(" ").append(patient.getLastName()).append("\n");
                details.append("Email: ").append(patient.getEmail()).append("\n");
                details.append("Phone: ").append(patient.getPhone()).append("\n");
                details.append("DOB: ").append(patient.getDateOfBirth()).append(" (Age: ").append(patient.getAge()).append(")\n");
                details.append("Gender: ").append(patient.getGender()).append("\n");
                details.append("Address: ").append(patient.getAddress()).append("\n");
                details.append("Existing Conditions: ").append(patient.getExistingConditions()).append("\n");
                details.append("Allergies: ").append(patient.getAllergies()).append("\n");
                details.append("Medications: ").append(patient.getMedications()).append("\n\n");

                details.append("--- Health Records ---\n");
                if (healthRecords.isEmpty()) {
                    details.append("No health records found.\n");
                } else {
                    for (HealthRecord hr : healthRecords) {
                        details.append("  Record Date: ").append(hr.getRecordDate())
                               .append(", HR: ").append(hr.getHeartRate())
                               .append(", BP: ").append(hr.getSystolicBP()).append("/").append(hr.getDiastolicBP())
                               .append(", Sugar: ").append(hr.getBloodSugar())
                               .append(", Temp: ").append(hr.getTemperature())
                               .append(", Symptoms: ").append(hr.getSymptoms()).append("\n");
                    }
                }
                details.append("\n");

                details.append("--- Appointments ---\n");
                if (appointments.isEmpty()) {
                    details.append("No appointments found.\n");
                } else {
                    for (Appointment apt : appointments) {
                        details.append("  ID: ").append(apt.getAppointmentId())
                               .append(", Doctor: ").append(apt.getDoctorName())
                               .append(", Type: ").append(apt.getAppointmentType())
                               .append(", Date: ").append(apt.getFormattedDate())
                               .append(", Status: ").append(apt.getStatus()).append("\n");
                    }
                }

                JTextArea detailArea = new JTextArea(details.toString());
                detailArea.setEditable(false);
                JScrollPane detailScrollPane = new JScrollPane(detailArea);
                detailScrollPane.setPreferredSize(new Dimension(500, 400));
                JOptionPane.showMessageDialog(this, detailScrollPane, "Patient Details: " + patient.getFirstName() + " " + patient.getLastName(), JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error viewing patient details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }


    private void clearPatientForm() {
        txtPatientFirstName.setText("");
        txtPatientLastName.setText("");
        txtPatientEmail.setText("");
        txtPatientPhone.setText("");
        txtPatientDOB.setText("");
        cmbPatientGender.setSelectedIndex(0);
        txtPatientAddress.setText("");
        txtExistingConditions.setText("");
        txtAllergies.setText("");
        txtMedications.setText("");
        patientTable.clearSelection();
    }

    private void createHealthPredictionTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        
        JPanel inputPanel = new JPanel(new GridLayout(0, 4, 10, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Health Record & Prediction Input"));

        cmbPredictionPatient = new JComboBox<>();
        txtHeartRate = new JTextField(10);
        txtSystolicBP = new JTextField(10);
        txtDiastolicBP = new JTextField(10);
        txtBloodSugar = new JTextField(10);
        txtCholesterol = new JTextField(10);
        txtTemperature = new JTextField(10);
        txtSymptoms = new JTextField(20);
        txtDiagnosis = new JTextField(20);
        txtMedicationTaken = new JTextField(20);
        txtSmokingStatus = new JTextField(10);
        txtExerciseFrequency = new JTextField(10);
        txtStressLevel = new JTextField(5);

        inputPanel.add(new JLabel("Select Patient:"));
        inputPanel.add(cmbPredictionPatient);
        inputPanel.add(new JLabel("Heart Rate (bpm):"));
        inputPanel.add(txtHeartRate);
        inputPanel.add(new JLabel("Systolic BP (mmHg):"));
        inputPanel.add(txtSystolicBP);
        inputPanel.add(new JLabel("Diastolic BP (mmHg):"));
        inputPanel.add(txtDiastolicBP);
        inputPanel.add(new JLabel("Blood Sugar (mg/dL):"));
        inputPanel.add(txtBloodSugar);
        inputPanel.add(new JLabel("Cholesterol (mg/dL):"));
        inputPanel.add(txtCholesterol);
        inputPanel.add(new JLabel("Temperature (°C):"));
        inputPanel.add(txtTemperature);
        inputPanel.add(new JLabel("Symptoms:"));
        inputPanel.add(txtSymptoms);
        inputPanel.add(new JLabel("Diagnosis:"));
        inputPanel.add(txtDiagnosis);
        inputPanel.add(new JLabel("Medication Taken:"));
        inputPanel.add(txtMedicationTaken);
        inputPanel.add(new JLabel("Smoking Status:"));
        inputPanel.add(txtSmokingStatus);
        inputPanel.add(new JLabel("Exercise Frequency:"));
        inputPanel.add(txtExerciseFrequency);
        inputPanel.add(new JLabel("Stress Level (1-10):"));
        inputPanel.add(txtStressLevel);

        
        JPanel healthButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnSubmitHealthRecord = new JButton("Submit Health Record");
        btnPredictHealth = new JButton("Predict Health & Recommendations");
        healthButtonPanel.add(btnSubmitHealthRecord);
        healthButtonPanel.add(btnPredictHealth);

        
        taPredictionResults = new JTextArea(10, 40);
        taPredictionResults.setEditable(false);
        taPredictionResults.setLineWrap(true);
        taPredictionResults.setWrapStyleWord(true);
        JScrollPane predictionScrollPane = new JScrollPane(taPredictionResults);
        predictionScrollPane.setBorder(BorderFactory.createTitledBorder("Prediction Results"));

        
        healthRecordTableModel = new DefaultTableModel(new String[]{"ID", "Date", "HR", "SBP", "DBP", "Sugar", "Cholesterol", "Temp", "Symptoms"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        healthRecordTable = new JTable(healthRecordTableModel);
        JScrollPane healthRecordScrollPane = new JScrollPane(healthRecordTable);
        healthRecordScrollPane.setBorder(BorderFactory.createTitledBorder("Patient's Health History"));


        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, predictionScrollPane, healthRecordScrollPane);
        splitPane.setResizeWeight(0.5); 

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(healthButtonPanel, BorderLayout.CENTER);
        panel.add(splitPane, BorderLayout.SOUTH);

        tabbedPane.addTab("Health Prediction", panel);

        
        btnSubmitHealthRecord.addActionListener(e -> submitHealthRecord());
        btnPredictHealth.addActionListener(e -> predictHealthAndRecommendations());
        cmbPredictionPatient.addActionListener(e -> loadHealthRecordsForSelectedPatient());
    }

    private void loadPatientsIntoPredictionCombobox() {
        cmbPredictionPatient.removeAllItems();
        cmbPredictionPatient.addItem("Select Patient"); 
        List<Patient> patients = dbService.getAllPatients();
        for (Patient p : patients) {
            
            cmbPredictionPatient.addItem(p.getPatientId() + " - " + p.getFirstName() + " " + p.getLastName());
        }
    }

    private void loadHealthRecordsForSelectedPatient() {
        healthRecordTableModel.setRowCount(0); 
        String selectedItem = (String) cmbPredictionPatient.getSelectedItem();
        if (selectedItem != null && !selectedItem.equals("Select Patient")) {
            try {
                int patientId = Integer.parseInt(selectedItem.split(" - ")[0]);
                List<HealthRecord> records = dbService.getHealthRecordsForPatient(patientId);
                for (HealthRecord hr : records) {
                    healthRecordTableModel.addRow(new Object[]{
                        hr.getRecordId(),
                        hr.getRecordDate(),
                        hr.getHeartRate(),
                        hr.getSystolicBP(),
                        hr.getDiastolicBP(),
                        hr.getBloodSugar(),
                        hr.getCholesterol(),
                        hr.getTemperature(),
                        hr.getSymptoms()
                    });
                }
            } catch (Exception e) {
                System.err.println("Error loading health records: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void submitHealthRecord() {
        String selectedPatientItem = (String) cmbPredictionPatient.getSelectedItem();
        if (selectedPatientItem == null || selectedPatientItem.equals("Select Patient")) {
            JOptionPane.showMessageDialog(this, "Please select a patient first.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int patientId = Integer.parseInt(selectedPatientItem.split(" - ")[0]);
            
            HealthRecord newRecord = new HealthRecord();
            newRecord.setPatientId(patientId);
            newRecord.setRecordDate(LocalDate.now()); 

            
            newRecord.setHeartRate(Double.parseDouble(txtHeartRate.getText()));
            newRecord.setSystolicBP(Double.parseDouble(txtSystolicBP.getText()));
            newRecord.setDiastolicBP(Double.parseDouble(txtDiastolicBP.getText()));
            newRecord.setBloodSugar(Double.parseDouble(txtBloodSugar.getText()));
            newRecord.setCholesterol(Double.parseDouble(txtCholesterol.getText()));
            newRecord.setTemperature(Double.parseDouble(txtTemperature.getText()));

            
            newRecord.setSymptoms(txtSymptoms.getText());
            newRecord.setDiagnosis(txtDiagnosis.getText());
            newRecord.setMedication(txtMedicationTaken.getText());
            newRecord.setSmokingStatus(txtSmokingStatus.getText());
            newRecord.setExerciseFrequency(txtExerciseFrequency.getText());
            newRecord.setStressLevel(Integer.parseInt(txtStressLevel.getText()));

            dbService.saveHealthRecord(newRecord);
            JOptionPane.showMessageDialog(this, "Health record submitted successfully!");
            clearHealthRecordForm();
            loadHealthRecordsForSelectedPatient(); 
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for numerical fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error submitting health record: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void predictHealthAndRecommendations() {
        String selectedPatientItem = (String) cmbPredictionPatient.getSelectedItem();
        if (selectedPatientItem == null || selectedPatientItem.equals("Select Patient")) {
            JOptionPane.showMessageDialog(this, "Please select a patient first.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int patientId = Integer.parseInt(selectedPatientItem.split(" - ")[0]);
            Map<String, Object> results = mlService.getPredictionAndRecommendations(patientId);

            taPredictionResults.setText(""); 

            if (results.containsKey("error")) {
                taPredictionResults.append("Prediction Error: " + results.get("error") + "\n");
            } else {
                taPredictionResults.append("--- Health Prediction ---\n");
                taPredictionResults.append("Health Risk: " + results.get("healthRisk") + "\n");
                taPredictionResults.append("Health Score: " + results.get("healthScore") + "/100\n\n");

                taPredictionResults.append("--- Predicted Diseases ---\n");
                List<String> predictedDiseases = (List<String>) results.get("predictedDiseases");
                if (predictedDiseases != null && !predictedDiseases.isEmpty()) {
                    for (String disease : predictedDiseases) {
                        taPredictionResults.append("- " + disease + "\n");
                    }
                } else {
                    taPredictionResults.append("No specific disease predicted based on current data.\n");
                }
                taPredictionResults.append("\n");

                taPredictionResults.append("--- Recommendations ---\n");
                List<String> recommendations = (List<String>) results.get("recommendations");
                if (recommendations != null && !recommendations.isEmpty()) {
                    for (String rec : recommendations) {
                        taPredictionResults.append("- " + rec + "\n");
                    }
                } else {
                    taPredictionResults.append("No specific recommendations generated.\n");
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error parsing patient ID for prediction.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error during health prediction: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void clearHealthRecordForm() {
        
        txtHeartRate.setText("");
        txtSystolicBP.setText("");
        txtDiastolicBP.setText("");
        txtBloodSugar.setText("");
        txtCholesterol.setText("");
        txtTemperature.setText("");
        txtSymptoms.setText("");
        txtDiagnosis.setText("");
        txtMedicationTaken.setText("");
        txtSmokingStatus.setText("");
        txtExerciseFrequency.setText("");
        txtStressLevel.setText("");
        taPredictionResults.setText("");
    }

    private void createAppointmentManagementTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Appointment Scheduling"));

        cmbAppointmentPatient = new JComboBox<>();
        cmbAppointmentDoctor = new JComboBox<>();
        cmbAppointmentType = new JComboBox<>(new String[]{"Consultation", "Follow-up", "Check-up", "Emergency"});
        txtAppointmentDate = new JFormattedTextField(DateTimeFormatter.ofPattern("yyyy-MM-dd").toFormat());
        txtAppointmentDate.setColumns(15);
        txtAppointmentTime = new JFormattedTextField(DateTimeFormatter.ofPattern("HH:mm").toFormat());
        txtAppointmentTime.setColumns(10);

        formPanel.add(new JLabel("Select Patient:"));
        formPanel.add(cmbAppointmentPatient);
        formPanel.add(new JLabel("Select Doctor:"));
        formPanel.add(cmbAppointmentDoctor);
        formPanel.add(new JLabel("Appointment Type:"));
        formPanel.add(cmbAppointmentType);
        formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        formPanel.add(txtAppointmentDate);
        formPanel.add(new JLabel("Time (HH:MM):"));
        formPanel.add(txtAppointmentTime);

        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnScheduleAppointment = new JButton("Schedule Appointment");
        btnRefreshAppointments = new JButton("Refresh Appointments");
        btnCompleteAppointment = new JButton("Complete Selected");
        btnCancelAppointment = new JButton("Cancel Selected");

        buttonPanel.add(btnScheduleAppointment);
        buttonPanel.add(btnRefreshAppointments);
        buttonPanel.add(btnCompleteAppointment);
        buttonPanel.add(btnCancelAppointment);

        
        appointmentTableModel = new DefaultTableModel(new String[]{"ID", "Patient", "Doctor", "Type", "Date & Time", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        appointmentTable = new JTable(appointmentTableModel);
        JScrollPane scrollPane = new JScrollPane(appointmentTable);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Appointment Management", panel);

        
        btnScheduleAppointment.addActionListener(e -> scheduleAppointment());
        btnRefreshAppointments.addActionListener(e -> loadAppointmentsIntoTable());
        btnCompleteAppointment.addActionListener(e -> completeAppointment());
        btnCancelAppointment.addActionListener(e -> cancelAppointment());
    }

    private void loadAppointmentsIntoTable() {
        appointmentTableModel.setRowCount(0); 
        List<Appointment> appointments = aptService.getAllAppointments();
        for (Appointment apt : appointments) {
            appointmentTableModel.addRow(new Object[]{
                apt.getAppointmentId(),
                apt.getPatientName(),
                apt.getDoctorName(),
                apt.getAppointmentType(),
                apt.getFormattedDate(),
                apt.getStatus()
            });
        }
    }

    private void loadDoctorsIntoCombobox() {
        cmbAppointmentDoctor.removeAllItems();
        cmbAppointmentDoctor.addItem("Select Doctor");
        List<Doctor> doctors = dbService.getAllDoctors();
        for (Doctor d : doctors) {
            cmbAppointmentDoctor.addItem(d.getDoctorId() + " - " + d.getFirstName() + " " + d.getLastName() + " (" + d.getSpecialization() + ")");
        }
    }

    private void loadPatientsIntoAppointmentCombobox() {
        cmbAppointmentPatient.removeAllItems();
        cmbAppointmentPatient.addItem("Select Patient");
        List<Patient> patients = dbService.getAllPatients();
        for (Patient p : patients) {
            cmbAppointmentPatient.addItem(p.getPatientId() + " - " + p.getFirstName() + " " + p.getLastName());
        }
    }

    private void scheduleAppointment() {
        String selectedPatientItem = (String) cmbAppointmentPatient.getSelectedItem();
        String selectedDoctorItem = (String) cmbAppointmentDoctor.getSelectedItem();
        String appointmentType = (String) cmbAppointmentType.getSelectedItem();
        String dateStr = txtAppointmentDate.getText();
        String timeStr = txtAppointmentTime.getText();

        if (selectedPatientItem == null || selectedPatientItem.equals("Select Patient") ||
            selectedDoctorItem == null || selectedDoctorItem.equals("Select Doctor") ||
            dateStr.isEmpty() || timeStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all appointment details.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int patientId = Integer.parseInt(selectedPatientItem.split(" - ")[0]);
            String patientName = selectedPatientItem.split(" - ")[1];

            int doctorId = Integer.parseInt(selectedDoctorItem.split(" - ")[0]);
            String doctorName = selectedDoctorItem.split(" - ")[1].split(" \\(")[0]; 
            String doctorSpecialization = selectedDoctorItem.substring(selectedDoctorItem.indexOf('(') + 1, selectedDoctorItem.length() - 1);


            LocalDateTime appointmentDateTime = LocalDateTime.parse(dateStr + " " + timeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            Appointment newAppointment = new Appointment(patientId, patientName, doctorName, appointmentType, appointmentDateTime);
            newAppointment.setDoctorId(doctorId);
            newAppointment.setDoctorSpecialization(doctorSpecialization);
            newAppointment.setConsultationFee(100.0); 

            aptService.addAppointment(newAppointment);
            JOptionPane.showMessageDialog(this, "Appointment scheduled successfully!");
            clearAppointmentForm();
            loadAppointmentsIntoTable(); 
            loadPatientsIntoAppointmentCombobox(); // Refresh patient list if needed
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date/time format. Use YYYY-MM-DD for date and HH:MM for time.", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error parsing patient/doctor ID.", "Data Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error scheduling appointment: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void clearAppointmentForm() {
        cmbAppointmentPatient.setSelectedIndex(0);
        cmbAppointmentDoctor.setSelectedIndex(0);
        cmbAppointmentType.setSelectedIndex(0);
        txtAppointmentDate.setText("");
        txtAppointmentTime.setText("");
    }

    private void completeAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow >= 0) {
            int appointmentId = (Integer) appointmentTableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Mark this appointment as Completed?",
                "Confirm Completion",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                aptService.completeAppointment(appointmentId);
                JOptionPane.showMessageDialog(this, "Appointment marked as completed!");
                loadAppointmentsIntoTable(); 
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an appointment to complete!", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void cancelAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow >= 0) {
            int appointmentId = (Integer) appointmentTableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to cancel this appointment?",
                "Confirm Cancellation", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                aptService.cancelAppointment(appointmentId);
                JOptionPane.showMessageDialog(this, "Appointment cancelled successfully!");
                loadAppointmentsIntoTable(); 
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an appointment to cancel!", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
}