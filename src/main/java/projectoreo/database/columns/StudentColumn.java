package projectoreo.database.columns;

public enum StudentColumn {
  STUDENT_ID(1),
  FIRST_NAME(2),
  MIDDLE_NAME(3),
  LAST_NAME(4),
  SECTION_ID(5);

  private int index;

  StudentColumn(int index) {
    this.index = index;
  }

  public int get() {
    return index;
  }
}
