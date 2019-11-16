package projectoreo.models;

public class RoomType {

    private int id;
    private String type;

    public RoomType() {
        this.id = -1;
        this.type = "unspecified";
    }

    public RoomType(int id, String type) {
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
