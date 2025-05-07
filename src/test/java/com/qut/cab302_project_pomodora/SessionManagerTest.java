package com.qut.cab302_project_pomodora;

import com.qut.cab302_project_pomodora.model.SessionManager;
import com.qut.cab302_project_pomodora.model.SqliteConnection;
import com.qut.cab302_project_pomodora.model.User;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SessionManagerTest {

    private static final String TEST_SESSION_FILE = "session.txt";
    private static Connection connection;

    // Set up the database connection and create a test database
    @BeforeAll
    static void setupDatabase() throws SQLException {
        // Create an in-memory SQLite DB or use a test-specific DB
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        connection.createStatement().executeUpdate("""
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                userName TEXT,
                playerLevel INTEGER,
                levelExp INTEGER,
                sessionToken TEXT
            )
        """);
        SqliteConnection.setTestInstance(connection);
    }

    @BeforeEach
    void clearState() throws IOException, SQLException {
        Files.deleteIfExists(Paths.get(TEST_SESSION_FILE));
        connection.createStatement().execute("DELETE FROM users");
        SessionManager.setCurrentUser(null);
    }


    /**
     * Test to ensure that the session file is created and the database is updated correctly when starting a session.
     * @throws Exception SQL exception if there is an error with the database
     */
    @Test
    void testStartSession() throws Exception {
        // Create a test user and insert it into the database
        User user = new User(1, "testUser", 1, 0);
        connection.createStatement().execute("INSERT INTO users (id, userName, playerLevel, levelExp) VALUES (1, 'testUser', 1, 0)");

        // Start the session
        SessionManager.startSession(user);

        assertTrue(Files.exists(Paths.get(TEST_SESSION_FILE)));

        // Check DB session token
        PreparedStatement stmt = connection.prepareStatement("SELECT sessionToken FROM users WHERE userName = ?");
        stmt.setString(1, "testUser");
        ResultSet rs = stmt.executeQuery();
        assertTrue(rs.next());
        assertNotNull(rs.getString("sessionToken"));
    }

    /**
     * Test to ensure that the correct session is loaded from the session file.
     * @throws Exception SQL exception if there is an error with the database
     */
    @Test
    void testLoadSessionCorrectUser() throws Exception {
        String fakeToken = UUID.randomUUID().toString();
        Files.write(Paths.get(TEST_SESSION_FILE), fakeToken.getBytes());
        connection.createStatement().execute("""
            INSERT INTO users (id, userName, playerLevel, levelExp, sessionToken)
            VALUES (1, 'testUser', 2, 50, '""" + fakeToken + "')");

        SessionManager.loadSession();
        User loaded = SessionManager.getCurrentUser();

        assertNotNull(loaded);
        assertEquals("testUser", loaded.getUserName());
        assertEquals(2, loaded.getPlayerLevel());
        assertEquals(50, loaded.getLevelExp());
        assertEquals(fakeToken, loaded.getSessionToken());
    }

    /**
     * Test to ensure that the session file is deleted if the token is invalid.
     * @throws Exception SQL exception if there is an error with the database
     */
    @Test
    void testLoadSessionInvalidToken() throws Exception {
        Files.write(Paths.get(TEST_SESSION_FILE), "invalid-token".getBytes());
        // Insert a user with a different session token
        SessionManager.loadSession();
        // Check that the session file is deleted
        assertNull(SessionManager.getCurrentUser());
        assertFalse(Files.exists(Paths.get(TEST_SESSION_FILE)));
    }

    /**
     * Test to ensure that the session is ended correctly, clearing the user and deleting the session file.
     * @throws Exception SQL exception if there is an error with the database
     */
    @Test
    void testEndSession_clearsUserAndDeletesFile() throws Exception {
        // Create a test user and insert it into the database
        User user = new User(1, "testUser", 1, 0);
        connection.createStatement().execute("INSERT INTO users (id, userName, playerLevel, levelExp, sessionToken) VALUES (1, 'testUser', 1, 0, 'abc123')");
        Files.write(Paths.get(TEST_SESSION_FILE), "abc123".getBytes());

        // Start the session
        SessionManager.setCurrentUser(user);
        SessionManager.endSession();

        // Check DB is cleared
        PreparedStatement stmt = connection.prepareStatement("SELECT sessionToken FROM users WHERE userName = ?");
        stmt.setString(1, "testUser");
        ResultSet rs = stmt.executeQuery();
        assertTrue(rs.next());
        assertNull(rs.getString("sessionToken"));

        // Check file deleted and user cleared
        assertNull(SessionManager.getCurrentUser());
        assertFalse(Files.exists(Paths.get(TEST_SESSION_FILE)));
    }

    /**
     * Tear down the database connection and reset the SqliteConnection instance.
     * @throws SQLException SQL exception if there is an error with the database
     */
    @AfterAll
    static void tearDown() throws SQLException {
        SqliteConnection.getInstance().close();
        SqliteConnection.reset();
    }

}
