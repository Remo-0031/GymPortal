package com.example.gymmembership;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private TableView<member> mainTable;
    @FXML
    private TableColumn<member, Integer> IDCol;
    @FXML
    private TableColumn<member, String> NameCol;
    @FXML
    private TableColumn<member, Date> BDateCol;
    @FXML
    private TableColumn<member, Date> JDateCol;
    @FXML
    private TableColumn<member, Date> DDateCol;
    @FXML
    private TableColumn<member, Boolean> isStudentCol;
    @FXML
    private TableColumn<member, Boolean> withTrainerCol;

    private ObservableList<member> memberList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        IDCol.setCellValueFactory(new PropertyValueFactory<member, Integer>("ID"));
        NameCol.setCellValueFactory(new PropertyValueFactory<member, String>("fullName"));
        BDateCol.setCellValueFactory(new PropertyValueFactory<member, Date>("birthDate"));
        JDateCol.setCellValueFactory(new PropertyValueFactory<member, Date>("joinDate"));
        DDateCol.setCellValueFactory(new PropertyValueFactory<member, Date>("dueDate"));
        isStudentCol.setCellValueFactory(new PropertyValueFactory<member, Boolean>("student"));
        withTrainerCol.setCellValueFactory(new PropertyValueFactory<member, Boolean>("withTrainer"));
        System.out.println("Test");
        try {
            setupTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void setupTable() throws SQLException {
        try (Connection conn = DBUtils.connect()){
            String sql = "Select * from members";
            CallableStatement stm= conn.prepareCall(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()){
                int ID = rs.getInt("memID");
                String First = rs.getString("First Name");
                String Last = rs.getString("Last Name");
                Date BDate = rs.getDate("Birth Date");
                Date JDate = rs.getDate("Join Date");
                Date DDate = rs.getDate("Due Date");
                boolean student = rs.getBoolean("Student");
                boolean trainer = rs.getBoolean("Trainer");

                memberList.add(new member(ID,First+" "+Last,BDate,JDate,DDate,student,trainer));
            }
            System.out.println(memberList.getFirst().getBirthDate());
            System.out.println("Total members loaded: " + memberList.size());

            member test = memberList.get(0);
            System.out.println("ID: " + test.getID());
            System.out.println("Name: " + test.getFullName());
            System.out.println("BDate: " + test.getBirthDate());
            System.out.println("Student: " + test.isStudent());
            System.out.println("Trainer: " + test.isWithTrainer());

            mainTable.setItems(memberList);
        }catch (SQLException s){
            throw new RuntimeException(s);
        }
    }
}
