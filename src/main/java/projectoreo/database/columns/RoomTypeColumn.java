package projectoreo.database.columns;

public enum RoomTypeColumn {
  TYPE_ID(1),
  NAME(2);

  private int index;

  RoomTypeColumn(int index) {
    this.index = index;
  }

  public int get() {
    return index;
 }
}
