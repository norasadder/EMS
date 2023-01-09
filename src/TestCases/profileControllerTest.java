package TestCases;
import org.junit.jupiter.api.Test;
import sample.profileController;

import static org.junit.jupiter.api.Assertions.*;
class profileControllerTest {
    @Test
    void searchTest() {
        var pc = new profileController() ;
        assertEquals("found",profileController.searchTest(110100,"Asal Company"));
        assertEquals("not found",profileController.searchTest(110100,"Paltel Company"));
        assertEquals("not found",profileController.searchTest(1100,"Paltel Company"));
    }
}