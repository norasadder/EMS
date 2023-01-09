package sample;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.Button;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;



public class profileController implements Initializable {
    public static int id;
    @FXML
    private Label homei;
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
    public Label Esalary;

    @FXML
    public Button getinfo;
    @FXML
    public TextField found;

     String Eimage;
    @FXML
    public void check (ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("checkInEMP.fxml"));
        stage.setScene(root.getScene());

    }
    @FXML
    public void profile (ActionEvent event) throws IOException {
        profileController.id = LogInCVController.userID ;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("profile.fxml"));
        stage.setScene(root.getScene());

    }

    @FXML
    public void vacation (ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("requestVacation.fxml"));
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
        Stage root = FXMLLoader.load(getClass().getResource("checkInEMP.fxml"));
        stage.setScene(root.getScene());

    }

    @FXML
    public void Esearch (ActionEvent event) {
        try {
            String i = found.getText();
            Unit u = new Unit();
            if (i.equals(null)) {
                u.showAlert("Error", "search", "please enter an ID!", Alert.AlertType.ERROR);
            } else {
                Connection con = u.mySQLConnect();
                Statement s = con.createStatement();
                Statement s2 = con.createStatement();
                String stmtqry1 = "select * from employees where ID = "+Integer.parseInt(i)+" and CompanyName = '"+cn+"'";
                String stmtqry2 = "select * from managers where ID ="+Integer.parseInt(i)+" and CompanyName = '"+cn+"'";
                ResultSet resultSet1 = s.executeQuery(stmtqry1);
                ResultSet resultSet2 = s2.executeQuery(stmtqry2);
                if(resultSet1.next()|| resultSet2.next() ) {
                    profileController.id = Integer.parseInt(i);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Stage root = FXMLLoader.load(getClass().getResource("profile.fxml"));
                    stage.setScene(root.getScene());
                }
                    else {
                        u.showAlert("Error", "search", "there's no employee or manager  with this ID!", Alert.AlertType.ERROR);
                    }

                }
            } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
String cn ;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            Unit u = new Unit();
            Connection con = u.mySQLConnect();
            Statement s = con.createStatement();
           String searchStr = "select Name from company where ID = " + LogInCVController.companyID;
            ResultSet check = s.executeQuery(searchStr);
            check.next() ;
             cn = check.getString(1) ;

            if(LogInCVController.position == 1){
                searchStr = "select Photo from managers where ID = " + LogInCVController.userID;
                 check = s.executeQuery(searchStr);
                check.next() ;
                String pn = check.getString(1) ;
                Image MImage = new Image(getClass().getResourceAsStream("/employeesPhotos/"+pn));
                profilePhoto.setImage(MImage);
            }
            else {
                 searchStr = "select Photo from employees where ID = " + LogInCVController.userID;
                 check = s.executeQuery(searchStr);
                check.next() ;
                String pn = check.getString(1) ;
                Image MImage = new Image(getClass().getResourceAsStream("/employeesPhotos/"+pn));
                profilePhoto.setImage(MImage);
            }
            if(profileController.id==LogInCVController.userID){
                 searchStr = "" ;
                if(LogInCVController.position == 1) {
                     searchStr = "select * from managers where ID =" + profileController.id;
                }
                else {
                    searchStr = "select * from employees where ID =" + profileController.id;
                }
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
            else if(!(profileController.id==LogInCVController.userID)) {
                String ss = String.valueOf(profileController.id);
                int i = Integer.parseInt(ss.substring(2,3));
                if(i== 1) {
                     searchStr = "select Fname,Lname,Email,PhoneNumber,Position,Birthdate,photo,ID from managers where ID = " + profileController.id;
                }
                else {
                    searchStr = "select Fname,Lname,Email,PhoneNumber,Position,Birthdate,photo,ID from employees where ID = " + profileController.id;
                }
                 check = s.executeQuery(searchStr);
                check.next() ;
                Ename.setText(check.getString("Fname")+" "+check.getString("Lname"));
                Etmpid.setText(check.getString("ID"));
                Eemail.setText(check.getString("Email"));
                Ephone.setText(check.getString("PhoneNumber"));
                Eposition.setText(check.getString("Position"));
                Ebirthday.setText(check.getString("Birthdate"));
                Eadress.setText("______");
                Esalary.setText("______");
                Eimage=check.getString("Photo");
                image.setImage( new Image(getClass().getResourceAsStream("/employeesPhotos/"+Eimage)));

            }


            con.close();


        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    public static String searchTest (int id,String cn) {
        try {
            Unit u = new Unit();
            Connection con = u.mySQLConnect();
            Statement s = con.createStatement();
            Statement s2 = con.createStatement();
            String stmtqry1 = "select * from employees where ID = "+id+" and CompanyName = '"+cn+"'";
            String stmtqry2 = "select * from managers where ID ="+id+" and CompanyName = '"+cn+"'";
            ResultSet resultSet1 = s.executeQuery(stmtqry1);
            ResultSet resultSet2 = s2.executeQuery(stmtqry2);
            if(resultSet1.next()|| resultSet2.next() ) {
                return "found" ;
            }
            else {
                return "not found" ;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return "";
    }

}

