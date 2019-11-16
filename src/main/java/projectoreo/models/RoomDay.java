package projectoreo.models;

public class RoomDay {

    private int id;
    private String day;

    public RoomDay() {
        this.id = -1;
        this.day = "unspecified";
    }

    public RoomDay(int id, String day) {
        this.id = id;
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
