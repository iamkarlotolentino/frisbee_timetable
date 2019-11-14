package tolentino;

import org.junit.jupiter.api.*;
import tolentino.managers.DatabaseManager;
import tolentino.models.Section;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DatabaseManager:Section Test")
public class DBMSectionTest {

    @BeforeAll
    static void beforeAllTest() {
        DatabaseManager db = DatabaseManager.getInstance();
        db.defineSection();
    }

    @BeforeEach
    void beforeEachTest() {
        DatabaseManager db = DatabaseManager.getInstance();
        assertTrue(db.isConnected());
    }

    @Test
    public void shouldLocateSectionQueriesIni() {
        DatabaseManager.SectionQueries queries = new DatabaseManager.SectionQueries();
        assertNotNull(queries.getSECTION_INI());
    }

    @Test
    public void shouldCreateSection() {
        DatabaseManager.SectionQueries queries = new DatabaseManager.SectionQueries();
        try {
            assertTrue(queries.createSection(new Section(1, "Hello")) > 0);
        } catch (SQLException e) {
            assertTrue(false);
        }
    }

    @Test
    public void shouldReadAllSection() {
        // Creating initial section data
        DatabaseManager.SectionQueries queries = new DatabaseManager.SectionQueries();

        for (int i = 0; i < 5; i++) {
            try {
                assertTrue(queries.createSection(new Section(i, String.valueOf(i))) > 0);
            } catch (SQLException e) {
                // When fails to add a new section
                assertTrue(false);
            }
        }

        ResultSet res = null;
        try {
            res = queries.readSectionAll();
            assertNotNull(res, "The ResultSet received from #createSection(Section section is NULL");
        } catch (SQLException e) {
            assertTrue(false);
        }

        int i = 0;

        try {
            while (!res.next()) {
                assertTrue(res.getInt("section_id") == 1);
                assertTrue(res.getString("name").equals(String.valueOf(1)));
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @AfterEach
    void afterEachTest() {
        System.out.println("After each test");
    }

    @AfterAll
    static void afterAllTest() {
        System.out.println("After all test");
    }

}
