package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class manageEmpController implements Initializable {
    @FXML
    private TextField ID,phone,salary, Fname,Lname,email,position,compName,address,photo,companyName;
    @FXML
    private DatePicker Birthdate;
    @FXML
    private PasswordField password;
    @FXML
    private ImageView profilePhoto ; ;

    private boolean ValidPass(){
        Pattern pattern;
        Matcher matcher;
        String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{6,15})";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password.getText());

        if(matcher.matches()){
            return true;
        }
        else return false;
    }
    public void searchEmp(){
        Unit con=new Unit();
        boolean flag=false;
        try{
            Connection connection=con.mySQLConnect();
            String sqlSearch="select * from employees";
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sqlSearch);

            while(resultSet.next()){
                if(ID.getText().equals(resultSet.getString("ID"))){
                   if(companyName.getText().equals(resultSet.getString("CompanyName"))) {
                       ID.setText(resultSet.getString("ID"));
                       password.setText(resultSet.getString("Password"));
                       Fname.setText(resultSet.getString("Fname"));
                       Lname.setText(resultSet.getString("Lname"));
                       Date date=resultSet.getDate("Birthdate");
                       Birthdate.setValue(date.toLocalDate());
                       email.setText(resultSet.getString("Email"));
                       address.setText(resultSet.getString("Address"));
                       position.setText(resultSet.getString("Position"));
                       photo.setText(resultSet.getString("Photo"));
                       phone.setText(resultSet.getString("PhoneNumber"));
                       salary.setText(resultSet.getString("Salary"));
                       compName.setText(resultSet.getString("CompanyName"));
                       flag=true;
                   }
                }

            }
            if(!flag) {
                con.showAlert("Error", "Manage employees", "there's no employee with this ID!", Alert.AlertType.ERROR);

            }


            connection.close();
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null,e);
        }
    }
    @FXML
    public void addEmp(){
        Pattern TextPattern=Pattern.compile("[a-zA-Z]+");
        Matcher FNameMatcher=TextPattern.matcher(Fname.getText());
        Matcher LNameMatcher=TextPattern.matcher(Lname.getText());
        Matcher addressMatcher=TextPattern.matcher(address.getText());
        Matcher positionMatcher=TextPattern.matcher(position.getText());

        Pattern NumPattern=Pattern.compile("[0-9]+");
        Matcher IDMatcher=NumPattern.matcher(ID.getText());

        Pattern EmailPattern=Pattern.compile("^(.+)@(.+)$");
        Matcher EmailMatcher=EmailPattern.matcher(email.getText());

        Unit con=new Unit();

        boolean exist=false;
        boolean flag1=false;
        if(phone.getText().isEmpty()||salary.getText().isEmpty() || Fname.getText().isEmpty() || Lname.getText().isEmpty()
        || email.getText().isEmpty()||password.getText().isEmpty()||position.getText().isEmpty()||compName.getText().isEmpty()
        ||address.getText().isEmpty()||photo.getText().isEmpty()||Birthdate.getValue().equals("")||ID.getText().isEmpty()){
            con.showAlert("Error","Add employee","Please enter all the requirement field and try again!",
                    Alert.AlertType.ERROR);
        }
        else if(!ValidPass()){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate password");
            alert.setHeaderText(null);
            alert.setContentText("Password must contain at least one (digit,lowercase,uppercase and special character)" +
                    " and length must be between 6-15");
            alert.showAndWait();
        }
        else if(!FNameMatcher.matches()){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.setContentText("Please make sure that employee's first name not contains numbers");
            alert.showAndWait();
        }
        else if(!LNameMatcher.matches()){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.setContentText("Please make sure that employee's last name not contains numbers");
            alert.showAndWait();
        }
        else if(!addressMatcher.matches()){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.setContentText("Please make sure that employee's address not contains numbers");
            alert.showAndWait();
        }
        else if(!positionMatcher.matches()){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.setContentText("Please make sure that employee's position not contains numbers");
            alert.showAndWait();
        }
        else if(!IDMatcher.matches()){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.setContentText("Please make sure that employee's ID not contains characters");
            alert.showAndWait();
        }
        else if(!EmailMatcher.matches()){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.setContentText("Please make sure that employee's Email is correct");
            alert.showAndWait();
        }
        else {
            try {

                Connection connection=con.mySQLConnect();
                String sqlSearch="select * from employees";
                Statement statement=connection.createStatement();
                ResultSet resultSet=statement.executeQuery(sqlSearch);
                while (resultSet.next()){
                    if(ID.getText().equals(resultSet.getString("ID"))){
                        con.showAlert("ERROR", "Manage employees", "There's an employee with the same ID you entered!", Alert.AlertType.ERROR);
                        exist=true;
                        break;
                    }
                }
                if(!exist){
                    String addEmpString;
                    addEmpString = "insert into employees (ID,Fname,Lname,Email,Password,PhoneNumber,Address,Position" +
                            ",Photo,Birthdate,Salary,CompanyName) values (?,?,?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement st = connection.prepareStatement(addEmpString);
                    System.out.println("con done");
                    st.setInt(1,Integer.parseInt(ID.getText()));
                    st.setString(2, Fname.getText());
                    st.setString(3, Lname.getText());
                    st.setString(4, email.getText());
                    st.setString(5, (password.getText()));
                    st.setString(6, phone.getText());
                    st.setString(7, (address.getText()));
                    st.setString(8, (position.getText()));
                    st.setString(9, (photo.getText()));
                    st.setDate(10, Date.valueOf(Birthdate.getValue()));
                    st.setString(11, salary.getText());
                    st.setString(12, compName.getText());
                    st.executeUpdate();
                    connection.close();
                    flag1=true;
                }

            } catch (Exception e){
                JOptionPane.showMessageDialog(null,e);
            }
        }
        if(flag1){
            con.showAlert("Done", "Manage employees", "Employee added!", Alert.AlertType.INFORMATION);
        }
    }
    public void UpdateInfo() {
        Unit con=new Unit();
        boolean flag2=false;
        try{
            Connection connection=con.mySQLConnect();
            String sqlUpdate="update employees set PhoneNumber=?,Salary=?,Fname=?,Lname=?,Email=?" +
                    ",Password=?,Position=?,CompanyName=?,Address=?,Photo=?,Birthdate=? where ID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate);
            preparedStatement.setString(1,phone.getText());
            preparedStatement.setString(2,salary.getText());
            preparedStatement.setString(3,Fname.getText());
            preparedStatement.setString(4, Lname.getText());
            preparedStatement.setString(5, email.getText());
            preparedStatement.setString(6, password.getText());
            preparedStatement.setString(7, position.getText());
            preparedStatement.setString(8, compName.getText());
            preparedStatement.setString(9, address.getText());
            preparedStatement.setString(10, photo.getText());
            preparedStatement.setDate(11,Date.valueOf(Birthdate.getValue()));
            preparedStatement.setInt(12,Integer.parseInt(ID.getText()));
            preparedStatement.executeUpdate();
            connection.close();
            flag2=true;
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null,e);
        }
        if(flag2){
            con.showAlert("Done", "Manage employees", "Employee updated!", Alert.AlertType.INFORMATION);
        }
    }

    public void deleteEmp(){
        Unit con=new Unit();
        boolean flag3=false;
        try{
            Connection connection=con.mySQLConnect();
            Statement statement=connection.createStatement();
            String sqlDelete="delete from employees where ID="+ID.getText();
            statement.executeUpdate(sqlDelete);
            connection.close();
            flag3=true;
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null,e);
        }
        if(flag3){
            con.showAlert("Done", "Manage employees", "Employee deleted!", Alert.AlertType.INFORMATION);
        }
    }

    public void showProfile(ActionEvent event) throws IOException {
        profileController.id = LogInCVController.userID ;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("Eprofile.fxml"));
        stage.setScene(root.getScene());
        stage.setResizable(false);
    }

    public void LogOut(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("LogIn-CV.fxml"));
        stage.setScene(root.getScene());
        stage.setResizable(false);
    }

    public void home(ActionEvent event)throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("ManagerHomePage.fxml"));
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

            System.out.println(LogInCVController.companyID);
            searchStr = "select Name from company where ID = " + LogInCVController.companyID;
            check = s.executeQuery(searchStr);
            check.next() ;
            String cn = check.getString(1) ;
            System.out.println(cn);
            companyName.setText(cn);
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static int getEmpNum(){
        int num = 0 ;
        try {
            Unit u = new Unit();
            Connection con = u.mySQLConnect();
            Statement s = con.createStatement();
            String searchStr = "select count(*) from employees where companyName = '" + LogInCVController.companyNamee +"'";
            ResultSet check = s.executeQuery(searchStr);
            check.next();
            num = check.getInt(1);
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return num;
    }
}

