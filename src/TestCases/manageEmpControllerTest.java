package TestCases;

import org.junit.Test;
import sample.LogInCVController;
import sample.manageEmpController;

import static org.junit.jupiter.api.Assertions.*;

class manageEmpControllerTest {
    @org.junit.jupiter.api.Test
    void testGetEmpNum() {
        var mec = new manageEmpController() ;
        int flag = manageEmpController.getEmpNum();
        LogInCVController.companyNamee = "Asal Company";
        assertEquals(8,manageEmpController.getEmpNum());

        LogInCVController.companyNamee = "Paltel Company";
        assertEquals(10,manageEmpController.getEmpNum());

        LogInCVController.companyNamee = "Northern electricity Company";
        assertEquals(10,manageEmpController.getEmpNum());

        LogInCVController.companyNamee = "Sbitany Company";
        assertEquals(10,manageEmpController.getEmpNum());

        LogInCVController.companyNamee = "Exalt Company";
        assertEquals(10,manageEmpController.getEmpNum());

        LogInCVController.companyNamee = "Infinite Company";
        assertEquals(10,manageEmpController.getEmpNum());

        LogInCVController.companyNamee = "Al-Jebrini Company";
        assertEquals(10,manageEmpController.getEmpNum());
    }
}