package projectoreo.models;

public class Subject {

  private String id;
  private String name;
  private String desc;
  private RoomType type;

  public Subject() {
    this.id = "unspecified";
    this.name = "unspecified";
    this.desc = "unspecified";
    this.type = new RoomType();
  }

  public Subject(String id, String name, String desc, RoomType type) {
    this.id = id;
    this.name = name;
    this.desc = desc;
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
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

  public RoomType getType() {
    return type;
  }

  public void setType(RoomType type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "Subject{"
        + "id='"
        + id
        + '\''
        + ", name='"
        + name
        + '\''
        + ", desc='"
        + desc
        + '\''
        + ", type="
        + type
        + '}';
  }
}
