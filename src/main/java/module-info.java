module com.example.gymmembership {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires webcam.capture;


    opens com.example.gymmembership to javafx.fxml;
    exports com.example.gymmembership;
}