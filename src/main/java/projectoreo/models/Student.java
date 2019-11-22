package projectoreo.models;

public class Student {

  private String id;
  private String firstName;
  private String middleName;
  private String lastName;
  private Section sectionId;

  public Student() {
    this.id = "unspecified";
    this.firstName = "<none>";
    this.middleName = "<none>";
    this.lastName = "<none>";
    sectionId = new Section();
  }

  public Student(
      String id, String firstName, String middleName, String lastName, Section sectionId) {
    this.id = id;
    this.firstName = firstName;
    this.middleName = middleName;
    this.lastName = lastName;
    this.sectionId = sectionId;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Section getSectionId() {
    return sectionId;
  }

  public void setSectionId(Section sectionId) {
    this.sectionId = sectionId;
  }

  @Override
  public String toString() {
    return firstName + " " + middleName + " "+ lastName + " [" + id + "]";
  }
}
