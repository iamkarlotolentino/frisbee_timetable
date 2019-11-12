package tolentino.models;

public class Subject {

    private int id;
    private String name;
    private String desc;
    private int type;

    public Subject() {
        this.id = -1;
        this.name = "unspecified";
        this.desc = "unspecified";
        this.type = -1;
    }

    public Subject(int id, String name, String desc, int type) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.type = type;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
