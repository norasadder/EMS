package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class showWorkingHoursController implements Initializable {
    @FXML
    private ImageView profilePhoto ;
    @FXML
    private TextField companyName;
    @FXML
    private TextField serachName;
    @FXML
    public TableView<ListTableWorkHours> workTable;
    public TableColumn<ListTableWorkHours,String> empName;
    public TableColumn<ListTableWorkHours, String> Date;
    public TableColumn<ListTableWorkHours,String> checkIn;
    public TableColumn<ListTableWorkHours,String> checkOut;
    private String cn;
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
            cn = check.getString(1) ;
            System.out.println(cn);
            companyName.setText(cn);
            if(serachName.getText().isEmpty()|| serachName.getText().equals("")){
                getWorkHours();
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        empName.setCellValueFactory(new PropertyValueFactory<>("empName"));
        Date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        checkIn.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
        checkOut.setCellValueFactory(new PropertyValueFactory<>("checkOut"));
    }
    public ObservableList<ListTableWorkHours> getWorkHours(){

        ObservableList<ListTableWorkHours> WorkItem= FXCollections.observableArrayList();
        Unit con=new Unit();
        try {
            Connection connection=con.mySQLConnect();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from workhours where companyName='"+cn+"'");
            PreparedStatement preparedStatement1=connection.prepareStatement("select Fname" +
                    ",Lname,RemVacDay from employees INNER JOIN workhours ON workhours.id=employees.ID");
            System.out.println("0");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSet resultSet1 =preparedStatement1.executeQuery();
            while(resultSet.next() && resultSet1.next()){
                ListTableWorkHours hours=new ListTableWorkHours();
                hours.empName.set(resultSet1.getString("Fname")+" "+resultSet1.getString("Lname"));
                hours.Date.set(resultSet.getDate("WorkDay").toString());
                hours.checkIn.set(resultSet.getTime("EntryTime").toString());
                hours.checkOut.set(resultSet.getTime("DepartureTime").toString());
                WorkItem.add(hours);
            }
            workTable.setItems(WorkItem);
            connection.close();
        }catch (Exception ex){
            System.out.println(ex);
        }
        return WorkItem;
    }
    public void search(){
        Unit con=new Unit();
        Connection connection=con.mySQLConnect();
        boolean flag=false;
        try {
            if(serachName.getText().isEmpty()){
                con.showAlert("Done", "Manage vacations",
                        "Please make sure to enter a name in search field!", Alert.AlertType.WARNING);
            }

            else{
                ObservableList<ListTableWorkHours> workHoursItem= FXCollections.observableArrayList();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from workhours where companyName='"+
                        cn+"'");
                PreparedStatement preparedStatement1=connection.prepareStatement("select Fname" +
                        ",Lname,RemVacDay from employees INNER JOIN workhours ON workhours.id=employees.ID");


                        ResultSet resultSet = preparedStatement.executeQuery();
                        ResultSet resultSet1 =preparedStatement1.executeQuery();
                        while(resultSet.next() && resultSet1.next()){
                            if(serachName.getText().equals(resultSet1.getString("Fname")+" "+resultSet1.getString("Lname"))){
                                ListTableWorkHours hours=new ListTableWorkHours();
                                hours.empName.set(resultSet1.getString("Fname")+" "+resultSet1.getString("Lname"));
                                hours.Date.set(resultSet.getDate("WorkDay").toString());
                                hours.checkIn.set(resultSet.getTime("EntryTime").toString());
                                hours.checkOut.set(resultSet.getTime("DepartureTime").toString());
                                hours.companyName.set(resultSet.getString("companyName"));
                                workHoursItem.add(hours);
                                flag=true;
                                break;
                            }

                        }
                        workTable.setItems(workHoursItem);

                if(!flag){
                    con.showAlert("Done", "Working hours",
                            "Not found!", Alert.AlertType.ERROR);
                }
            }

            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void close(){
        serachName.setText("");
        getWorkHours();
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
}
