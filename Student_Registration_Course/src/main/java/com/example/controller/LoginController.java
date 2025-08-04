package com.example.controller;

import com.example.Main;
import com.example.util.DataManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login Failed", "Please enter both username and password.");
            return;
        }
        
        if (DataManager.authenticateUser(username, password)) {
            try {
                Main.showDashboardScene();
            } catch (Exception e) {
                showAlert("Error", "Failed to load dashboard: " + e.getMessage());
            }
        } else {
            showAlert("Login Failed", "Invalid username or password!");
        }
    }
    
    @FXML
    private void handleRegister() {
        try {
            Main.showRegisterScene();
        } catch (Exception e) {
            showAlert("Error", "Failed to load registration page: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleExit() {
        System.exit(0);
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
