package tolentino.orm;

public class Room {

    private int id;
    private RoomType type;
    private String name;

    public Room() {
        this.id = -1;
        this.type = null;
        this.name = "unspecified";
    }

    public Room(int id, RoomType type, String name) {
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

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
