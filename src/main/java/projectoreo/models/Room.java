package projectoreo.models;

public class Room {

    private int id;
    private RoomType type;
    private String name;

    public Room() {
        this.id = -1;
        this.type = new RoomType();
        this.name = "";
    }

    public Room(int id, String name, RoomType type) {
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
