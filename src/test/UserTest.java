import com.qut.cab302_project_pomodora.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    private static final String username = "John Doe";
    private static final String password = "passwd123";
    private static final int userLevel = 1;
    private static final int userLevelExp = 1;
    private static final String userEmail = "johndoe@email.com";

    private User user1;

    @BeforeEach
    public void setUp() {
        user1 = new User(username,password,userLevel,userLevelExp,userEmail);
    }

    @Test
    public void testSetAndGetUsername() {
        user1.setUserName("Ninten Doe");
        assertEquals("Ninten Doe", user1.getUserName());
    }

    @Test
    public void testSetAndGetID() {
        user1.setId(64);
        assertEquals(64, user1.getId());
    }

    @Test
    public void testSetAndGetEmail() {
        user1.setEmail("nintendoe64@email.com");
        assertEquals("nintendoe64@email.com", user1.getEmail());
    }

    @Test
    public void testSetAndGetPassword(){
        user1.setPassword("ToughPassword64");
        assertEquals("ToughPassword64", user1.getPassword());
    }

    @Test
    public void testSetAndGetPlayerLevel() {
        user1.setPlayerLevel(2);
        assertEquals(2,user1.getPlayerLevel());
    }


}
