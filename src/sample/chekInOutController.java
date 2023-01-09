package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class chekInOutController implements Initializable {
    @FXML
    private Label homei;
    @FXML
    private Hyperlink profile;
    @FXML
    private Hyperlink check;
    @FXML
    private Hyperlink vacation;
    @FXML
    private Button in;
    @FXML
    private Button out;
    @FXML
    private Button search;
    @FXML
    private TextField found;
    @FXML
    private ImageView profilePhoto;
    public String cn;
    public int late;
    public int entertime;

    @FXML
    public void homei (ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("checkInEMP.fxml"));
        stage.setScene(root.getScene());

    }

    @FXML
    public void check (ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("checkInEMP.fxml"));
        stage.setScene(root.getScene());

    }
    @FXML

    public void vacation (ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("requestVacation.fxml"));
        stage.setScene(root.getScene());

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Unit u = new Unit();
            Connection con = u.mySQLConnect();
            Statement s = con.createStatement();
            String searchStr = "select Name from company where ID = " + LogInCVController.companyID;
            ResultSet check = s.executeQuery(searchStr);
            check.next() ;
             cn = check.getString(1) ;
            s = con.createStatement();
            searchStr = "select Photo from employees where ID = " + LogInCVController.userID;
            check = s.executeQuery(searchStr);
            check.next() ;
            String pn = check.getString(1) ;
            Image MImage = new Image(getClass().getResourceAsStream("/employeesPhotos/"+pn));
            profilePhoto.setImage(MImage);


        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @FXML
    public void profile (ActionEvent event) throws IOException {
        profileController.id = LogInCVController.userID ;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("profile.fxml"));
        stage.setScene(root.getScene());

    }
    @FXML
    public void in (ActionEvent event) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
        LocalDate localDate = LocalDate.now();
        localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localTime = LocalTime.now();
        Time time = Time.valueOf( localTime );
        String time1=time.toString();
        String[] hourMin = time1.split(":");
        int hour = Integer.parseInt(hourMin[0]);
        entertime=hour;
        System.out.println(hour);
        float late=0;
        try {

            Unit u = new Unit();
            Connection con = u.mySQLConnect();
            String query1 = "insert into workhours(EntryTime,LateHours,WorkDay,id,companyName) values (?,?,?,?,?)";
            PreparedStatement st = con.prepareStatement(query1);
            st.setTime(1,(time));
            if(hour!=8){
                late=hour-8;
            }
            st.setFloat(2,(late));
            st.setDate(3,Date.valueOf(localDate));
            st.setInt(4,LogInCVController.userID);
            st.setString(5,cn);
            st.executeUpdate();

        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    @FXML
    public void out (ActionEvent event) throws IOException {

        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localTime = LocalTime.now();
        Time time = Time.valueOf( localTime );
        int Eworkhours=0;
        int early=0;
        String time1=time.toString();
        String[] hourMin = time1.split(":");
        int hour = Integer.parseInt(hourMin[0]);
        int lh;

        try {
            Unit u = new Unit();
            Connection con = u.mySQLConnect();
            Statement s = con.createStatement();
            String query1 = "update workhours set DepartureTime=?,WorkHours=?,LateHours=? where id="+LogInCVController.userID;
            PreparedStatement st = con.prepareStatement(query1);

            st.setTime(1,(time));
            Eworkhours=hour-entertime;
            early=15-hour;
            lh=7-Eworkhours;
            st.setFloat(2,Eworkhours);
            st.setFloat(3,lh);
            st.executeUpdate();

            String searchStr = "select RemVacDay from employees where id="+LogInCVController.userID;
            ResultSet check = s.executeQuery(searchStr);
            check.next() ;
            float ff = check.getFloat(1) ;

            query1 = "update employees set RemVacDay= ? where id="+LogInCVController.userID;
            st = con.prepareStatement(query1);
            ff = ff - (lh/7) ;
            st.setFloat(1,ff);
            st.executeUpdate();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

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


    public void logout(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("LogIn-CV.fxml"));
        stage.setScene(root.getScene());
        stage.setResizable(false);
    }
}
