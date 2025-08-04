package com.example.controller;

import com.example.Main;
import com.example.model.Student;
import com.example.model.User;
import com.example.util.DataManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> programComboBox;
    @FXML private ComboBox<Integer> semesterComboBox;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize ComboBoxes
        programComboBox.getItems().addAll("BBA", "BBS", "BCS");
        semesterComboBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8);
        
        // Set default values
        semesterComboBox.setValue(1);
    }
    
    @FXML
    private void handleRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String program = programComboBox.getValue();
        Integer semester = semesterComboBox.getValue();
        
        // Validation
        if (name.isEmpty() || email.isEmpty() || username.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty() || 
            program == null || semester == null) {
            showAlert("Validation Error", "Please fill in all fields.");
            return;
        }
        
        if (!isValidEmail(email)) {
            showAlert("Validation Error", "Please enter a valid email address.");
            return;
        }
        
        if (password.length() < 6) {
            showAlert("Validation Error", "Password must be at least 6 characters long.");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showAlert("Validation Error", "Passwords do not match.");
            return;
        }
        
        if (DataManager.userExists(username)) {
            showAlert("Registration Error", "Username already exists. Please choose a different username.");
            return;
        }
        
        // Create new user
        Student student = new Student(name, email, program, semester);
        User user = new User(username, password, student);
        
        if (DataManager.registerUser(user)) {
            showSuccessAlert("Registration Successful", 
                "Account created successfully! You can now login with your credentials.");
            handleBackToLogin();
        } else {
            showAlert("Registration Error", "Failed to create account. Please try again.");
        }
    }
    
    @FXML
    private void handleBackToLogin() {
        try {
            Main.showLoginScene();
        } catch (Exception e) {
            showAlert("Error", "Failed to load login page: " + e.getMessage());
        }
    }
    
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && email.length() > 5;
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
