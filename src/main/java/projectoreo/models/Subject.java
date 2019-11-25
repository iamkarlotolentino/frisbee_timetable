package projectoreo.models;

public class Subject {

  private String id;
  private String name;
  private int requiredHours;
  private int units;
  private RoomType type;

  private int sectionsUsed;

  public Subject() {}

  public Subject(
      String id, String name, int requiredHours, int units, RoomType type, int sectionsUsed) {
    this.id = id;
    this.name = name;
    this.requiredHours = requiredHours;
    this.units = units;
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

  public int getRequiredHours() {
    return requiredHours;
  }

  public void setRequiredHours(int requiredHours) {
    this.requiredHours = requiredHours;
  }

  public int getUnits() {
    return units;
  }

  public void setUnits(int units) {
    this.units = units;
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
