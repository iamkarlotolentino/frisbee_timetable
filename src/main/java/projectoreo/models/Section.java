package projectoreo.models;

public class Section {

  private int id;
  private String name;
  private int studentsCount;

  public Section() {
    this.id = -1;
    this.name = "<none>";
    this.studentsCount = 0;
  }

  public Section(int id, String name, int studentsCount) {
    this.id = id;
    this.name = name;
    this.studentsCount = studentsCount;
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

  public int getStudentsCount() {
    return studentsCount;
  }

  public void setStudentsCount(int studentsCount) {
    this.studentsCount = studentsCount;
  }

  @Override
  public String toString() {
    return name;
  }
}
