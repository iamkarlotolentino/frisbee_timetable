package projectoreo.database.queries;

import org.ini4j.Ini;
import projectoreo.database.DatabaseManager;
import projectoreo.models.RoomType;
import projectoreo.utils.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomTypeQueries {
  private DatabaseManager DB_MANAGER;
  private PreparedStatement ps;
  private ResultSet res;
  
  private Ini ROOMTYPE_INI;

  public RoomTypeQueries(DatabaseManager DB_MANAGER) {
    this.DB_MANAGER = DB_MANAGER;
    try {
      ROOMTYPE_INI = new Ini(new File(ResourceLoader.getResource("sql/roomtypesql.ini").toURI()));
      DatabaseManager.LogUtils.successLoadIni("roomtypesql.ini");
    } catch (IOException | URISyntaxException e) {
      DatabaseManager.LogUtils.failLoadIni("roomtypesql.ini");
    }
  }

  public int create(RoomType roomType) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMTYPE_INI.get("create", "create_roomtype"));
    ps.setString(1, roomType.getType());
    return ps.executeUpdate();
  }

  public ResultSet readAll() throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMTYPE_INI.get("read", "read_all"));
    return ps.executeQuery();
  }

  public RoomType readById(int id) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMTYPE_INI.get("read", "read_byid"));
    ps.setInt(1, id);
    res = ps.executeQuery();

    RoomType roomType = new RoomType();
    while (res.next()) {
      roomType.setId(id);
      roomType.setType(res.getString("name"));
    }
    return roomType;
  }

  public int updateById(RoomType roomType) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMTYPE_INI.get("update", "update_name_byid"));
    ps.setString(1, roomType.getType());
    ps.setInt(2, roomType.getId());
    return ps.executeUpdate();
  }

  public int deleteAll() throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMTYPE_INI.get("delete", "delete_all"));
    return ps.executeUpdate();
  }

  public int deleteById(int id) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMTYPE_INI.get("delete", "delete_byid"));
    ps.setInt(1, id);
    return ps.executeUpdate();
  }
} // -- End of RoomTypeQueries
