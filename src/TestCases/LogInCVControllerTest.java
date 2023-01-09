package TestCases;

import org.junit.jupiter.api.Test;
import sample.LogInCVController;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LogInCVControllerTest {

    @Test
    void logInn() throws IOException {
        var lc = new LogInCVController() ;
        assertEquals("ID not found",LogInCVController.logInn(11111,""));
        assertEquals("logged in successfully",LogInCVController.logInn(110100,"Mona@123"));
        assertEquals("incorrect password",LogInCVController.logInn(110100,"Mona"));
    }
}