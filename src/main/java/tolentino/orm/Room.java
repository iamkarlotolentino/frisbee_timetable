package tolentino.orm;

public class Room {

    class Entity {
        private int id;
        private RoomType.Entity type;
        private String name;

        public Entity() {
            this.id = -1;
            this.type = null;
            this.name = "unspecified";
        }

        public Entity(int id, RoomType.Entity type, String name) {
            this.id = id;
            this.type = type;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public RoomType.Entity getType() {
            return type;
        }

        public void setType(RoomType.Entity type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
