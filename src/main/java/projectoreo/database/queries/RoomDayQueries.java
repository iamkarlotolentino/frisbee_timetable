package projectoreo.database.queries;

import org.ini4j.Ini;
import projectoreo.database.DatabaseManager;
import projectoreo.models.RoomDay;
import projectoreo.utils.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class RoomDayQueries {
  private DatabaseManager DB_MANAGER;
  private PreparedStatement ps;
  private ResultSet res;
  
  private Ini ROOMDAY_INI;

  public RoomDayQueries(DatabaseManager DB_MANAGER) {
    this.DB_MANAGER = DB_MANAGER;
    try {
      ROOMDAY_INI = new Ini(new File(ResourceLoader.getResource("sql/roomdaysql.ini").toURI()));
      DatabaseManager.LogUtils.successLoadIni("roomdaysql.ini");
    } catch (IOException | URISyntaxException e) {
      DatabaseManager.LogUtils.failLoadIni("roomdaysql.ini");
    }
  }

  public int create(RoomDay roomDay) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMDAY_INI.get("create", "create_roomday"));
    ps.setString(1, roomDay.getDay());
    return ps.executeUpdate();
  }

  public ResultSet reallAll() throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMDAY_INI.get("read", "read_all"));
    return ps.executeQuery();
  }

  public RoomDay readById(int id) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMDAY_INI.get("read", "read_byid"));
    ps.setInt(1, id);
    res = ps.executeQuery();

    RoomDay roomDay = new RoomDay();
    while (res.next()) {
      roomDay.setId(id);
      roomDay.setDay(res.getString("text"));
    }
    return roomDay;
  }

  public int updateById(RoomDay roomDay) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMDAY_INI.get("update", "update_byid"));
    ps.setString(1, roomDay.getDay());
    ps.setInt(2, roomDay.getId());
    return ps.executeUpdate();
  }

  public int deleteAll() throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMDAY_INI.get("delete", "delete_all"));
    return ps.executeUpdate();
  }

  public int deleteById(int id) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMDAY_INI.get("delete", "delete_byid"));
    ps.setInt(1, id);
    return ps.executeUpdate();
  }
} // -- End of RoomDay Queries
