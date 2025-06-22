# 🏥 Smart Health Prediction & Appointment System

## Empowering Healthcare Management with Predictive Insights

[![Java](https://img.shields.io/badge/Java-11%2B-blue.svg?style=flat-square&logo=java)](https://www.java.com/)
[![SQLite](https://img.shields.io/badge/Database-SQLite-green.svg?style=flat-square&logo=sqlite)](https://www.sqlite.org/index.html)
[![Swing GUI](https://img.shields.io/badge/GUI-JavaSwing-orange.svg?style=flat-square&logo=swing)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square)](https://opensource.org/licenses/MIT)

---

## Table of Contents

* [About The Project](#about-the-project)
* [Features](#features)
* [Technologies Used](#technologies-used)
* [Getting Started](#getting-started)
    * [Prerequisites](#prerequisites)
    * [Installation](#installation)
    * [Running the Application](#running-the-application)
* [Project Structure](#project-structure)
* [Machine Learning Components (Simplified)](#machine-learning-components-simplified)
* [Future Enhancements](#future-enhancements)
* [Contributing](#contributing)
* [License](#license)
* [Contact](#contact)

---

## About The Project

The **Smart Health Prediction & Appointment System** is a robust desktop application built with Java Swing, designed to streamline patient management, appointment scheduling, and basic health analytics. It integrates rudimentary machine learning concepts to provide health risk assessments, predict potential diseases, and offer personalized recommendations based on recorded health metrics.

This system serves as a foundational example of how a healthcare management application can incorporate data-driven insights to assist both patients and healthcare professionals. While the machine learning models are simplified for demonstration purposes, they illustrate the potential for predictive analytics in health informatics.

## Features

* **Comprehensive Patient Management:**
    * **Registration:** Easily register new patients with detailed demographic and medical history (existing conditions, allergies, medications).
    * **CRUD Operations:** Full functionality to Create, Read, Update, and Delete patient records.
    * **Search & View:** Efficiently search for patients and view their detailed profiles, including their historical health records and appointments.

* **Efficient Appointment Scheduling:**
    * **Flexible Booking:** Schedule appointments with specific patients and doctors, defining appointment type, date, and time.
    * **Status Management:** Update appointment statuses (Scheduled, Completed, Cancelled) for accurate tracking.
    * **Overview:** View all scheduled appointments in a clear, organized table.

* **Detailed Health Record Management:**
    * **Metric Input:** Record vital health metrics such as heart rate, blood pressure, blood sugar, cholesterol, temperature, symptoms, and stress levels.
    * **Historical Data:** Maintain a chronological history of a patient's health records.

* **Integrated Health Prediction & Recommendations (ML Components):**
    * **Health Risk Prediction:** Based on patient data and latest health records, the system provides an overall health risk assessment (Low, Moderate, High) and a calculated health score.
    * **Disease Prediction:** Offers simplified predictions of potential diseases (e.g., Hypertension, Diabetes, Respiratory Infections) based on input parameters.
    * **Personalized Recommendations:** Generates actionable health advice tailored to the patient's risk profile and predicted conditions.

* **Robust Data Persistence:**
    * Utilizes **SQLite** as an embedded, file-based relational database (`health_prediction.db`).
    * Automatically initializes the database and creates necessary tables upon first run.
    * Includes basic sample data for quick testing.

* **Intuitive User Interface:**
    * A clean, multi-tabbed Java Swing GUI for easy navigation between Patient Management, Health Prediction, and Appointment Management sections.
    * Interactive tables for viewing and managing data.

## Technologies Used

* **Core Language:** Java (JDK 11+)
* **Database:** SQLite
* **Database Connectivity:** JDBC (Java Database Connectivity)
* **User Interface:** Java Swing
* **Date/Time Handling:** Java 8 Date and Time API (`java.time.*`)
* **Data Processing:** Java Stream API

## Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

* **Java Development Kit (JDK) 11 or higher:**
    * Download from [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.java.net/install/index.html).
    * Ensure `java` and `javac` commands are accessible in your system's PATH.

* **SQLite JDBC Driver:**
    * This project requires the SQLite JDBC driver to interact with the database.
    * Download the `sqlite-jdbc-<version>.jar` file from its official GitHub releases page:
        [https://github.com/xerial/sqlite-jdbc/releases](https://github.com/xerial/sqlite-jdbc/releases)
    * Choose the latest stable release.

### Installation

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/your_username/HealthPredictionSystem.git](https://github.com/your_username/HealthPredictionSystem.git)
    cd HealthPredictionSystem
    ```
    (Replace `your_username/HealthPredictionSystem.git` with the actual repository URL)

2.  **Create `lib` directory and add JDBC driver:**
    * Inside your `HealthPredictionSystem` project root, create a new folder named `lib`.
    * Place the downloaded `sqlite-jdbc-<version>.jar` file into this `lib` folder.
        Your project structure should look like:
        ```
        HealthPredictionSystem/
        ├── src/
        └── lib/
            └── sqlite-jdbc-3.44.1.0.jar  (or similar version)
        ```

3.  **Compile the Java files:**
    Open your terminal or command prompt in the `HealthPredictionSystem` directory and run the compilation command.

    * **For Linux / macOS:**
        ```bash
        mkdir -p bin
        javac -d bin -cp "lib/*" src/com/healthprediction/model/*.java \
        src/com/healthprediction/service/*.java \
        src/com/healthprediction/ui/*.java \
        src/com/healthprediction/ml/*.java \
        src/com/healthprediction/*.java
        ```
    * **For Windows (use `^` for line continuation):**
        ```cmd
        mkdir bin
        javac -d bin -cp "lib\*" src\com\healthprediction\model\*.java ^
        src\com\healthprediction\service\*.java ^
        src\com\healthprediction\ui\*.java ^
        src\com\healthprediction\ml\*.java ^
        src\com\healthprediction\*.java
        ```
    * If compilation is successful, a `bin` directory will be created containing the `.class` files.

### Running the Application

After successful compilation, you can run the application from the command line.

* **For Linux / macOS:**
    ```bash
    java -cp "bin:lib/*" com.healthprediction.SmartHealthApp
    ```
* **For Windows:**
    ```cmd
    java -cp "bin;lib\*" com.healthprediction.SmartHealthApp
    ```

A Java Swing GUI window titled "Smart Health Prediction & Appointment System" should appear. The `health_prediction.db` file will be created in your project root on first run.

## Project Structure
