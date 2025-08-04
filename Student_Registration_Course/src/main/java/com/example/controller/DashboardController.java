package com.example.controller;

import com.example.Main;
import com.example.util.DataManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    
    @FXML private Label welcomeLabel;
    @FXML private Label registeredCoursesCountLabel;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String studentName = DataManager.getCurrentStudent().getName();
        welcomeLabel.setText("Welcome, " + studentName + "!");
        
        int courseCount = DataManager.getRegisteredCourses().size();
        registeredCoursesCountLabel.setText("Registered Courses: " + courseCount);
    }
    
    @FXML
    private void handleProfile(MouseEvent event) {
        // Add hover effect
        VBox card = (VBox) event.getSource();
        addHoverEffect(card);
        
        try {
            Main.showProfileScene();
        } catch (Exception e) {
            showAlert("Error", "Failed to load profile: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleRegisterCourse(MouseEvent event) {
        // Add hover effect
        VBox card = (VBox) event.getSource();
        addHoverEffect(card);
        
        try {
            Main.showRegisterCourseScene();
        } catch (Exception e) {
            showAlert("Error", "Failed to load course registration: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleViewCourses(MouseEvent event) {
        // Add hover effect
        VBox card = (VBox) event.getSource();
        addHoverEffect(card);
        
        try {
            Main.showViewCoursesScene();
        } catch (Exception e) {
            showAlert("Error", "Failed to load courses view: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleViewCharts(MouseEvent event) {
        // Add hover effect
        VBox card = (VBox) event.getSource();
        addHoverEffect(card);
        
        try {
            Main.showChartsScene();
        } catch (Exception e) {
            showAlert("Error", "Failed to load charts: " + e.getMessage());
        }
    }
    
    private void addHoverEffect(VBox card) {
        // Add a subtle scale effect
        card.setScaleX(0.98);
        card.setScaleY(0.98);
        
        // Reset after a short delay
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(
                javafx.util.Duration.millis(150),
                e -> {
                    card.setScaleX(1.0);
                    card.setScaleY(1.0);
                }
            )
        );
        timeline.play();
    }
    
    @FXML
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("You will be redirected to the login page.");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                DataManager.logout();
                Main.showLoginScene();
            } catch (Exception e) {
                showAlert("Error", "Failed to logout: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Application");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("The application will be closed.");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
