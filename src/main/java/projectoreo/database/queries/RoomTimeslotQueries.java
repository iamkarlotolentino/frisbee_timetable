package projectoreo.database.queries;

import org.ini4j.Ini;
import projectoreo.database.DatabaseManager;
import projectoreo.models.RoomTimeslot;
import projectoreo.utils.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class RoomTimeslotQueries {
  private DatabaseManager DB_MANAGER;
  private PreparedStatement ps;
  private ResultSet res;
  
  private Ini ROOMTIMESLOT_INI;

  public RoomTimeslotQueries(DatabaseManager DB_MANAGER) {
    this.DB_MANAGER = DB_MANAGER;
    try {
      ROOMTIMESLOT_INI =
          new Ini(new File(ResourceLoader.getResource("sql/roomtimeslotsql.ini").toURI()));
      DatabaseManager.LogUtils.successLoadIni("roomtimeslotsql.ini");
    } catch (IOException | URISyntaxException e) {
      DatabaseManager.LogUtils.failLoadIni("roomtimeslotsql.ini");
    }
  }

  public int create(RoomTimeslot roomTimeslot) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMTIMESLOT_INI.get("create", "create_timeslot"));
    ps.setInt(1, roomTimeslot.getSectionId());
    ps.setInt(2, roomTimeslot.getRoomId());
    ps.setInt(3, roomTimeslot.getTimeId());
    ps.setInt(4, roomTimeslot.getDayId());
    return ps.executeUpdate();
  }

  public ResultSet readAll() throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMTIMESLOT_INI.get("read", "read_all"));
    return ps.executeQuery();
  }

  public RoomTimeslot readById(int id) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMTIMESLOT_INI.get("read", "read_all_byid"));
    ps.setInt(1, id);
    res = ps.executeQuery();

    RoomTimeslot roomTimeslot = new RoomTimeslot();
    while (res.next()) {
      roomTimeslot.setId(id);
      roomTimeslot.setSectionId(res.getInt("section__fk"));
      roomTimeslot.setRoomId(res.getInt("room__fk"));
      roomTimeslot.setTimeId(res.getInt("time__fk"));
      roomTimeslot.setDayId(res.getInt("day__fk"));
    }
    return roomTimeslot;
  }

  public ResultSet readBySection(int sectionId) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMTIMESLOT_INI.get("read", "read_bysection"));
    ps.setInt(1, sectionId);
    return ps.executeQuery();
  }

  public ResultSet readByTime(int timeId) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMTIMESLOT_INI.get("read", "read_bytime"));
    ps.setInt(1, timeId);
    return ps.executeQuery();
  }

  public ResultSet readByDay(int dayId) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMTIMESLOT_INI.get("read", "read_byday"));
    ps.setInt(1, dayId);
    return ps.executeQuery();
  }

  public int updateAllById(RoomTimeslot roomTimeslot) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMTIMESLOT_INI.get("update", "update_all_byid"));
    ps.setInt(1, roomTimeslot.getSectionId());
    ps.setInt(2, roomTimeslot.getRoomId());
    ps.setInt(3, roomTimeslot.getTimeId());
    ps.setInt(4, roomTimeslot.getDayId());
    ps.setInt(5, roomTimeslot.getId());
    return ps.executeUpdate();
  }

  public int deleteAll() throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMTIMESLOT_INI.get("delete", "delete_all"));
    return ps.executeUpdate();
  }

  public int deleteById(int id) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(ROOMTIMESLOT_INI.get("delete", "delete_byid"));
    ps.setInt(1, id);
    return ps.executeUpdate();
  }

  // TODO: Continue the other delete functions
} // -- End of RoomTimeslotQueries
