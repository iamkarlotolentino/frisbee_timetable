package projectoreo.models;

public class Room {

    private int id;
    private int type;
    private String name;

    public Room() {
        this.id = -1;
        this.type = -1;
        this.name = "unspecified";
    }

    public Room(int id, int type, String name) {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
