import com.qut.cab302_project_pomodora.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SignInTest {

    private User testUser;

    @BeforeEach
    public void setup() {
        testUser = new User("John", "password123", 1, 0, "john@email.com");
    }

    @Test
    public void testLoginSuccess() {
        boolean result = testUser.login("john@email.com", "password123");
        assertTrue(result, "Login should succeed with correct credentials");
    }

    @Test
    public void testLoginFail_WrongPassword() {
        boolean result = testUser.login("john@email.com", "wrongPassword");
        assertFalse(result, "Login should fail with wrong password");
    }

    @Test
    public void testLoginFail_WrongEmail() {
        boolean result = testUser.login("wrong@email.com", "password123");
        assertFalse(result, "Login should fail with wrong email");
    }

    @Test
    public void testLoginFail_WrongEmailAndPassword(){
        boolean result = testUser.login("wrong@gmail.com", "wrongPassword");
        assertFalse(result,"Login with wrong email and password");
    }
}
