package projectoreo.database.queries;

import org.ini4j.Ini;
import projectoreo.database.DatabaseManager;
import projectoreo.models.Room;
import projectoreo.models.RoomType;
import projectoreo.utils.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomQueries {
  private DatabaseManager DB_MANAGER;
  private PreparedStatement ps;
  private ResultSet res;
  
  private Ini ROOM_INI;

  public RoomQueries(DatabaseManager DB_MANAGER) {
    this.DB_MANAGER = DB_MANAGER;
    try {
      ROOM_INI = new Ini(new File(ResourceLoader.getResource("sql/roomsql.ini").toURI()));
      DatabaseManager.LogUtils.successLoadIni("roomsql.ini");
    } catch (IOException | URISyntaxException e) {
      DatabaseManager.LogUtils.failLoadIni("roomsql.ini");
    }
  }

  public int create(Room room) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOM_INI.get("create", "create_room"));
    ps.setString(1, room.getName());
    ps.setInt(2, room.getType().getId());
    return ps.executeUpdate();
  }

  public ResultSet readAll() throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOM_INI.get("read", "read_all"));
    return ps.executeQuery();
  }

  public Room readById(int id) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOM_INI.get("read", "read_byid"));
    ps.setInt(1, id);
    res = ps.executeQuery();

    Room room = new Room();
    while (res.next()) {
      room.setId(id);
      room.setName(res.getString("name"));
      room.setType(DB_MANAGER.getRoomTypeQueries().readById(res.getInt("type_id")));
    }
    return room;
  }

  public ResultSet readByType(RoomType roomType) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOM_INI.get("read", "read_bytype"));
    ps.setInt(1, roomType.getId());
    return ps.executeQuery();
  }

  public int updateAllById(Room room) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOM_INI.get("update", "update_all_byid"));
    ps.setString(1, room.getName());
    ps.setInt(2, room.getType().getId());
    ps.setInt(3, room.getId());
    return ps.executeUpdate();
  }

  public int deleteAll() throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOM_INI.get("delete", "delete_all"));
    return ps.executeUpdate();
  }

  public int deleteById(int id) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOM_INI.get("delete", "delete_byid"));
    ps.setInt(1, id);
    return ps.executeUpdate();
  }

  public int deleteByName(String name) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOM_INI.get("delete", "delete_byname"));
    ps.setString(1, name);
    return ps.executeUpdate();
  }
} // -- End of RoomQueries
