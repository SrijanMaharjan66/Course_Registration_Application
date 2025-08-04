package com.example.controller;

import com.example.Main;
import com.example.model.Course;
import com.example.util.DataManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewCoursesController implements Initializable {
    
    @FXML private TableView<Course> coursesTable;
    @FXML private TableColumn<Course, String> codeColumn;
    @FXML private TableColumn<Course, String> nameColumn;
    @FXML private TableColumn<Course, String> programColumn;
    @FXML private TableColumn<Course, Integer> creditsColumn;
    @FXML private Label totalCoursesLabel;
    @FXML private Label totalCreditsLabel;
    
    private ObservableList<Course> coursesList;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize table columns
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        programColumn.setCellValueFactory(new PropertyValueFactory<>("program"));
        creditsColumn.setCellValueFactory(new PropertyValueFactory<>("credits"));
        
        // Load registered courses
        loadRegisteredCourses();
        updateSummary();
    }
    
    private void loadRegisteredCourses() {
        coursesList = FXCollections.observableArrayList(DataManager.getRegisteredCourses());
        coursesTable.setItems(coursesList);
    }
    
    private void updateSummary() {
        int totalCourses = coursesList.size();
        int totalCredits = coursesList.stream().mapToInt(Course::getCredits).sum();
        
        totalCoursesLabel.setText("Total Courses: " + totalCourses);
        totalCreditsLabel.setText("Total Credits: " + totalCredits);
    }
    
    @FXML
    private void handleDeleteCourse() {
        Course selectedCourse = coursesTable.getSelectionModel().getSelectedItem();
        
        if (selectedCourse == null) {
            showAlert("No Selection", "Please select a course to delete.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Course");
        alert.setHeaderText("Delete Course Registration");
        alert.setContentText("Are you sure you want to unregister from:\n" + 
                           selectedCourse.getCourseCode() + " - " + selectedCourse.getCourseName());
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DataManager.unregisterCourse(selectedCourse);
            loadRegisteredCourses();
            updateSummary();
            
            showSuccessAlert("Course Deleted", "Successfully unregistered from the course.");
        }
    }
    
    @FXML
    private void handleRefresh() {
        loadRegisteredCourses();
        updateSummary();
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
