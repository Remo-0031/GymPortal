module com.example.gymmembership {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.gymmembership to javafx.fxml;
    exports com.example.gymmembership;
}