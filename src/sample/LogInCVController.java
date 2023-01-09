package sample;

import com.sun.glass.ui.CommonDialogs;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LogInCVController implements Initializable {
    public static int userID;
    public static int position;
    public static int companyID;
    public static String companyCVName;
    public static String companyNamee;
    public static File f ;
    @FXML
    private AnchorPane CVPane,loginPane;

    @FXML
    private TextField ID,pswd;
    @FXML
    private Hyperlink logIN , CV ;
    @FXML
    private ChoiceBox companyName;
    @FXML
    private Line l1 ;
    @FXML
    private Line l2;
    ObservableList<String> companies = FXCollections.observableArrayList(); ;
    public void showCV (){
   CVPane.setVisible(true);
   loginPane.setVisible(false);
   logIN.setTextFill(new Color(0.7882, 0.7882, 0.7882, 1.0));
   l1.setStroke(new Color(0.7882, 0.7882, 0.7882, 1.0));
   CV.setTextFill(new Color(0.2667,0.4549, 0.4196, 1.0));
   l2.setStroke(new Color(0.2667,0.4549, 0.4196, 1.0));
    }

    public void hideCV (){
        loginPane.setVisible(true);
        CVPane.setVisible(false);
        CV.setTextFill(new Color(0.7882, 0.7882, 0.7882, 1.0));
        l1.setStroke(new Color(0.2667,0.4549, 0.4196, 1.0));
        logIN.setTextFill(new Color(0.2667,0.4549, 0.4196, 1.0));
        l2.setStroke(new Color(0.7882, 0.7882, 0.7882, 1.0));
    }



    public void logIn(ActionEvent event) throws IOException{
        Unit u = new Unit();
        String idStr;
        String searchStr ;
        int temp = 0 ;
        String empID , empPswd;
        empID = ID.getText();
        empPswd = pswd.getText() ;
        if(empID.equals("")||empPswd.equals("")){
            u.showAlert("Error", "Log In", "ID or Password aren't filled !", Alert.AlertType.ERROR);
        }
        else {
            try {
                Connection con = u.mySQLConnect();
                Statement s = con.createStatement();
                searchStr = "select count(ID) from employees where ID = " + empID ;
                ResultSet check = s.executeQuery(searchStr);
                check.next();
                temp += check.getInt(1) ;

                searchStr = "select count(ID) from managers where ID = " + empID ;
                check = s.executeQuery(searchStr);
                check.next();
                temp += check.getInt(1) ;

                if(temp == 0){
                    u.showAlert("Error", "Log In", "ID not found!", Alert.AlertType.ERROR);
                }
                else {
                    companyID = Integer.parseInt(empID.substring(0,2));
                    position = Integer.parseInt(empID.substring(2,3));
                    userID = Integer.parseInt(empID);
                    if (position == 0) {
                        idStr = "select password from employees where ID = " + empID;
                        ResultSet rs = s.executeQuery(idStr);
                        rs.next();
                        if(rs.getString(1) .equals(empPswd)) {
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            Stage root = FXMLLoader.load(getClass().getResource("checkInEMP.fxml"));
                            stage.setScene(root.getScene());
                            stage.setResizable(false);
                        }
                        else {
                            u.showAlert("Error", "Log In", "Password and ID don't  match,Check your passowrd!", Alert.AlertType.ERROR);
                        }
                    }
                    else if (position == 1){
                        idStr = "select password from managers where ID = " + empID;
                        ResultSet rs = s.executeQuery(idStr);
                        rs.next();
                        if(rs.getString(1) .equals(empPswd)) {
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            Stage root = FXMLLoader.load(getClass().getResource("ManagerHomePage.fxml"));
                            stage.setScene(root.getScene());
                            stage.setResizable(false);
                        }
                        else {
                            u.showAlert("Error", "Log In", "Password and ID don't match,Check your passowrd!", Alert.AlertType.ERROR);

                        }
                        }
                    }
                }
            catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            Unit u = new Unit();
            Connection con = u.mySQLConnect();
            Statement s = con.createStatement();
            String searchStr = "select Name from company ;";
            ResultSet check = s.executeQuery(searchStr);
            String cn ;
            while(check.next()) {
            cn = check.getString(1);
                companies.add(cn);
            }
            companyName.setValue("choose company");
            companyName.setItems(companies);
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void fileUpload (ActionEvent event){
        FileChooser fc = new FileChooser() ;
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files","*.png", "*.jpg", "*.gif")) ;
        f = fc.showOpenDialog(null);

    }

    public void upload(ActionEvent event) throws SQLException, FileNotFoundException {
        companyCVName = (String)companyName.getValue();
        Unit u = new Unit();
        Connection con = u.mySQLConnect();
        Statement st = con.createStatement();
        InputStream in = new FileInputStream(f);
//        System.out.println(in);
        String s = "insert into CV (CV,CompanyName) values(?,'"+companyCVName+"')" ;
        PreparedStatement pstmt = con.prepareStatement(s);
        pstmt.setBlob(1, in);
        pstmt.execute();
    }

    public static String logInn (int empID,String empPswd ) throws IOException{
        Unit u = new Unit();
        String idStr;
        String searchStr ;
        int temp = 0 ;
        try {
            Connection con = u.mySQLConnect();
            Statement s = con.createStatement();
            searchStr = "select count(ID) from employees where ID = " + empID ;
            ResultSet check = s.executeQuery(searchStr);
            check.next();
            temp += check.getInt(1) ;

            searchStr = "select count(ID) from managers where ID = " + empID ;
            check = s.executeQuery(searchStr);
            check.next();
            temp += check.getInt(1) ;

            if(temp == 0){
                return "ID not found";
            }
            else {
                position = Integer.parseInt(String.valueOf(empID).substring(2,3));
                if (position == 0) {
                    idStr = "select password from employees where ID = " + empID;
                    ResultSet rs = s.executeQuery(idStr);
                    rs.next();
                    if(rs.getString(1) .equals(empPswd)) {
                        return "logged in successfully" ;
                    }
                    else {
                        return "incorrect password" ;
                    }
                }
                else if (position == 1){
                    idStr = "select password from managers where ID = " + empID;
                    ResultSet rs = s.executeQuery(idStr);
                    rs.next();
                    if(rs.getString(1) .equals(empPswd)) {
                        return "logged in successfully" ;
                    }
                    else {
                        return "incorrect password" ;

                    }
                }
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "" ;
    }
}
