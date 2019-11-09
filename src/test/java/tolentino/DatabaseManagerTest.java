package tolentino;

import org.junit.Test;
import tolentino.orm.DatabaseManager;

import static junit.framework.TestCase.assertTrue;

public class DatabaseManagerTest {

    @Test
    public void shouldDatabaseConnect() {
        DatabaseManager db = DatabaseManager.getInstance();
        assertTrue(db.isConnected());
    }
}
