package tolentino.orm;

public class RoomType {

    class Entity {
        private int id;
        private String type;

        public Entity() {
            this.id = -1;
            this.type = "unspecified";
        }

        public Entity(int id, String type) {
            this.id = id;
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
