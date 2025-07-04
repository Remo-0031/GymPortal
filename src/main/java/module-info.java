module com.example.gymmembership {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.gymmembership to javafx.fxml;
    exports com.example.gymmembership;
}