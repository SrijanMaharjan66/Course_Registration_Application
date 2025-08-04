package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    private static Stage primaryStage;
    
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("EduPortal - Student Course Registration System");
        primaryStage.setResizable(true); // Allow resizing
        primaryStage.setMaximized(false); // Start normal size
        
        // Set minimum window size
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        
        // Load login scene
        showLoginScene();
        
        primaryStage.show();
    }
    
    public static void showLoginScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/login.fxml"));
        Scene scene = new Scene(loader.load(), 900, 700);
        scene.getStylesheets().add(Main.class.getResource("/css/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
    }
    
    public static void showDashboardScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/dashboard.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 800);
        scene.getStylesheets().add(Main.class.getResource("/css/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
    }
    
    public static void showProfileScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/profile.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);
        scene.getStylesheets().add(Main.class.getResource("/css/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
    }
    
    public static void showRegisterCourseScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/register-course.fxml"));
        Scene scene = new Scene(loader.load(), 900, 700);
        scene.getStylesheets().add(Main.class.getResource("/css/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
    }
    
    public static void showViewCoursesScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/view-courses.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        scene.getStylesheets().add(Main.class.getResource("/css/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
    }
    
    public static void showRegisterScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/register.fxml"));
        Scene scene = new Scene(loader.load(), 800, 800);
        scene.getStylesheets().add(Main.class.getResource("/css/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
    }
    
    public static void showChartsScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/charts.fxml"));
        Scene scene = new Scene(loader.load(), 1100, 800);
        scene.getStylesheets().add(Main.class.getResource("/css/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
    }
    
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
