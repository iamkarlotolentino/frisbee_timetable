package projectoreo.database.columns;

public enum SubjectColumn {
    SUBJECT_ID (1),
    NAME(2),
    UNITS(3),
    REQUIRED_HOURS (4),
    TYPE_ID(5);

  private int index;

  SubjectColumn(int index) {
    this.index = index;
  }

  public int get() {
    return index;
  }
}
