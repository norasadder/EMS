package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class showUploadedCVsController implements Initializable {
    @FXML
    private TableView <CVsTable> CVsTableView ;
    @FXML
    private TableColumn <CVsTable, ImageView> CVsColumn ;

    private String n = LogInCVController.companyNamee ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            CVsColumn.setCellValueFactory(new PropertyValueFactory<CVsTable,ImageView>("CV"));
            ObservableList<CVsTable> CVsList = FXCollections.observableArrayList() ;
            Unit u = new Unit();
            Connection con = u.mySQLConnect();
            Statement s = con.createStatement();
            System.out.println(n);
            String searchStr = "select CV from cv where CompanyName = '"+n+"';";
            ResultSet check = s.executeQuery(searchStr);

            while(check.next()) {
                ImageView iv = new ImageView();
                Blob in = check.getBlob(1);
                System.out.println(in);
                InputStream is = in.getBinaryStream();
                Image img = new Image(is);
                iv.setImage(img);
                CVsList.add(new CVsTable(iv));
            }
            CVsTableView.setItems(CVsList);
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void Home(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage root = FXMLLoader.load(getClass().getResource("ManagerHomePage.fxml"));
        stage.setScene(root.getScene());
        stage.setResizable(false);
    }
}
