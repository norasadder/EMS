package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

import java.time.LocalDate;

public class CVsTable {

    public ImageView CV ;
    public CVsTable(ImageView CV) {
        this.CV = CV ;
    }

    public ImageView getCV () {
        return CV ;
    }

}
