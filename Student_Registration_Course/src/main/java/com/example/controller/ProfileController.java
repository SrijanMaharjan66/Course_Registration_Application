package com.example.controller;

import com.example.Main;
import com.example.model.Student;
import com.example.util.DataManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> programComboBox;
    @FXML private ComboBox<Integer> semesterComboBox;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize ComboBoxes
        programComboBox.getItems().addAll("BBA", "BBS", "BCS", "Computer Science", "Business Administration");
        semesterComboBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8);
        
        // Load current student data
        Student student = DataManager.getCurrentStudent();
        nameField.setText(student.getName());
        emailField.setText(student.getEmail());
        programComboBox.setValue(student.getProgram());
        semesterComboBox.setValue(student.getSemester());
    }
    
    @FXML
    private void handleSave() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String program = programComboBox.getValue();
        Integer semester = semesterComboBox.getValue();
        
        if (name.isEmpty() || email.isEmpty() || program == null || semester == null) {
            showAlert("Validation Error", "Please fill in all fields.");
            return;
        }
        
        if (!isValidEmail(email)) {
            showAlert("Validation Error", "Please enter a valid email address.");
            return;
        }
        
        Student student = new Student(name, email, program, semester);
        DataManager.setCurrentStudent(student);
        
        showSuccessAlert("Profile Updated", "Your profile has been updated successfully!");
    }
    
    @FXML
    private void handleBackToDashboard() {
        try {
            Main.showDashboardScene();
        } catch (Exception e) {
            showAlert("Error", "Failed to load dashboard: " + e.getMessage());
        }
    }
    
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
