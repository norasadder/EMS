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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
public class requestVacationController implements Initializable {

    @FXML
    private CheckBox emergency;

    @FXML
    private DatePicker endDate;

    @FXML
    private TextField found;

    @FXML
    private DatePicker startDate;

    @FXML
    private ImageView profilePhoto ;
    @FXML
    private Label rem;
    String cn;
    public void initialize(URL url, ResourceBundle rb) {
        try
        {
            Unit u=new Unit();
            Connection con = u.mySQLConnect();
            Statement s = con.createStatement();
            String searchStr = "select Photo from employees where ID = " + LogInCVController.userID;
            ResultSet check = s.executeQuery(searchStr);
            check.next() ;
            String pn = check.getString(1) ;
            Image MImage = new Image(getClass().getResourceAsStream("/employeesPhotos/"+pn));
            profilePhoto.setImage(MImage);
            s = con.createStatement();
            searchStr = "select Name from company where ID = " + LogInCVController.companyID;
            check = s.executeQuery(searchStr);
            check.next() ;
            cn = check.getString(1) ;

            searchStr = "select RemVacDay from employees where ID = " + LogInCVController.userID;
            check = s.executeQuery(searchStr);
            check.next() ;
            rem.setText(String.valueOf(check.getInt(1)));
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    @FXML
    public void Request() {
        Unit con=new Unit();
        try{
            LocalDate startD = startDate.getValue();
            LocalDate endD = endDate.getValue();
            int isEmergency  = emergency.isSelected()?1:0;
            if(startD ==null || endD == null) {
                con.showAlert("Error", "Request Vacation", "Start Date or End date aren't filled !", Alert.AlertType.ERROR);
            }
            else {
                int noOfDaysBetween = (int) ChronoUnit.DAYS.between(startD, endD);
                Connection connection=con.mySQLConnect();
                String addRequest;
                addRequest = "insert into vacation (StartDate,EndDate," +
                        "ReqVacDay,Emergency,Flag,CompanyName,EmpID) values (?,?,?,?,?,?,?)";
                PreparedStatement st = connection.prepareStatement(addRequest);
                st.setDate(1,Date.valueOf(startD));
                st.setDate(2,Date.valueOf(endD));
                st.setInt(3, noOfDaysBetween);
                st.setInt(4, isEmergency);
                st.setInt(5, 2);
                st.setString(6,cn);
                st.setInt(7,LogInCVController.userID);
                st.executeUpdate();
                connection.close();
            }
        }
        catch (Exception e) {
            System.out.println(e);
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
                String stmtqry1 = "select * from employees where ID = "+Integer.parseInt(i);
                String stmtqry2 = "select * from managers where ID ="+Integer.parseInt(i);
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


    public void CheckInOut(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("checkInEMP.fxml"));
        stage.setScene(root.getScene());
        stage.setResizable(false);
    }

    public void showProfile(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("profile.fxml"));
        stage.setScene(root.getScene());
        stage.setResizable(false);
    }

    public void LogOut(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("LogIn-CV.fxml"));
        stage.setScene(root.getScene());
        stage.setResizable(false);
    }

    public void reqVac(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("requestVacation.fxml"));
        stage.setScene(root.getScene());
        stage.setResizable(false);
    }
}