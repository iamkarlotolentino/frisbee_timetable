package tolentino;

import org.junit.jupiter.api.*;
import tolentino.managers.DatabaseManager;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseManagerTest {

    @BeforeAll
    static void beforeAllTest() {
        DatabaseManager db = DatabaseManager.getInstance();
        assertTrue(db.isConnected());
        assertNotNull(db.getDDL_INI());
    }

    @BeforeEach
    void beforeEachTest() {
        assertTrue(DatabaseManager.getInstance().isConnected());
    }

    @Test
    public void shouldBeConnected() {
        assertTrue(DatabaseManager.getInstance().isConnected());
    }

    @Test
    public void shouldNotNull() {
        assertNotNull(DatabaseManager.getInstance().getDDL_INI());
    }

    @AfterEach
    void afterEachTest() {
    }

    @AfterAll
    static void afterAllTest() {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            db.disconnect();
        } catch (SQLException e) {
        }
    }

}
