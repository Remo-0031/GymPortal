package com.example.gymmembership;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class LoginController {
    @FXML
    private TextField User;
    @FXML
    private PasswordField PassF;
    @FXML
    private Button LogIn;
    @FXML
    private Text Forgot;
    @FXML
    private Text Incorrect;
    @FXML
    private AnchorPane ap;

    @FXML
    private void onKeyPressed(KeyEvent event) throws Exception{
        if(event.getCode() == KeyCode.ENTER){
            onLoginClick(new ActionEvent(event.getSource(),null));
        }
    }

    @FXML
    protected void onLoginClick(ActionEvent event) throws Exception {
        String UserName = User.getText();
        String Password = PassF.getText();
        try (Connection conn = DBUtils.connect()){
            String sql = "Select * from credentials WHERE UserName=? and PassWord=? and Privilege=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,UserName);
            stmt.setString(2,Password);
            stmt.setString(3,"Admin");
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                System.out.println("Login Success");
                FXMLLoader homeFXML = new FXMLLoader(getClass().getResource("Home-view.fxml"));
                Parent root = homeFXML.load();
                Scene homeScene = new Scene(root,1920,1080);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(homeScene);
                stage.setMaximized(true);
            }else {
                Incorrect.setFill(Color.RED);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}