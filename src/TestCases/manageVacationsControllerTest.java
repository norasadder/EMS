package TestCases;

import org.junit.jupiter.api.Test;
import sample.manageVacationsController;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class manageVacationsControllerTest {

    @Test
    void acceptTest() throws SQLException {
        var mvc = new manageVacationsController() ;
        assertEquals("accepted",manageVacationsController.acceptTest(1));
    }

    @Test
    void refuseTest() throws SQLException {
        var mvc = new manageVacationsController() ;
        assertEquals("rejected",manageVacationsController.refuseTest(1));
    }
}