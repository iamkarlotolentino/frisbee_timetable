package projectoreo.database.columns;

public enum TakenSubjectsColumn {
    TAKEN_ID (1),
    SECTION_ID(2),
    SUBJECT_ID(3);

    private int index;

    TakenSubjectsColumn(int index) {
        this.index = index;
    }

    public int get() {
        return index;
    }
}
