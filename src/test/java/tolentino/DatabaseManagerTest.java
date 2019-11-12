package tolentino;

import org.junit.jupiter.api.*;
import tolentino.orm.DatabaseManager;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseManagerTest {

    @BeforeAll
    static void beforeAllTest() {
        DatabaseManager db = DatabaseManager.getInstance();
    }

    @BeforeEach
    void beforeEachTest() {
    }

    @Test
    public void shouldBeConnected() {
        DatabaseManager db = DatabaseManager.getInstance();
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
