package tolentino.orm;

public class Section {

    private DatabaseManager db = DatabaseManager.getInstance();



    class Entity {

        private int id;
        private String name;

        public Entity() {
            this.id = -1;
            this.name = "not specified";
        }

        public Entity(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
