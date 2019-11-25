package projectoreo.database.queries;

import org.ini4j.Ini;
import projectoreo.database.DatabaseManager;
import projectoreo.models.RoomTime;
import projectoreo.utils.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class RoomTimeQueries {
  private DatabaseManager DB_MANAGER;
  private PreparedStatement ps;
  private ResultSet res;
  
  private Ini ROOMTIME_INI;

  public RoomTimeQueries(DatabaseManager DB_MANAGER) {
    this.DB_MANAGER = DB_MANAGER;
    try {
      ROOMTIME_INI = new Ini(new File(ResourceLoader.getResource("sql/roomtimesql.ini").toURI()));
      DatabaseManager.LogUtils.successLoadIni("roomtimesql.ini");
    } catch (IOException | URISyntaxException e) {
      DatabaseManager.LogUtils.failLoadIni("roomtimesql.ini");
    }
  }

  public int create(RoomTime roomTime) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMTIME_INI.get("create", "create_roomtime"));
    ps.setString(1, roomTime.getTime());
    return ps.executeUpdate();
  }

  public ResultSet readAll() throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMTIME_INI.get("read", "read_all"));
    return ps.executeQuery();
  }

  public RoomTime readById(int id) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMTIME_INI.get("read", "read_all_byid"));
    ps.setInt(1, id);
    res = ps.executeQuery();

    RoomTime roomTime = new RoomTime();
    while (res.next()) {
      roomTime.setId(res.getInt("id"));
      roomTime.setTime(res.getString("time"));
    }
    return roomTime;
  }

  public int updateTimeById(RoomTime roomTime) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMTIME_INI.get("update", "update_time_byid"));
    ps.setString(1, roomTime.getTime());
    ps.setInt(2, roomTime.getId());
    return ps.executeUpdate();
  }

  public int deleteAll() throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMTIME_INI.get("delete", "delete_all"));
    return ps.executeUpdate();
  }

  public int deleteById(int id) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMTIME_INI.get("delete", "delete_byid"));
    ps.setInt(1, id);
    return ps.executeUpdate();
  }
} // -- End of RoomTime Queries
