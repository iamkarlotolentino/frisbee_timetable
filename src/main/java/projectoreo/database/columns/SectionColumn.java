package projectoreo.database.columns;

public enum SectionColumn {
    SECTION_ID (1),
    NAME(2);

    private int index;
    SectionColumn(int index) {
        this.index = index;
    }

    public int get() {
        return index;
    }
}
