package sample;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class ListTableMV {
    public SimpleStringProperty Name = new SimpleStringProperty();
    public SimpleStringProperty StartDate = new SimpleStringProperty();
    public SimpleStringProperty EndDate = new SimpleStringProperty();
    public SimpleIntegerProperty NumOfReqDays = new SimpleIntegerProperty();
    public SimpleStringProperty Type = new SimpleStringProperty();
    public SimpleFloatProperty NumOfRemDays = new SimpleFloatProperty();
    public SimpleIntegerProperty ID = new SimpleIntegerProperty();
    public SimpleIntegerProperty flag = new SimpleIntegerProperty();

    public int getEmpID() {
        return EmpID.get();
    }

    public SimpleIntegerProperty empIDProperty() {
        return EmpID;
    }

    public void setEmpID(int empID) {
        this.EmpID.set(empID);
    }

    public SimpleIntegerProperty EmpID = new SimpleIntegerProperty();

    public void setName(String name) {
        this.Name.set(name);
    }

    public String getStartDate() {
        return StartDate.get();
    }

    public SimpleStringProperty startDateProperty() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        this.StartDate.set(startDate);
    }

    public String getEndDate() {
        return EndDate.get();
    }

    public SimpleStringProperty endDateProperty() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        this.EndDate.set(endDate);
    }

    public void setNumOfReqDays(int numOfReqDays) {
        this.NumOfReqDays.set(numOfReqDays);
    }

    public void setType(String type) {
        this.Type.set(type);
    }

    public void setNumOfRemDays(int numOfRemDays) {
        this.NumOfRemDays.set(numOfRemDays);
    }

    public void setID(int ID) {
        this.ID.set(ID);
    }

    public String getName() {
        return Name.get();
    }

    public SimpleStringProperty nameProperty() {
        return Name;
    }

    public int getNumOfReqDays() {
        return NumOfReqDays.get();
    }

    public SimpleIntegerProperty numOfReqDaysProperty() {
        return NumOfReqDays;
    }

    public String getType() {
        return Type.get();
    }

    public SimpleStringProperty typeProperty() {
        return Type;
    }

    public float getNumOfRemDays() {
        return NumOfRemDays.get();
    }

    public SimpleFloatProperty numOfRemDaysProperty() {
        return NumOfRemDays;
    }

    public int getID() {
        return ID.get();
    }

    public SimpleIntegerProperty IDProperty() {
        return ID;
    }
}
