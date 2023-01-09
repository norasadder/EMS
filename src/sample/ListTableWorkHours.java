package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ListTableWorkHours {
    public SimpleStringProperty empName = new SimpleStringProperty();
    public SimpleStringProperty Date = new SimpleStringProperty();
    public SimpleStringProperty checkIn = new SimpleStringProperty();
    public SimpleStringProperty checkOut = new SimpleStringProperty();

    public String getCompanyName() {
        return companyName.get();
    }

    public SimpleStringProperty companyNameProperty() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }

    public SimpleStringProperty companyName = new SimpleStringProperty();

    public String getEmpName() {
        return empName.get();
    }

    public SimpleStringProperty empNameProperty() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName.set(empName);
    }

    public String getDate() {
        return Date.get();
    }

    public SimpleStringProperty dateProperty() {
        return Date;
    }

    public void setDate(String date) {
        this.Date.set(date);
    }

    public String getCheckIn() {
        return checkIn.get();
    }

    public SimpleStringProperty checkInProperty() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn.set(checkIn);
    }

    public String getCheckOut() {
        return checkOut.get();
    }

    public SimpleStringProperty checkOutProperty() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut.set(checkOut);
    }
}
