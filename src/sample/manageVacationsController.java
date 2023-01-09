package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.ResourceBundle;

public class manageVacationsController implements Initializable {
    public TableView<ListTableMV> MVTable;
    public TableColumn<ListTableMV,String> Name;
    public TableColumn<ListTableMV, String> StartDate;
    public TableColumn<ListTableMV,String> EndDate;
    public TableColumn<ListTableMV,String> Type;
    public TableColumn<ListTableMV,Integer> NumOfReqDays;
    public TableColumn<ListTableMV,Integer> NumOfRemDays;
    public TableColumn<ListTableMV,Integer> ID;
    public TextField search;
    public ImageView profilePhoto;
    public TextField companyName;
    public String cn;
    public static int SFlag;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
            cn = check.getString(1) ;
            System.out.println(cn);
            companyName.setText(cn);
;
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        StartDate.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
        EndDate.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
        Type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        NumOfReqDays.setCellValueFactory(new PropertyValueFactory<>("NumOfReqDays"));
        NumOfRemDays.setCellValueFactory(new PropertyValueFactory<>("NumOfRemDays"));
        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        if(search.getText().isEmpty()){
            getVacations();
        }
    }
    public ObservableList<ListTableMV> getVacations(){

        ObservableList<ListTableMV> VacationItem= FXCollections.observableArrayList();
        Unit con=new Unit();
        try {
            Connection connection=con.mySQLConnect();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from vacation where " +
                    "CompanyName='"+companyName.getText()+"' and Flag = 2;");
            PreparedStatement preparedStatement1=connection.prepareStatement("select Fname" +
                    ",Lname,RemVacDay from employees INNER JOIN vacation ON vacation.EmpID=employees.ID");
            System.out.println("0");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSet resultSet1 =preparedStatement1.executeQuery();
            while(resultSet.next() && resultSet1.next()){
                ListTableMV vacation=new ListTableMV();
                vacation.Name.set(resultSet1.getString("Fname")+" "+resultSet1.getString("Lname"));
                vacation.StartDate.set(resultSet.getDate("StartDate").toString());
                vacation.EndDate.set(resultSet.getDate("EndDate").toString());
                vacation.NumOfReqDays.set(resultSet.getInt("ReqVacDay"));
                vacation.NumOfRemDays.set(resultSet1.getFloat("RemVacDay"));
                vacation.ID.set(resultSet.getInt("id"));
                vacation.Type.set(resultSet.getString("Emergency"));
                vacation.flag.set(resultSet.getInt("Flag"));
                vacation.EmpID.set(resultSet.getInt("EmpID"));
                VacationItem.add(vacation);
            }

            MVTable.setItems(VacationItem);
            connection.close();
        }catch (Exception ex){
            System.out.println(ex);
        }
        return VacationItem;
    }
    public void accept(){
        Unit con=new Unit();
        Connection connection=con.mySQLConnect();
        PreparedStatement preparedStatement = null;
        Statement statement=null;
        Statement statement1=null;
        float remDays=0;
        Integer reqDays=0;
        try {

            if(MVTable.getSelectionModel().isEmpty()){
                con.showAlert("Done", "Manage vacations",
                        "Please select an employee from table!", Alert.AlertType.WARNING);
            }
            else{
                Integer idSelected = MVTable.getSelectionModel().getSelectedItem().getID();
                Integer idSelected1 = MVTable.getSelectionModel().getSelectedItem().getEmpID();
                statement = connection.createStatement();
                statement1 = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select ReqVacDay from vacation " +
                        "where id="+idSelected);
                ResultSet resultSet1 = statement1.executeQuery("select RemVacDay from employees " +
                        "where ID="+idSelected1);
                System.out.println(idSelected +" "+idSelected1);
                while (resultSet.next()){
                    reqDays=resultSet.getInt("ReqVacDay");
                }
                while (resultSet1.next()){
                    remDays=resultSet1.getFloat("RemVacDay");
                }
                System.out.println(remDays+"  "+reqDays);
                if(remDays<=14 && remDays>0){
                    preparedStatement = connection.prepareStatement("update vacation set Flag=1 where id="+idSelected);
                    preparedStatement.executeUpdate();
                    preparedStatement = connection.prepareStatement("update employees set RemVacDay=? where ID="+idSelected1);
                    remDays=remDays-reqDays;
                    preparedStatement.setFloat(1,remDays);
                    preparedStatement.executeUpdate();
                    System.out.println(remDays+"  "+reqDays);
                    con.showAlert("DONE", "Manage vacations",
                            "Vacation accepted!", Alert.AlertType.INFORMATION);
                }
                if(remDays==0){
                    con.showAlert("Sorry", "Manage vacations",
                            "Vacation days equal zero!", Alert.AlertType.WARNING);
                }
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public void refuse(){
        Unit con=new Unit();
        Connection connection=con.mySQLConnect();
        PreparedStatement preparedStatement = null;
        try {

            if(MVTable.getSelectionModel().isEmpty()){
                con.showAlert("Done", "Manage vacations",
                        "Please select an employee from table!", Alert.AlertType.WARNING);
            }
            else{
                Integer idSelected = MVTable.getSelectionModel().getSelectedItem().getID();
                preparedStatement = connection.prepareStatement("update vacation set Flag=0 where id="+idSelected);
                preparedStatement.executeUpdate();
                connection.close();
                con.showAlert("DONE", "Manage vacations",
                        "Vacation refused!", Alert.AlertType.INFORMATION);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void search(){
        Unit con=new Unit();
        Connection connection=con.mySQLConnect();
        boolean flag=false;
        String searchField=search.getText();
        String nameTable="";
        try {
            if(search.getText().isEmpty()){
                con.showAlert("Done", "Manage vacations",
                        "Please make sure to enter a name in search field!", Alert.AlertType.WARNING);
            }
            else{
                ObservableList<ListTableMV> VacationItem= FXCollections.observableArrayList();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from vacation where CompanyName ='"+cn+"'");
                PreparedStatement preparedStatement1=connection.prepareStatement("select Fname" +
                        ",Lname,RemVacDay from employees INNER JOIN vacation ON vacation.EmpID=employees.ID");

                        ResultSet resultSet = preparedStatement.executeQuery();
                        ResultSet resultSet1 =preparedStatement1.executeQuery();
                        while(resultSet.next() && resultSet1.next()){
                            if(search.getText().contains(resultSet1.getString("Fname")+" "+resultSet1.getString("Lname"))){
                                ListTableMV vacation=new ListTableMV();
                                vacation.Name.set(resultSet1.getString("Fname")+" "+resultSet1.getString("Lname"));
                                vacation.StartDate.set(resultSet.getDate("StartDate").toString());
                                vacation.EndDate.set(resultSet.getDate("EndDate").toString());
                                vacation.NumOfReqDays.set(resultSet.getInt("ReqVacDay"));
                                vacation.NumOfRemDays.set(resultSet1.getFloat("RemVacDay"));
                                vacation.ID.set(resultSet.getInt("id"));
                                vacation.Type.set(resultSet.getString("Emergency"));
                                vacation.flag.set(resultSet.getInt("Flag"));
                                vacation.EmpID.set(resultSet.getInt("EmpID"));
                                VacationItem.add(vacation);
                                flag=true;
                                break;
                            }
                        }
                        MVTable.setItems(VacationItem);
                if(!flag){
                    con.showAlert("Done", "Manage vacations",
                            "Not found!", Alert.AlertType.ERROR);
                }
            }

            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void close(){
        search.setText("");
        getVacations();
    }
    public void backHome(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("ManagerHomePage.fxml"));
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

    public static String acceptTest(int id) throws SQLException {
        Unit con=new Unit();
        Connection connection=con.mySQLConnect();
        PreparedStatement preparedStatement = null;
        Statement statement= connection.createStatement();
        try {
            Integer idSelected = id;
            preparedStatement = connection.prepareStatement("update vacation set Flag=1 where id="+idSelected);
            preparedStatement.executeUpdate();
            ResultSet rs = statement.executeQuery("select Flag from vacation where id = "+ idSelected);
            rs.next();
            SFlag = rs.getInt(1);
            connection.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        if(SFlag == 1){
            return "accepted" ;
        }
        else if(SFlag == 2){
            return "pending" ;
        }
        return "rejected" ;
    }

    public static String refuseTest(int id) throws SQLException {
        Unit con=new Unit();
        Connection connection=con.mySQLConnect();
        PreparedStatement preparedStatement = null;
        Statement statement= connection.createStatement();
        try {
            Integer idSelected = id;
            preparedStatement = connection.prepareStatement("update vacation set Flag=0 where id="+idSelected);
            preparedStatement.executeUpdate();
            ResultSet rs = statement.executeQuery("select Flag from vacation where id = "+ idSelected);
            rs.next();
            SFlag = rs.getInt(1);
            connection.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        if(SFlag == 0){
            return "rejected" ;
        }
        else if(SFlag == 2){
            return "pending" ;
        }
        return "accepted" ;
    }
}
