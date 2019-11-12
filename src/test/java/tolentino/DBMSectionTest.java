package tolentino;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("DatabaseManager:Section Test")
public class DBMSectionTest {

    @BeforeAll
    static void beforeAllTest() {
    }

    @BeforeEach
    void beforeEachTest() {
    }

    @Test
    public void test2() {
        System.out.println("test2");
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
