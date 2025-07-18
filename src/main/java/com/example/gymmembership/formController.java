package com.example.gymmembership;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class formController{

    @FXML
    private DatePicker BDATE_PICKER;

    @FXML
    private Button Cancel_Button;

    @FXML
    private DatePicker DDate_Picker;

    @FXML
    private TextField FirstName_Field;

    @FXML
    private TextField LastName_Field;

    @FXML
    private Button Proceed_Button;

    @FXML
    private MenuButton Promo_Picker;

    @FXML
    private CheckBox Student_CheckBox;

    @FXML
    private CheckBox Trainer_CheckBox;

    private member memberInformation;

    @FXML
    private Button generateQR_button;

    public void setMember(member memberI){
        memberInformation = memberI;
        System.out.println(memberInformation.getFullName());
    }

    public member getMemberInformation() {
        return memberInformation;
    }

    public void setInformation() {
        FirstName_Field.setText(getMemberInformation().getFullName().split(" ")[0]);
        LastName_Field.setText(getMemberInformation().getFullName().split(" ")[1]);
        BDATE_PICKER.setValue(getMemberInformation().getBirthDate().toLocalDate());
        DDate_Picker.setValue(getMemberInformation().getDueDate().toLocalDate());
        Student_CheckBox.setSelected(memberInformation.isStudent());
        Trainer_CheckBox.setSelected(memberInformation.isWithTrainer());
    }
    @FXML
    public void UpdateMember(ActionEvent event){
        String fname_updated = FirstName_Field.getText();
        String lname_updated = LastName_Field.getText();
        Date BDay_Updated = Date.valueOf(BDATE_PICKER.getValue());
        Date DueDate_Updated = Date.valueOf(DDate_Picker.getValue());
        boolean isStudent_updated = Student_CheckBox.isSelected();
        boolean withTrainer_updated = Trainer_CheckBox.isSelected();

        try (Connection conn = DBUtils.connect()){
            String stm = "UPDATE `members` SET `First Name` = ?, `Last Name` = ?," +
                    " `Birth Date` = ?, `Due Date` = ?, `Student` = ?, " +
                    "`Trainer` = ? WHERE `members`.`MemID` = ?";
            PreparedStatement ps = conn.prepareStatement(stm);
            ps.setString(1,fname_updated);
            ps.setString(2,lname_updated);
            ps.setDate(3,BDay_Updated);
            ps.setDate(4,DueDate_Updated);
            ps.setBoolean(5,isStudent_updated);
            ps.setBoolean(6,withTrainer_updated);
            ps.setInt(7,getMemberInformation().getID());

            if(ps.executeUpdate()>0){
                System.out.println("Member information updated successfully");
                Stage formStage = (Stage)Student_CheckBox.getScene().getWindow();
                formStage.close();
            }else {
                System.out.println("Member information was not updated");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void generateQR(ActionEvent event) throws Exception {
        String path = "src/main/resources/images/QRs/" + memberInformation.getID() + ".jpg";

        BitMatrix matrix = new MultiFormatWriter().encode(String.valueOf(memberInformation.getID()), BarcodeFormat.QR_CODE,500,500);

        MatrixToImageWriter.writeToPath(matrix,"jpg", Paths.get(path));

        File imageFile = new File(path);
        if (Desktop.isDesktopSupported()){
            try {
                Desktop.getDesktop().open(imageFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            System.out.println("ERR");
        }
    }
}
