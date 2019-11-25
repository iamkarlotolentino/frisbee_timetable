package projectoreo.database.columns;

public enum RoomColumn {
  ROOM_ID(1),
  NAME(2),
  TYPE_ID(3);

  private int index;

  RoomColumn(int index) {
    this.index = index;
  }

  public int get() {
    return index;
  }
}
