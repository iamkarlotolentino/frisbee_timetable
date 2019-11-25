package projectoreo.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.ini4j.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import projectoreo.database.queries.*;
import projectoreo.utils.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

  public static Connection con = null;
  private static Logger DB_LOGGER = LoggerFactory.getLogger(DatabaseManager.class);
  private static DatabaseManager instance;
  private static PreparedStatement ps = null;
  private static ResultSet res = null;
  private Ini DDL_INI;
  private StudentQueries studentQueries = new StudentQueries(this);
  private RoomTypeQueries roomTypeQueries = new RoomTypeQueries(this);
  private SubjectQueries subjectQueries = new SubjectQueries(this);
  private SectionQueries sectionQueries = new SectionQueries(this);
  private TakenSubjectsQueries takenSubjectsQueries = new TakenSubjectsQueries(this);
  private RoomQueries roomQueries = new RoomQueries(this);

  private DatabaseManager() {
    // If not yet connected, then connect
    String propertiesPath = ResourceLoader.getResource("db.properties").getPath();
    if (con == null) {
      connect(propertiesPath.substring(1));
    }
    // TODO: Preferences if not yet defined tables
    if (false) {
      try {
        DDL_INI = new Ini(new File(ResourceLoader.getResource("sql/ddl.ini").toURI()));

      } catch (IOException | URISyntaxException e) {
        DB_LOGGER.error("The ddl SQL file is not found.");
      }

      defineStudent();
      defineSection();
      defineRoom();
      defineRoomType();
      defineRoomDay();
      defineRoomTime();
      defineRoomTimeslot();
      defineSubject();
      defineSectionTakenSubjects();
    }
  }

  public static DatabaseManager getInstance() {
    if (instance == null) instance = new DatabaseManager();
    return instance;
  }

  public TakenSubjectsQueries getTakenSubjectsQueries() {
    return takenSubjectsQueries;
  }

  // Pass the properties file URL
  private void connect(String url) {
    HikariConfig config = new HikariConfig(url);
    HikariDataSource dataSource = new HikariDataSource(config);

    try {
      Class.forName("org.sqlite.SQLiteDataSource");
      con = dataSource.getConnection();
    } catch (SQLException | ClassNotFoundException e) {
      con = null;
      DB_LOGGER.error("The connection was not authenticated.");
    }
  }

  public void disconnect() throws SQLException {
    con.close();
  }

  public boolean defineSection() {
    try {
      ps = con.prepareStatement(DDL_INI.get("ddl", "define_section"));
      int state = ps.executeUpdate();
      if (state > 0) {
        LogUtils.createTableSuccess("section");
        return true;
      }
    } catch (SQLException e) {
      LogUtils.createTableFailed("section");
    }
    return false;
  }

  public boolean defineSubject() {
    try {
      ps = con.prepareStatement(DDL_INI.get("ddl", "define_subject"));
      int state = ps.executeUpdate();
      if (state > 0) {
        LogUtils.createTableSuccess("subject");
        return true;
      }
    } catch (SQLException e) {
      LogUtils.createTableFailed("subject");
    }
    return false;
  }

  public boolean defineRoomType() {
    try {
      ps = con.prepareStatement(DDL_INI.get("ddl", "define_room_type"));
      int state = ps.executeUpdate();
      if (state > 0) {
        LogUtils.createTableSuccess("room_type");
        return true;
      }
    } catch (SQLException e) {
      LogUtils.createTableFailed("room_type");
    }
    return false;
  }

  public boolean defineRoomTime() {
    try {
      ps = con.prepareStatement(DDL_INI.get("ddl", "define_room_time"));
      int state = ps.executeUpdate();
      if (state > 0) {
        LogUtils.createTableSuccess("room_time");
        return true;
      }
    } catch (SQLException e) {
      LogUtils.createTableFailed("room_time");
    }
    return false;
  }

  public boolean defineRoomTimeslot() {
    try {
      ps = con.prepareStatement(DDL_INI.get("ddl", "define_room_timeslot"));
      int state = ps.executeUpdate();
      if (state > 0) {
        LogUtils.createTableSuccess("room_timeslot");
        return true;
      }
    } catch (SQLException e) {
      LogUtils.createTableFailed("room_timeslot");
    }
    return false;
  }

  public boolean defineRoomDay() {
    try {
      ps = con.prepareStatement(DDL_INI.get("ddl", "define_room_day"));
      int state = ps.executeUpdate();
      if (state > 0) {
        LogUtils.createTableSuccess("room_day");
        return true;
      }
    } catch (SQLException e) {
      LogUtils.createTableFailed("room_day");
    }
    return false;
  }

  public boolean defineRoom() {
    try {
      ps = con.prepareStatement(DDL_INI.get("ddl", "define_room"));
      int state = ps.executeUpdate();
      if (state > 0) {
        LogUtils.createTableSuccess("room");
        return true;
      }
    } catch (SQLException e) {
      LogUtils.createTableFailed("room");
    }
    return false;
  }

  public boolean defineStudent() {
    try {
      ps = con.prepareStatement(DDL_INI.get("ddl", "define_student"));
      int state = ps.executeUpdate();
      if (state > 0) {
        LogUtils.createTableSuccess("student");
        return true;
      }
    } catch (SQLException e) {
      LogUtils.createTableFailed("student");
    }
    return false;
  }

  public boolean defineSectionTakenSubjects() {
    try {
      ps = con.prepareStatement(DDL_INI.get("ddl", "define_section_taken_subjects"));
      int state = ps.executeUpdate();
      if (state > 0) {
        LogUtils.createTableSuccess("taken_subjects");
        return true;
      }
    } catch (SQLException e) {
      LogUtils.createTableFailed("taken_subjects");
    }
    return false;
  }

  public boolean isConnected() {
    return (con != null);
  }

  public StudentQueries getStudentQueries() {
    return studentQueries;
  }

  public SubjectQueries getSubjectQueries() {
    return subjectQueries;
  }

  public SectionQueries getSectionQueries() {
    return sectionQueries;
  }

  public RoomTypeQueries getRoomTypeQueries() {
    return roomTypeQueries;
  }

  public RoomQueries getRoomQueries() {
    return roomQueries;
  }

  public static class LogUtils {
    public static void failLoadIni(String fileName) {
      DB_LOGGER.error(
          "(" + fileName + ") is not found or failed to load. Requires immediate action.");
    }

    public static void successLoadIni(String fileName) {
      DB_LOGGER.info("(" + fileName + ") has been successfully loaded.");
    }

    public static void createTableSuccess(String tableName) {
      DB_LOGGER.info("([" + tableName + "] has been created");
    }

    public static void createTableFailed(String tableName) {
      DB_LOGGER.error("[" + tableName + "] exists.");
    }
  }
}
