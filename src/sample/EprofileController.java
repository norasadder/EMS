package sample;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;


public class EprofileController implements Initializable {
    public static int id;

    @FXML
    private Label Eadress;
    @FXML
    private Hyperlink check;
    @FXML
    private Hyperlink profile;
    @FXML
    private Hyperlink vacation;
    @FXML
    private Hyperlink logout;
    @FXML
    private Label Ephone;
    @FXML
    private Label Etmpid;
    @FXML
    public Label Ename;
    @FXML
    public Label Eemail;
    @FXML
    public Label Eposition;
    @FXML
    public Label Ebirthday;
    @FXML
    private ImageView profilePhoto ;
    @FXML
    private ImageView image ;
    @FXML
    private TextField companyName ;



    @FXML
    public Label Esalary;

    @FXML
    public Button getinfo;
    @FXML
    public TextField found;

     String Eimage;

    @FXML
    public void profile (ActionEvent event) throws IOException {
        EprofileController.id = LogInCVController.userID ;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("Eprofile.fxml"));
        stage.setScene(root.getScene());

    }

    @FXML
    public void logout (ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("LogIn-CV.fxml"));
        stage.setScene(root.getScene());

    }
    @FXML
    public void homei (ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("ManagerHomePage.fxml"));
        stage.setScene(root.getScene());

    }

String cn ;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            Unit u = new Unit();
            Connection con = u.mySQLConnect();
            Statement s = con.createStatement();
            String searchStr = "select Photo from managers where ID = " + LogInCVController.userID;
            ResultSet check = s.executeQuery(searchStr);
            check.next() ;
            String pn = check.getString(1) ;
            Image MImage = new Image(getClass().getResourceAsStream("/employeesPhotos/"+pn));
            profilePhoto.setImage(MImage);
            searchStr = "select Name from company where ID = " + LogInCVController.companyID;
            check = s.executeQuery(searchStr);
            check.next() ;
            String cn = check.getString(1) ;
            LogInCVController.companyNamee = cn ;
            companyName.setText(cn);
                 searchStr = "select * from managers where ID =" +LogInCVController.userID;
                 check = s.executeQuery(searchStr);
                check.next() ;
                Ename.setText(check.getString("Fname")+" "+check.getString("Lname"));
                   Etmpid.setText(check.getString("ID"));
                    Eemail.setText(check.getString("Email"));
                    Ephone.setText(check.getString("PhoneNumber"));
                    Eadress.setText(check.getString("Address"));
                    Eposition.setText(check.getString("Position"));
                    Ebirthday.setText(check.getString("Birthdate"));
                    Esalary.setText(check.getString("Salary"));
                    Eimage=check.getString("Photo");
                    image.setImage( new Image(getClass().getResourceAsStream("/employeesPhotos/"+Eimage)));
            }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}

