package com.example.controller;

import com.example.Main;
import com.example.model.Course;
import com.example.util.DataManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ChartsController implements Initializable {
    
    @FXML private TabPane chartsTabPane;
    @FXML private Tab pieChartTab;
    @FXML private Tab barChartTab;
    @FXML private PieChart coursesPieChart;
    @FXML private BarChart<String, Number> coursesBarChart;
    @FXML private Label totalCoursesLabel;
    @FXML private Label totalCreditsLabel;
    @FXML private Label studentNameLabel;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String studentName = DataManager.getCurrentStudent().getName();
        studentNameLabel.setText("Student: " + studentName);
        
        loadChartsData();
        updateSummary();
    }
    
    private void loadChartsData() {
        List<Course> registeredCourses = DataManager.getRegisteredCourses();
        
        if (registeredCourses.isEmpty()) {
            // Show empty state
            showEmptyState();
            return;
        }
        
        // Count courses by program
        Map<String, Integer> programCounts = new HashMap<>();
        Map<String, Integer> programCredits = new HashMap<>();
        
        for (Course course : registeredCourses) {
            String program = course.getProgram();
            programCounts.put(program, programCounts.getOrDefault(program, 0) + 1);
            programCredits.put(program, programCredits.getOrDefault(program, 0) + course.getCredits());
        }
        
        // Create pie chart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : programCounts.entrySet()) {
            PieChart.Data data = new PieChart.Data(
                entry.getKey() + " (" + entry.getValue() + " courses)", 
                entry.getValue()
            );
            pieChartData.add(data);
        }
        
        coursesPieChart.setData(pieChartData);
        coursesPieChart.setTitle("Courses by Program");
        
        // Apply custom colors to pie chart
        applyPieChartColors();
        
        // Create bar chart data
        XYChart.Series<String, Number> coursesSeries = new XYChart.Series<>();
        coursesSeries.setName("Number of Courses");
        
        XYChart.Series<String, Number> creditsSeries = new XYChart.Series<>();
        creditsSeries.setName("Total Credits");
        
        for (Map.Entry<String, Integer> entry : programCounts.entrySet()) {
            String program = entry.getKey();
            coursesSeries.getData().add(new XYChart.Data<>(program, entry.getValue()));
            creditsSeries.getData().add(new XYChart.Data<>(program, programCredits.get(program)));
        }
        
        coursesBarChart.getData().clear();
        coursesBarChart.getData().addAll(coursesSeries, creditsSeries);
        coursesBarChart.setTitle("Courses and Credits by Program");
    }
    
    private void applyPieChartColors() {
        // Apply custom colors after the chart is rendered
        coursesPieChart.applyCss();
        coursesPieChart.layout();
        
        ObservableList<PieChart.Data> data = coursesPieChart.getData();
        String[] colors = {
            "#3498db", // Blue for first program
            "#e74c3c", // Red for second program  
            "#2ecc71", // Green for third program
            "#f39c12", // Orange for fourth program
            "#9b59b6", // Purple for fifth program
            "#1abc9c"  // Teal for sixth program
        };
        
        for (int i = 0; i < data.size(); i++) {
            PieChart.Data pieData = data.get(i);
            String color = colors[i % colors.length];
            pieData.getNode().setStyle("-fx-pie-color: " + color + ";");
        }
    }
    
    private void showEmptyState() {
        // Create empty pie chart data
        ObservableList<PieChart.Data> emptyData = FXCollections.observableArrayList();
        emptyData.add(new PieChart.Data("No courses registered", 1));
        coursesPieChart.setData(emptyData);
        coursesPieChart.setTitle("No Courses Registered");
        
        // Apply gray color for empty state
        coursesPieChart.applyCss();
        coursesPieChart.layout();
        if (!coursesPieChart.getData().isEmpty()) {
            coursesPieChart.getData().get(0).getNode().setStyle("-fx-pie-color: #bdc3c7;");
        }
        
        // Clear bar chart
        coursesBarChart.getData().clear();
        coursesBarChart.setTitle("No Data Available");
    }
    
    private void updateSummary() {
        List<Course> registeredCourses = DataManager.getRegisteredCourses();
        int totalCourses = registeredCourses.size();
        int totalCredits = registeredCourses.stream().mapToInt(Course::getCredits).sum();
        
        totalCoursesLabel.setText("Total Courses: " + totalCourses);
        totalCreditsLabel.setText("Total Credits: " + totalCredits);
    }
    
    @FXML
    private void handleRefresh() {
        loadChartsData();
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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
