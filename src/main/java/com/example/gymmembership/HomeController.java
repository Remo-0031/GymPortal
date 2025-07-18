package com.example.gymmembership;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Member;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    public Button up_button;
    @FXML
    private Button del_button;
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

        mainTable.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 2 ){
                    try {
                        openUpdateForm(new ActionEvent());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void setupTable() throws SQLException {
        LoadTable();
    }

    @FXML
    void removeRow(ActionEvent Event)throws Exception{
        int SelectedID = mainTable.getSelectionModel().getSelectedItem().getID();
        System.out.println(SelectedID);
        removeID(SelectedID);
        LoadTable();
    }

    public void LoadTable() {
        memberList.clear();
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

            member test = memberList.getFirst();
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

    private void removeID(int ID) {
        try (Connection conn = DBUtils.connect()){
            String stm = "DELETE FROM members WHERE `members`.`MemID` = ?";
            PreparedStatement pr = conn.prepareStatement(stm);
            pr.setInt(1,ID);
            if(pr.execute()){
                System.out.println("Member successfully deleted!");
            }else {
                System.out.println("Member deletion fail!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void openUpdateForm(ActionEvent event) throws IOException {
        member chosenMember = mainTable.getSelectionModel().getSelectedItem();
        if(chosenMember != null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Form-View.fxml"));
            Parent root = loader.load();

            //Set the Member in the FormController Class
            formController controller = loader.getController();
            controller.setMember(chosenMember);
            controller.setInformation();

            //Set the Scene and the Stage also make the modality Application_Modal so that the user will
            // Focus on the new Stage that was open
            Scene sc = new Scene(root,362,392);
            Stage newStage = new Stage();
            newStage.setTitle("Form");
            newStage.setScene(sc);
            newStage.setResizable(false);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.showAndWait();
            LoadTable();
        }
    }

    @FXML
    private void ScanMode(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Scan-View.fxml"));
        Parent root = loader.load();

        Scene sc = new Scene(root,1920,1080);
        Stage nStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        nStage.setScene(sc);
        nStage.setMaximized(true);
        nStage.setResizable(false);
    }
}
