package sample;

import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.text.Element;
import java.io.IOException;
import java.util.ResourceBundle;

public class ManagerHomeController implements Initializable {
    @FXML
    private ImageView profilePhoto ;

    @FXML
    private TextField companyName ;

    public void LogOut(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("LogIn-CV.fxml"));
        stage.setScene(root.getScene());
        stage.setResizable(false);
    }
    public void manageVac(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("manageVacations.fxml"));
        stage.setScene(root.getScene());
        stage.setResizable(false);
    }

    public void manageEmp(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("manageEmployees.fxml"));
        stage.setScene(root.getScene());
        stage.setResizable(false);
    }


    public void showProfile(ActionEvent event) throws IOException {
        profileController.id = LogInCVController.userID ;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("Eprofile.fxml"));
        stage.setScene(root.getScene());
        stage.setResizable(false);
    }

    public void CVS(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("showUploadedCvs.fxml"));
        stage.setScene(root.getScene());
        stage.setResizable(false);
    }

    public void showWorkHours(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("showWorkingHours.fxml"));
        stage.setScene(root.getScene());
        stage.setResizable(false);
    }
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
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
