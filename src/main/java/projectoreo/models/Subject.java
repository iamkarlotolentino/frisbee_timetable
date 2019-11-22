package projectoreo.models;

public class Subject {

  private String id;
  private String name;
  private String desc;
  private RoomType type;
  private int sectionsUsed;

  public Subject() {
    this.id = "unspecified";
    this.name = "unspecified";
    this.desc = "unspecified";
    this.type = new RoomType();
    this.sectionsUsed = -1;
  }

  public Subject(String id, String name, String desc, RoomType type, int sectionsUsed) {
    this.id = id;
    this.name = name;
    this.desc = desc;
    this.type = type;
    this.sectionsUsed = sectionsUsed;
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

  public int getSectionsUsed() {
    return sectionsUsed;
  }

  public void setSectionsUsed(int sectionsUsed) {
    this.sectionsUsed = sectionsUsed;
  }

  @Override
  public String toString() {
    return name;
  }
}
