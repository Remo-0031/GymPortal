package com.example.gymmembership;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ScanController implements Initializable {
    @FXML
    private Label BDate_Field;

    @FXML
    private Label DDate_field;

    @FXML
    private Label ID_field;

    @FXML
    private Label Name_field;

    @FXML
    private Label isStudent_field;

    @FXML
    private Label wTrainer_field;

    private boolean something_Displayed = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Thread qrThread = new Thread(() -> {
            Webcam webcam = Webcam.getDefault();
            webcam.open();

            while (true){
                BufferedImage image = webcam.getImage();
                if (image == null)continue;

                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try (Connection conn = DBUtils.connect()){
                    Result res = new MultiFormatReader().decode(bitmap);
                    String stm = "SELECT * FROM `members` WHERE MemID=?;";
                    PreparedStatement pstm = conn.prepareStatement(stm);
                    pstm.setInt(1,Integer.parseInt(res.getText()));
                    ResultSet resQ = pstm.executeQuery();
                    if(resQ.next()){
                        String ID = resQ.getString("MemID");
                        String name = resQ.getString("First Name") + " " + resQ.getString("Last Name");
                        String bdate = String.valueOf(resQ.getDate("Birth Date"));
                        String ddate = String.valueOf(resQ.getDate("Due Date"));
                        String wTrainer  = String.valueOf(resQ.getBoolean("Trainer"));
                        String isStudent = String.valueOf(resQ.getBoolean("Student"));

                        Platform.runLater(() -> {
                            try {
                                ID_field.setText(ID);
                                Name_field.setText(name);
                                BDate_Field.setText(bdate);
                                DDate_field.setText(ddate);
                                wTrainer_field.setText(wTrainer);
                                isStudent_field.setText(isStudent);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                } catch (NotFoundException | SQLException ignored) {

                }
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break; // exit thread on interruption
                }
            }
        });
        qrThread.setDaemon(true);
        qrThread.start();

    }
}
