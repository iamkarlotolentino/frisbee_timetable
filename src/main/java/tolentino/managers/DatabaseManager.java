package tolentino.managers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.ini4j.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tolentino.models.*;

public class DatabaseManager {

    private static Logger DB_LOGGER = LoggerFactory.getLogger(DatabaseManager.class);

    private static DatabaseManager instance;

    private Connection con = null;
    private PreparedStatement ps = null;
    private ResultSet res = null;

    private Ini DDL_INI;
    private Ini.Section DDL_SECTION;

    private DatabaseManager() {
        // If not yet connected, then connect
        if (con == null)
            connect("src/main/res/db.properties");
        // If not yet defined tables
        if (true) {
            try {
                DDL_INI = new Ini(new File("src/main/res/sql/ddl.ini"));
                DDL_SECTION = DDL_INI.get("ddl");
            } catch (IOException e) {
                DB_LOGGER.error("The ddl SQL file is not found.");
            }
        }
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

    //--------- Data Definition Language
    public boolean defineSection() {
        try {
            ps = con.prepareStatement(DDL_SECTION.get("define_section"));
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
            ps = con.prepareStatement(DDL_SECTION.get("define_subject"));
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
            ps = con.prepareStatement(DDL_SECTION.get("define_room_type"));
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
            ps = con.prepareStatement(DDL_SECTION.get("define_room_time"));
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
            ps = con.prepareStatement(DDL_SECTION.get("define_room_timeslot"));
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
            ps = con.prepareStatement(DDL_SECTION.get("define_room_day"));
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
            ps = con.prepareStatement(DDL_SECTION.get("define_room"));
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
            ps = con.prepareStatement(DDL_SECTION.get("define_student"));
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

    class SectionQueries {
        private Ini SECTION_INI;

        public SectionQueries() {
            try {
                SECTION_INI = new Ini(new File("src/main/res/sql/sectionsql.ini"));
                LogUtils.successLoadIni("sectionsql.ini");
            } catch (IOException e) {
                LogUtils.failLoadIni("sectionsql.ini");
            }
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

        private Section readSectionById(Section section) throws SQLException {
            ps = con.prepareStatement(SECTION_INI.get("read", "read_all_byid"));
            ps.setInt(1, section.getId());
            res = ps.executeQuery();
            section.setName(res.getString("name"));
            return section;
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
        //---- End of SectionQueries
    }   //-- End6 of SectionQueries

    class SubjectQueries {
        private Ini SUBJECT_INI;

        public SubjectQueries() {
            try {
                SUBJECT_INI = new Ini(new File("src/main/res/sql/subjectsql.ini"));
                LogUtils.successLoadIni("subjectsql.ini");
            } catch (IOException e) {
                LogUtils.failLoadIni("subjectsql.ini");
            }
        }

        public int create(Subject subject) throws SQLException {
            ps = con.prepareStatement(SUBJECT_INI.get("create", "create_subject"));
            ps.setString(1, subject.getName());
            ps.setString(2, subject.getDesc());
            ps.setInt(3, subject.getType());
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
                subject.setId(res.getInt("subject_id"));
                subject.setName(res.getString("name"));
                subject.setDesc(res.getString("desc"));
                subject.setType(res.getInt("type__fk"));
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
            ps.setInt(3, subject.getType());
            return ps.executeUpdate();
        }

        public int deleteAll() throws SQLException {
            ps = con.prepareStatement(SUBJECT_INI.get("delete", "delete_all"));
            return ps.executeUpdate();
        }
    }   //-- End of SubjectQueries

    class RoomQueries {
        private Ini ROOM_INI;

        public RoomQueries() {
            try {
                ROOM_INI = new Ini(new File("src/main/res/sql/roomsql.ini"));
                LogUtils.successLoadIni("roomsql.ini");
            } catch (IOException e) {
                LogUtils.failLoadIni("roomsql.ini");
            }
        }

        public int create(Room room) throws SQLException {
            ps = con.prepareStatement(ROOM_INI.get("create", "create_room"));
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
                // TODO: Please include the name of the room type. After creating the room type.
                room.setType(new RoomType(res.getInt("id"), null));
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
            ps.setInt(2, room.getType().getId());
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
    } //-- End of RoomQueries

    class StudentQueries {
        private Ini STUDENT_INI;

        public StudentQueries() {
            try {
                STUDENT_INI = new Ini(new File("src/main/res/sql/studentsql.ini"));
                LogUtils.successLoadIni("studentsql.ini");
            } catch (IOException e) {
                LogUtils.failLoadIni("studentsql.ini");
            }
        }

        public int create(Student student) throws SQLException {
            ps = con.prepareStatement(STUDENT_INI.get("create", "insert_student"));
            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getMiddleName());
            ps.setString(3, student.getLastName());
            ps.setInt(4, student.getSectionId());
            return ps.executeUpdate();
        }

        public ResultSet readAll() throws SQLException {
            ps = con.prepareStatement(STUDENT_INI.get("read", "read_all"));
            return ps.executeQuery();
        }

        public Student readById() throws SQLException {
            ps = con.prepareStatement(STUDENT_INI.get("read", "read_byid"));
            res = ps.executeQuery();

            Student student = new Student();
            while (!res.next()) {
                student.setFirstName(res.getString("first_name"));
                student.setMiddleName(res.getString("middle_name"));
                student.setLastName(res.getString("last_name"));
                student.setSectionId(res.getInt("section__fk"));
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
            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getMiddleName());
            ps.setString(3, student.getLastName());
            ps.setInt(4, student.getSectionId());
            return ps.executeUpdate();
        }

        public int deleteAll() throws SQLException {
            ps = con.prepareStatement(STUDENT_INI.get("delete", "delete_all"));
            return ps.executeUpdate();
        }

        public int deleteById(int id) throws SQLException {
            ps = con.prepareStatement(STUDENT_INI.get("delete", "delete_byid"));
            ps.setInt(1, id);
            return ps.executeUpdate();
        }

    }   //-- End of StudentQueries

    class RoomDayQueries {
        private Ini ROOMDAY_INI;

        public RoomDayQueries() {
            try {
                ROOMDAY_INI = new Ini(new File("src/main/res/sql/roomdaysql.ini"));
                LogUtils.successLoadIni("roomdaysql.ini");
            } catch (IOException e) {
                LogUtils.failLoadIni("roomdaysql.ini");
            }
        }

        public int create(RoomDay roomDay) throws SQLException {
            ps = con.prepareStatement(ROOMDAY_INI.get("create", "create_roomday"));
            return ps.executeUpdate();
        }

        public ResultSet reallAll() throws SQLException {
            ps = con.prepareStatement(ROOMDAY_INI.get("read", "read_all"));
            return ps.executeQuery();
        }

        public RoomDay readById(int id) throws SQLException {
            ps = con.prepareStatement(ROOMDAY_INI.get("read", "read_byid"));
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

    }   //-- End of RoomDay Queries

    class RoomTimeQueries {
        private Ini ROOMTIME_INI;

        public RoomTimeQueries() {
            try {
                ROOMTIME_INI = new Ini(new File("src/main/res/sql/roomtimesql.ini"));
                LogUtils.successLoadIni("roomtimesql.ini");
            } catch (IOException e) {
                LogUtils.failLoadIni("roomtimesql.ini");
            }
        }

        public int create(RoomTime roomTime) throws SQLException {
            ps = con.prepareStatement(ROOMTIME_INI.get("create", "create_roomtime"));
            return ps.executeUpdate();
        }

        public ResultSet readAll() throws SQLException {
            ps = con.prepareStatement(ROOMTIME_INI.get("read", "read_all"));
            return ps.executeQuery();
        }

        public RoomTime readById(int id) throws SQLException {
            ps = con.prepareStatement(ROOMTIME_INI.get("read", "read_all_byid"));
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
    }   //-- End of RoomTime Queries

    class RoomTimeslotQueries {

    }   //-- End of RoomTimeslotQueries

    class RoomTypeQueries {

    }   //-- End of RoomTypeQueries

    class SectionTakenSubjectsQueries {

    }   //-- End of SectionTakenSubjectsQueries

    static class LogUtils {
        public static void failLoadIni(String fileName) {
            DB_LOGGER.error("(" + fileName + ") is not found or failed to load. Requires immediate action.");
        }

        public static void successLoadIni(String fileName) {
            DB_LOGGER.error("(" + fileName + ") has been successfully loaded. Queries are readable.");
        }
    }

    public boolean isConnected() {
        return (con != null);
    }

    public static DatabaseManager getInstance() {
        if (instance == null)
            instance = new DatabaseManager();
        return instance;
    }
}
