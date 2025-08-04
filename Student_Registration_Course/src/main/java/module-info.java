module studentregistration {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    
    exports com.example;
    exports com.example.controller;
    exports com.example.model;
    exports com.example.util;
    
    opens com.example to javafx.fxml;
    opens com.example.controller to javafx.fxml;
    opens com.example.model to javafx.fxml;
}
