package projectoreo.models;

public class Student {

    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private int sectionId;

    public Student() {
    this.id = "unspecified";
        this.firstName = "<none>";
        this.middleName = "<none>";
        this.lastName = "<none>";
        sectionId = -1;
    }

    public Student(String id, String firstName, String middleName, String lastName, int sectionId) {
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

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", sectionId=" + sectionId +
                '}';
    }
}
