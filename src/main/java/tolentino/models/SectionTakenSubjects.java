package tolentino.models;

public class SectionTakenSubjects {

    private int id;
    private int sectionId;
    private int subjectId;

    public SectionTakenSubjects() {
        this.id = -1;
        this.sectionId = -1;
        this.subjectId = -1;
    }

    public SectionTakenSubjects(int id, int sectionId, int subjectId) {
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

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
}
