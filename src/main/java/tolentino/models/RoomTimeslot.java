package tolentino.models;

public class RoomTimeslot {

    private int id;
    private int sectionId;
    private int roomId;
    private int timeId;
    private int dayId;

    public RoomTimeslot() {
        this.id = -1;
        this.sectionId = -1;
        this.roomId = -1;
        this.timeId = -1;
        this.dayId = -1;
    }

    public RoomTimeslot(int id, int sectionId, int roomId, int timeId, int dayId) {
        this.id = id;
        this.sectionId = sectionId;
        this.roomId = roomId;
        this.timeId = timeId;
        this.dayId = dayId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getTimeId() {
        return timeId;
    }

    public void setTimeId(int timeId) {
        this.timeId = timeId;
    }

    public int getDayId() {
        return dayId;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }
}
