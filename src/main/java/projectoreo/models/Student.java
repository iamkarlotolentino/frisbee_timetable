package projectoreo.models;

public class Student {

    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private int sectionId;

    public Student() {
        this.id = -1;
        this.firstName = "unspecified";
        this.middleName = "unspecified";
        this.lastName = "unspecified";
        sectionId = -1;
    }

    public Student(int id, String firstName, String middleName, String lastName, int sectionId) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.sectionId = sectionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
