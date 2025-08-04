package com.example.controller;

import com.example.Main;
import com.example.model.Course;
import com.example.util.DataManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RegisterCourseController implements Initializable {
    
    @FXML private ComboBox<String> programFilter;
    @FXML private VBox coursesContainer;
    @FXML private Label selectedCoursesLabel;
    
    private List<CheckBox> courseCheckBoxes = new ArrayList<>();
    private List<Course> selectedCourses = new ArrayList<>();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        programFilter.getItems().addAll("All", "BBA", "BBS", "BCS");
        programFilter.setValue("All");
        programFilter.setOnAction(e -> filterCourses());
        
        loadCourses();
        updateSelectedCoursesLabel();
    }
    
    private void loadCourses() {
        coursesContainer.getChildren().clear();
        courseCheckBoxes.clear();
        
        String selectedProgram = programFilter.getValue();
        List<Course> courses = DataManager.getAvailableCourses();
        
        for (Course course : courses) {
            if (selectedProgram.equals("All") || course.getProgram().equals(selectedProgram)) {
                CheckBox checkBox = new CheckBox();
                checkBox.setText(course.toString() + " (" + course.getCredits() + " credits)");
                checkBox.setUserData(course);
                checkBox.setSelected(DataManager.isRegistered(course));
                
                checkBox.setOnAction(e -> {
                    Course selectedCourse = (Course) checkBox.getUserData();
                    if (checkBox.isSelected()) {
                        if (!selectedCourses.contains(selectedCourse)) {
                            selectedCourses.add(selectedCourse);
                        }
                    } else {
                        selectedCourses.remove(selectedCourse);
                    }
                    updateSelectedCoursesLabel();
                });
                
                if (checkBox.isSelected()) {
                    selectedCourses.add(course);
                }
                
                courseCheckBoxes.add(checkBox);
                coursesContainer.getChildren().add(checkBox);
            }
        }
    }
    
    private void filterCourses() {
        selectedCourses.clear();
        loadCourses();
    }
    
    private void updateSelectedCoursesLabel() {
        selectedCoursesLabel.setText("Selected Courses: " + selectedCourses.size());
    }
    
    @FXML
    private void handleRegister() {
        if (selectedCourses.isEmpty()) {
            showAlert("No Selection", "Please select at least one course to register.");
            return;
        }
        
        int newRegistrations = 0;
        for (Course course : selectedCourses) {
            if (!DataManager.isRegistered(course)) {
                DataManager.registerCourse(course);
                newRegistrations++;
            }
        }
        
        if (newRegistrations > 0) {
            showSuccessAlert("Registration Successful", 
                "Successfully registered for " + newRegistrations + " new course(s)!");
        } else {
            showAlert("Already Registered", "You are already registered for all selected courses.");
        }
        
        // Refresh the checkboxes to show current registration status
        loadCourses();
    }
    
    @FXML
    private void handleBackToDashboard() {
        try {
            Main.showDashboardScene();
        } catch (Exception e) {
            showAlert("Error", "Failed to load dashboard: " + e.getMessage());
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
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
