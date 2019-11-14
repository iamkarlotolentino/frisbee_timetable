package tolentino;

import tolentino.managers.DatabaseManager;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        DatabaseManager db = DatabaseManager.getInstance();
        System.out.println(db.isConnected());
    }
}
