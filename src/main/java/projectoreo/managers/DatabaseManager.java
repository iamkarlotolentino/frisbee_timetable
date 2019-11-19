package projectoreo.managers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.ini4j.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import projectoreo.models.*;
import projectoreo.utils.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

  private static Logger DB_LOGGER = LoggerFactory.getLogger(DatabaseManager.class);

  private static DatabaseManager instance;

  private static Connection con = null;
  private static PreparedStatement ps = null;
  private static ResultSet res = null;

  private StudentQueries studentQueries = new StudentQueries();
  private RoomTypeQueries roomTypeQueries = new RoomTypeQueries();
  private SubjectQueries subjectQueries = new SubjectQueries();
  private SectionQueries sectionQueries = new SectionQueries();

  private Ini DDL_INI;

  private DatabaseManager() {
    // If not yet connected, then connect
    String propertiesPath = ResourceLoader.getResource("db.properties").getPath();
    if (con == null) {
      connect(propertiesPath.substring(1, propertiesPath.length()));
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

  // --------- Data Definition Language
  public boolean defineSection() {
    try {
      ps = con.prepareStatement(DDL_INI.get("ddl", "define_section"));
      int state = ps.executeUpdate();
      if (state > 0) {
        DB_LOGGER.info("(section) table is created in the database.");
        return true;
      }
    } catch (SQLException e) {
      DB_LOGGER.info("(section) table is already existing in the database.");
    }
    return false;
  }

  public boolean defineSubject() {
    try {
      ps = con.prepareStatement(DDL_INI.get("ddl", "define_subject"));
      int state = ps.executeUpdate();
      if (state > 0) {
        DB_LOGGER.info("(subject) table is created in the database.");
        return true;
      }
    } catch (SQLException e) {
      DB_LOGGER.info("(subject) table is already existing in the database.");
    }
    return false;
  }

  public boolean defineRoomType() {
    try {
      ps = con.prepareStatement(DDL_INI.get("ddl", "define_room_type"));
      int state = ps.executeUpdate();
      if (state > 0) {
        DB_LOGGER.info("(room_type) table is created in the database.");
        return true;
      }
    } catch (SQLException e) {
      DB_LOGGER.info("(room_type) table is already existing in the database.");
    }
    return false;
  }

  public boolean defineRoomTime() {
    try {
      ps = con.prepareStatement(DDL_INI.get("ddl", "define_room_time"));
      int state = ps.executeUpdate();
      if (state > 0) {
        DB_LOGGER.info("(room_time) table is created in the database.");
        return true;
      }
    } catch (SQLException e) {
      DB_LOGGER.info("(room_time) table is already existing in the database.");
    }
    return false;
  }

  public boolean defineRoomTimeslot() {
    try {
      ps = con.prepareStatement(DDL_INI.get("ddl", "define_room_timeslot"));
      int state = ps.executeUpdate();
      if (state > 0) {
        DB_LOGGER.info("(room_timeslot) table is created in the database.");
        return true;
      }
    } catch (SQLException e) {
      DB_LOGGER.info("(room_timeslot) table is already existing in the database.");
    }
    return false;
  }

  public boolean defineRoomDay() {
    try {
      ps = con.prepareStatement(DDL_INI.get("ddl", "define_room_day"));
      int state = ps.executeUpdate();
      if (state > 0) {
        DB_LOGGER.info("(room_day) table is created in the database.");
        return true;
      }
    } catch (SQLException e) {
      DB_LOGGER.info("(room_day) table is already existing in the database.");
    }
    return false;
  }

  public boolean defineRoom() {
    try {
      ps = con.prepareStatement(DDL_INI.get("ddl", "define_room"));
      int state = ps.executeUpdate();
      if (state > 0) {
        DB_LOGGER.info("(room) table is created in the database.");
        return true;
      }
    } catch (SQLException e) {
      DB_LOGGER.info("(room) table is already existing in the database.");
    }
    return false;
  }

  public boolean defineStudent() {
    try {
      ps = con.prepareStatement(DDL_INI.get("ddl", "define_student"));
      int state = ps.executeUpdate();
      if (state > 0) {
        DB_LOGGER.info("(student) table is created in the database.");
        return true;
      }
    } catch (SQLException e) {
      DB_LOGGER.info("(student) table is already existing in the database.");
    }
    return false;
  }

  public boolean defineSectionTakenSubjects() {
    try {
      ps = con.prepareStatement(DDL_INI.get("ddl", "define_section_taken_subjects"));
      int state = ps.executeUpdate();
      if (state > 0) {
        DB_LOGGER.info("(sectiontakensubjects) table is created in the database.");
        return true;
      }
    } catch (SQLException e) {
      DB_LOGGER.info("(sectiontakensubjects) table is already existing in the database.");
    }
    return false;
  }

  public boolean isConnected() {
    return (con != null);
  }

  public Ini getDDL_INI() {
    return DDL_INI;
  }

  public void setDDL_INI(Ini DDL_INI) {
    this.DDL_INI = DDL_INI;
  }

  public StudentQueries getStudentQueries() {
    return studentQueries;
  }

  public SubjectQueries getSubjectQueries() {
    return subjectQueries;
  }

  public void setSubjectQueries(SubjectQueries subjectQueries) {
    this.subjectQueries = subjectQueries;
  }

  public SectionQueries getSectionQueries() {
    return sectionQueries;
  }

  public void setSectionQueries(SectionQueries sectionQueries) {
    this.sectionQueries = sectionQueries;
  }

  public RoomTypeQueries getRoomTypeQueries() {
    return roomTypeQueries;
  }

  public void setRoomTypeQueries(RoomTypeQueries roomTypeQueries) {
    this.roomTypeQueries = roomTypeQueries;
  }

  static class LogUtils {
    public static void failLoadIni(String fileName) {
      DB_LOGGER.error(
          "(" + fileName + ") is not found or failed to load. Requires immediate action.");
    }

    public static void successLoadIni(String fileName) {
      DB_LOGGER.info("(" + fileName + ") has been successfully loaded. Queries are readable.");
    }
  }

  public static class StudentQueries {
    private static Ini STUDENT_INI;

    public StudentQueries() {
      try {
        STUDENT_INI = new Ini(new File(ResourceLoader.getResource("sql/studentsql.ini").toURI()));
        LogUtils.successLoadIni("studentsql.ini");
      } catch (IOException | URISyntaxException e) {
        LogUtils.failLoadIni("studentsql.ini");
      }
    }

    public int create(Student student) throws SQLException {
      ps = con.prepareStatement(STUDENT_INI.get("create", "create_student"));
      ps.setString(1, student.getId());
      ps.setString(2, student.getFirstName());
      ps.setString(3, student.getMiddleName());
      ps.setString(4, student.getLastName());
      ps.setInt(5, student.getSectionId().getId());
      return ps.executeUpdate();
    }

    public ResultSet readAll() throws SQLException {
      ps = con.prepareStatement(STUDENT_INI.get("read", "read_all"));
      return ps.executeQuery();
    }

    public Student readById(String id) throws SQLException {
      ps = con.prepareStatement(STUDENT_INI.get("read", "read_byid"));
      ps.setString(1, id);
      res = ps.executeQuery();

      Student student = new Student();
      while (!res.next()) {
        student.setId(id);
        student.setFirstName(res.getString("first_name"));
        student.setMiddleName(res.getString("middle_name"));
        student.setLastName(res.getString("last_name"));
        student.setSectionId(
            DatabaseManager.getInstance()
                .getSectionQueries()
                .readSectionById(res.getInt("section__fk")));
      }
      return student;
    }

    public ResultSet readByFirstName() throws SQLException {
      ps = con.prepareStatement(STUDENT_INI.get("read", "read_byfirstname"));
      return ps.executeQuery();
    }

    public ResultSet readByMiddleName() throws SQLException {
      ps = con.prepareStatement(STUDENT_INI.get("read", "read_bymiddlename"));
      return ps.executeQuery();
    }

    public ResultSet readByLastName() throws SQLException {
      ps = con.prepareStatement(STUDENT_INI.get("read", "read_bylastname"));
      return ps.executeQuery();
    }

    public int updateAllById(Student student) throws SQLException {
      ps = con.prepareStatement(STUDENT_INI.get("update", "update_all_byid"));
      ps.setString(1, student.getId());
      ps.setString(2, student.getFirstName());
      ps.setString(3, student.getMiddleName());
      ps.setString(4, student.getLastName());
      ps.setInt(5, student.getSectionId().getId());
      ps.setString(6, student.getId());
      return ps.executeUpdate();
    }

    public int deleteAll() throws SQLException {
      ps = con.prepareStatement(STUDENT_INI.get("delete", "delete_all"));
      return ps.executeUpdate();
    }

    public int deleteById(String id) throws SQLException {
      ps = con.prepareStatement(STUDENT_INI.get("delete", "delete_byid"));
      ps.setString(1, id);
      return ps.executeUpdate();
    }
  } // -- End of StudentQueries

  public class SectionQueries {
    private Ini SECTION_INI;

    public SectionQueries() {
      try {
        SECTION_INI = new Ini(new File(ResourceLoader.getResource("sql/sectionsql.ini").toURI()));
        LogUtils.successLoadIni("sectionsql.ini");
      } catch (IOException | URISyntaxException e) {
        LogUtils.failLoadIni("sectionsql.ini");
      }
    }

    public Ini getSECTION_INI() {
      return SECTION_INI;
    }

    public int createSection(Section section) throws SQLException {
      ps = con.prepareStatement(SECTION_INI.get("create", "create_section"));
      ps.setString(1, section.getName());
      return ps.executeUpdate();
    }

    public ResultSet readSectionAll() throws SQLException {
      ps = con.prepareStatement(SECTION_INI.get("read", "read_all"));
      return ps.executeQuery();
    }

    public Section readSectionById(int id) throws SQLException {
      ps = con.prepareStatement(SECTION_INI.get("read", "read_all_byid"));
      ps.setInt(1, id);
      res = ps.executeQuery();
      return new Section(res.getInt("section_id"), res.getString("name"));
    }

    private int updateSectionById(Section section) throws SQLException {
      ps = con.prepareStatement(SECTION_INI.get("update", "update_name_byid"));
      ps.setString(1, section.getName());
      ps.setInt(2, section.getId());
      return ps.executeUpdate();
    }

    public int deleteSectionAll() throws SQLException {
      ps = con.prepareStatement(SECTION_INI.get("delete", "delete_all"));
      return ps.executeUpdate();
    }

    private int deleteSectionById(Section section) throws SQLException {
      ps = con.prepareStatement(SECTION_INI.get("delete", "delete_byid"));
      ps.setInt(1, section.getId());
      return ps.executeUpdate();
    }

    private int deleteSectionByName(Section section) throws SQLException {
      ps = con.prepareStatement(SECTION_INI.get("delete", "delete_byname"));
      ps.setString(1, section.getName());
      return ps.executeUpdate();
    }
    // ---- End of SectionQueries
  } // -- End of SectionQueries

  public class SubjectQueries {
    private Ini SUBJECT_INI;

    public SubjectQueries() {
      try {
        SUBJECT_INI = new Ini(new File(ResourceLoader.getResource("sql/subjectsql.ini").toURI()));
        LogUtils.successLoadIni("subjectsql.ini");
      } catch (IOException | URISyntaxException e) {
        LogUtils.failLoadIni("subjectsql.ini");
      }
    }

    public int create(Subject subject) throws SQLException {
      ps = con.prepareStatement(SUBJECT_INI.get("create", "create_subject"));
      ps.setString(1, subject.getId());
      ps.setString(2, subject.getName());
      ps.setString(3, subject.getDesc());
      ps.setInt(4, subject.getType().getId());
      return ps.executeUpdate();
    }

    public ResultSet readAll() throws SQLException {
      ps = con.prepareStatement(SUBJECT_INI.get("read", "read_all"));
      return ps.executeQuery();
    }

    public Subject readById(int id) throws SQLException {
      ps = con.prepareStatement(SUBJECT_INI.get("read", "read_all_byid"));
      ps.setInt(1, id);
      res = ps.executeQuery();

      Subject subject = new Subject();
      while (!res.next()) {
        subject.setId(res.getString("subject_id"));
        subject.setName(res.getString("name"));
        subject.setDesc(res.getString("desc"));
        subject.setType(roomTypeQueries.readById(res.getInt("type__fk")));
      }
      return subject;
    }

    public ResultSet readByType() throws SQLException {
      ps = con.prepareStatement(SUBJECT_INI.get("read", "read_all_bytype"));
      return ps.executeQuery();
    }

    public int updateById(Subject subject) throws SQLException {
      ps = con.prepareStatement(SUBJECT_INI.get("update", "update_all_by_id"));
      ps.setString(1, subject.getName());
      ps.setString(2, subject.getDesc());
      ps.setInt(3, subject.getType().getId());
      return ps.executeUpdate();
    }

    public int deleteAll() throws SQLException {
      ps = con.prepareStatement(SUBJECT_INI.get("delete", "delete_all"));
      return ps.executeUpdate();
    }

    public int deleteById(String id) throws SQLException {
      ps = con.prepareStatement(SUBJECT_INI.get("delete", "delete_byid"));
      ps.setString(1, id);
      return ps.executeUpdate();
    }
  } // -- End of SubjectQueries

  class RoomQueries {
    private Ini ROOM_INI;

    public RoomQueries() {
      try {
        ROOM_INI = new Ini(new File(ResourceLoader.getResource("sql/roomsql.ini").toURI()));
        LogUtils.successLoadIni("roomsql.ini");
      } catch (IOException | URISyntaxException e) {
        LogUtils.failLoadIni("roomsql.ini");
      }
    }

    public int create(Room room) throws SQLException {
      ps = con.prepareStatement(ROOM_INI.get("create", "create_room"));
      ps.setString(1, room.getName());
      ps.setInt(2, room.getType());
      return ps.executeUpdate();
    }

    public ResultSet readAll() throws SQLException {
      ps = con.prepareStatement(ROOM_INI.get("read", "read_all"));
      return ps.executeQuery();
    }

    public Room readById(int id) throws SQLException {
      ps = con.prepareStatement(ROOM_INI.get("read", "read_byid"));
      ps.setInt(1, id);
      res = ps.executeQuery();

      Room room = new Room();
      while (!res.next()) {
        room.setId(id);
        room.setName(res.getString("name"));
        room.setType(res.getInt("type__fk"));
      }
      return room;
    }

    public ResultSet readByType(RoomType roomType) throws SQLException {
      ps = con.prepareStatement(ROOM_INI.get("read", "read_bytype"));
      ps.setInt(1, roomType.getId());
      return ps.executeQuery();
    }

    public int updateAllById(Room room) throws SQLException {
      ps = con.prepareStatement(ROOM_INI.get("update", "update_all_byid"));
      ps.setString(1, room.getName());
      ps.setInt(2, room.getType());
      return ps.executeUpdate();
    }

    public int deleteAll() throws SQLException {
      ps = con.prepareStatement(ROOM_INI.get("delete", "delete_all"));
      return ps.executeUpdate();
    }

    public int deleteById(int id) throws SQLException {
      ps = con.prepareStatement(ROOM_INI.get("delete", "delete_byid"));
      ps.setInt(1, id);
      return ps.executeUpdate();
    }

    public int deleteByName(String name) throws SQLException {
      ps = con.prepareStatement(ROOM_INI.get("delete", "delete_byname"));
      ps.setString(1, name);
      return ps.executeUpdate();
    }
  } // -- End of RoomQueries

  class RoomDayQueries {
    private Ini ROOMDAY_INI;

    public RoomDayQueries() {
      try {
        ROOMDAY_INI = new Ini(new File(ResourceLoader.getResource("sql/roomdaysql.ini").toURI()));
        LogUtils.successLoadIni("roomdaysql.ini");
      } catch (IOException | URISyntaxException e) {
        LogUtils.failLoadIni("roomdaysql.ini");
      }
    }

    public int create(RoomDay roomDay) throws SQLException {
      ps = con.prepareStatement(ROOMDAY_INI.get("create", "create_roomday"));
      ps.setString(1, roomDay.getDay());
      return ps.executeUpdate();
    }

    public ResultSet reallAll() throws SQLException {
      ps = con.prepareStatement(ROOMDAY_INI.get("read", "read_all"));
      return ps.executeQuery();
    }

    public RoomDay readById(int id) throws SQLException {
      ps = con.prepareStatement(ROOMDAY_INI.get("read", "read_byid"));
      ps.setInt(1, id);
      res = ps.executeQuery();

      RoomDay roomDay = new RoomDay();
      while (!res.next()) {
        roomDay.setId(id);
        roomDay.setDay(res.getString("text"));
      }
      return roomDay;
    }

    public int updateById(RoomDay roomDay) throws SQLException {
      ps = con.prepareStatement(ROOMDAY_INI.get("update", "update_byid"));
      ps.setString(1, roomDay.getDay());
      ps.setInt(2, roomDay.getId());
      return ps.executeUpdate();
    }

    public int deleteAll() throws SQLException {
      ps = con.prepareStatement(ROOMDAY_INI.get("delete", "delete_all"));
      return ps.executeUpdate();
    }

    public int deleteById(int id) throws SQLException {
      ps = con.prepareStatement(ROOMDAY_INI.get("delete", "delete_byid"));
      ps.setInt(1, id);
      return ps.executeUpdate();
    }
  } // -- End of RoomDay Queries

  class RoomTimeQueries {
    private Ini ROOMTIME_INI;

    public RoomTimeQueries() {
      try {
        ROOMTIME_INI = new Ini(new File(ResourceLoader.getResource("sql/roomtimesql.ini").toURI()));
        LogUtils.successLoadIni("roomtimesql.ini");
      } catch (IOException | URISyntaxException e) {
        LogUtils.failLoadIni("roomtimesql.ini");
      }
    }

    public int create(RoomTime roomTime) throws SQLException {
      ps = con.prepareStatement(ROOMTIME_INI.get("create", "create_roomtime"));
      ps.setString(1, roomTime.getTime());
      return ps.executeUpdate();
    }

    public ResultSet readAll() throws SQLException {
      ps = con.prepareStatement(ROOMTIME_INI.get("read", "read_all"));
      return ps.executeQuery();
    }

    public RoomTime readById(int id) throws SQLException {
      ps = con.prepareStatement(ROOMTIME_INI.get("read", "read_all_byid"));
      ps.setInt(1, id);
      res = ps.executeQuery();

      RoomTime roomTime = new RoomTime();
      while (!res.next()) {
        roomTime.setId(res.getInt("id"));
        roomTime.setTime(res.getString("time"));
      }
      return roomTime;
    }

    public int updateTimeById(RoomTime roomTime) throws SQLException {
      ps = con.prepareStatement(ROOMTIME_INI.get("update", "update_time_byid"));
      ps.setString(1, roomTime.getTime());
      ps.setInt(2, roomTime.getId());
      return ps.executeUpdate();
    }

    public int deleteAll() throws SQLException {
      ps = con.prepareStatement(ROOMTIME_INI.get("delete", "delete_all"));
      return ps.executeUpdate();
    }

    public int deleteById(int id) throws SQLException {
      ps = con.prepareStatement(ROOMTIME_INI.get("delete", "delete_byid"));
      ps.setInt(1, id);
      return ps.executeUpdate();
    }
  } // -- End of RoomTime Queries

  class RoomTimeslotQueries {
    private Ini ROOMTIMESLOT_INI;

    public RoomTimeslotQueries() {
      try {
        ROOMTIMESLOT_INI =
            new Ini(new File(ResourceLoader.getResource("sql/roomtimeslotsql.ini").toURI()));
        LogUtils.successLoadIni("roomtimeslotsql.ini");
      } catch (IOException | URISyntaxException e) {
        LogUtils.failLoadIni("roomtimeslotsql.ini");
      }
    }

    public int create(RoomTimeslot roomTimeslot) throws SQLException {
      ps = con.prepareStatement(ROOMTIMESLOT_INI.get("create", "create_timeslot"));
      ps.setInt(1, roomTimeslot.getSectionId());
      ps.setInt(2, roomTimeslot.getRoomId());
      ps.setInt(3, roomTimeslot.getTimeId());
      ps.setInt(4, roomTimeslot.getDayId());
      return ps.executeUpdate();
    }

    public ResultSet readAll() throws SQLException {
      ps = con.prepareStatement(ROOMTIMESLOT_INI.get("read", "read_all"));
      return ps.executeQuery();
    }

    public RoomTimeslot readById(int id) throws SQLException {
      ps = con.prepareStatement(ROOMTIMESLOT_INI.get("read", "read_all_byid"));
      ps.setInt(1, id);
      res = ps.executeQuery();

      RoomTimeslot roomTimeslot = new RoomTimeslot();
      while (!res.next()) {
        roomTimeslot.setId(id);
        roomTimeslot.setSectionId(res.getInt("section__fk"));
        roomTimeslot.setRoomId(res.getInt("room__fk"));
        roomTimeslot.setTimeId(res.getInt("time__fk"));
        roomTimeslot.setDayId(res.getInt("day__fk"));
      }
      return roomTimeslot;
    }

    public ResultSet readBySection(int sectionId) throws SQLException {
      ps = con.prepareStatement(ROOMTIMESLOT_INI.get("read", "read_bysection"));
      ps.setInt(1, sectionId);
      return ps.executeQuery();
    }

    public ResultSet readByTime(int timeId) throws SQLException {
      ps = con.prepareStatement(ROOMTIMESLOT_INI.get("read", "read_bytime"));
      ps.setInt(1, timeId);
      return ps.executeQuery();
    }

    public ResultSet readByDay(int dayId) throws SQLException {
      ps = con.prepareStatement(ROOMTIMESLOT_INI.get("read", "read_byday"));
      ps.setInt(1, dayId);
      return ps.executeQuery();
    }

    public int updateAllById(RoomTimeslot roomTimeslot) throws SQLException {
      ps = con.prepareStatement(ROOMTIMESLOT_INI.get("update", "update_all_byid"));
      ps.setInt(1, roomTimeslot.getSectionId());
      ps.setInt(2, roomTimeslot.getRoomId());
      ps.setInt(3, roomTimeslot.getTimeId());
      ps.setInt(4, roomTimeslot.getDayId());
      ps.setInt(5, roomTimeslot.getId());
      return ps.executeUpdate();
    }

    public int deleteAll() throws SQLException {
      ps = con.prepareStatement(ROOMTIMESLOT_INI.get("delete", "delete_all"));
      return ps.executeUpdate();
    }

    public int deleteById(int id) throws SQLException {
      ps = con.prepareStatement(ROOMTIMESLOT_INI.get("delete", "delete_byid"));
      ps.setInt(1, id);
      return ps.executeUpdate();
    }

    // TODO: Continue the other delete functions
  } // -- End of RoomTimeslotQueries

  public class RoomTypeQueries {
    private Ini ROOMTYPE_INI;

    public RoomTypeQueries() {
      try {
        ROOMTYPE_INI = new Ini(new File(ResourceLoader.getResource("sql/roomtypesql.ini").toURI()));
        LogUtils.successLoadIni("roomtypesql.ini");
      } catch (IOException | URISyntaxException e) {
        LogUtils.failLoadIni("roomtypesql.ini");
      }
    }

    public int create(RoomType roomType) throws SQLException {
      ps = con.prepareStatement(ROOMTYPE_INI.get("create", "create_roomtype"));
      ps.setString(1, roomType.getType());
      return ps.executeUpdate();
    }

    public ResultSet readAll() throws SQLException {
      ps = con.prepareStatement(ROOMTYPE_INI.get("read", "read_all"));
      return ps.executeQuery();
    }

    public RoomType readById(int id) throws SQLException {
      ps = con.prepareStatement(ROOMTYPE_INI.get("read", "read_byid"));
      ps.setInt(1, id);
      res = ps.executeQuery();

      RoomType roomType = new RoomType();
      while (!res.next()) {
        roomType.setId(id);
        roomType.setType(res.getString("name"));
      }
      return roomType;
    }

    public int updateById(RoomType roomType) throws SQLException {
      ps = con.prepareStatement(ROOMTYPE_INI.get("update", "update_name_byid"));
      ps.setString(1, roomType.getType());
      ps.setInt(2, roomType.getId());
      return ps.executeUpdate();
    }

    public int deleteAll() throws SQLException {
      ps = con.prepareStatement(ROOMTYPE_INI.get("delete", "delete_all"));
      return ps.executeUpdate();
    }

    public int deleteById(int id) throws SQLException {
      ps = con.prepareStatement(ROOMTYPE_INI.get("delete", "delete_byid"));
      ps.setInt(1, id);
      return ps.executeUpdate();
    }
  } // -- End of RoomTypeQueries

  class SectionTakenSubjectsQueries {
    private Ini STS_INI;

    public SectionTakenSubjectsQueries() {
      try {
        STS_INI =
            new Ini(
                new File(ResourceLoader.getResource("sql/sectiontakensubjectssql.ini").toURI()));
        LogUtils.successLoadIni("sectiontakensubjectssql.ini");
      } catch (IOException | URISyntaxException e) {
        LogUtils.failLoadIni("sectiontakensubjects.ini");
      }
    }

    public int create(SectionTakenSubjects takenSubjects) throws SQLException {
      ps = con.prepareStatement(STS_INI.get("create", "insert_sts"));
      ps.setInt(1, takenSubjects.getSectionId());
      ps.setInt(2, takenSubjects.getSubjectId());
      return ps.executeUpdate();
    }

    public ResultSet readAll() throws SQLException {
      ps = con.prepareStatement(STS_INI.get("read", "read_all"));
      return ps.executeQuery();
    }

    public SectionTakenSubjects readById(int id) throws SQLException {
      ps = con.prepareStatement(STS_INI.get("read", "read_all_byid"));
      ps.setInt(1, id);
      res = ps.executeQuery();

      SectionTakenSubjects takenSubjects = new SectionTakenSubjects();
      while (!res.next()) {
        takenSubjects.setId(id);
        takenSubjects.setSectionId(res.getInt("section__fk"));
        takenSubjects.setSubjectId(res.getInt("subject__fk"));
      }
      return takenSubjects;
    }

    // TODO: Place here the query which finds which subject has been used and are not.

    public int updateAllById(SectionTakenSubjects takenSubjects) throws SQLException {
      ps = con.prepareStatement(STS_INI.get("update", "update_all_byid"));
      ps.setInt(1, takenSubjects.getSectionId());
      ps.setInt(2, takenSubjects.getSubjectId());
      ps.setInt(3, takenSubjects.getId());
      return ps.executeUpdate();
    }

    // TODO: There are left query functions.

    public int deleteAll() throws SQLException {
      ps = con.prepareStatement(STS_INI.get("delete", "delete_all"));
      return ps.executeUpdate();
    }

    public int deleteById(int id) throws SQLException {
      ps = con.prepareStatement(STS_INI.get("delete", "delete_byid"));
      ps.setInt(1, id);
      return ps.executeUpdate();
    }
  } // -- End of SectionTakenSubjectsQueries
}
