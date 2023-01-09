package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

import java.sql.*;


public class Unit {

    private Connection con ;

    public void showAlert(String title,String header, String text, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public Connection mySQLConnect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/EMS", "root", "12345");
        }

        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return con;
    }
}
