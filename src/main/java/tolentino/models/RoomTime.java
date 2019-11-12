package tolentino.models;

public class RoomTime {

    private int id;
    private String time;

    public RoomTime() {
        this.id = -1;
        this.time = "unspecified";
    }

    public RoomTime(int id, String time) {
        this.id = id;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
