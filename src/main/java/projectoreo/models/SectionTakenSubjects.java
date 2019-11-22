package projectoreo.models;

public class SectionTakenSubjects {

    private int id;
    private int sectionId;
    private String subjectId;

    public SectionTakenSubjects() {
        this.id = -1;
        this.sectionId = -1;
        this.subjectId = "";
    }

    public SectionTakenSubjects(int id, int sectionId, String subjectId) {
        this.id = id;
        this.sectionId = sectionId;
        this.subjectId = subjectId;
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

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
