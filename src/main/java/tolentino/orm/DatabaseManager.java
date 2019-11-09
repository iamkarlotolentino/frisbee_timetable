package tolentino.orm;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

    private static DatabaseManager instance;

    private Connection con = null;
    private PreparedStatement ps = null;
    private ResultSet res = null;

    private DatabaseManager() {
        // If not yet connected, then connect
        if (con == null)
            connect("src/main/res/db.properties");
    }

    // Pass the properties file URL
    private void connect(String url) {

        HikariConfig config = new HikariConfig(url);
        HikariDataSource dataSource = new HikariDataSource(config);

        try {
            Class.forName("org.sqlite.JDBC");
            con = dataSource.getConnection();

            ps = con.prepareStatement("SELECT * FROM room;");
            res = ps.executeQuery();

//            while (res.next()) {
//                System.out.println(res.getString("name"));
//            }
        } catch (SQLException | ClassNotFoundException e) {
            con = null;
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return (con != null);
    }

    /***************************************************************************************************
     * SECTION :: FUNCTIONS
     ***************************************************************************************************/

    private void createSection(Section.Entity section) throws SQLException {
        ps = con.prepareStatement(
                "INSERT INTO section VALUES (section.name)"
        );
        ps.execute();
    }

    private ResultSet readSectionAll() throws SQLException {
        ps = con.prepareStatement(
                "SELECT * FROM section"
        );
        return ps.executeQuery();
    }

    private Section.Entity readSectionById(Section.Entity section) throws SQLException {
        ps = con.prepareStatement(
                "SELECT * FROM section WHERE id = ?"
        );
        ps.setInt(1, section.getId());
        res = ps.executeQuery();
        section.setName(res.getString("name"));
        return section;
    }

    private void updateSectionById(Section.Entity section) throws SQLException {
        ps = con.prepareStatement(
                "UPDATE section SET name = ? WHERE id = ?"
        );
        ps.setString(1, section.getName());
        ps.setInt(2, section.getId());
        ps.executeUpdate();
    }

    private void deleteSectionById(Section.Entity section) throws SQLException {
        ps = con.prepareStatement(
                "DELETE FROM section WHERE id = ?"
        );
        ps.setInt(1, section.getId());
        ps.executeUpdate();
    }

    private void deleteSectionByName(Section.Entity section) throws SQLException {
        ps = con.prepareStatement(
                "DELETE FROM section WHERE name = ?"
        );
        ps.setString(1, section.getName());
        ps.executeUpdate();
    }

    /***************************************************************************************************
     * ROOM TYPE :: FUNCTIONS
     ***************************************************************************************************/

    private void createRoomType(RoomType.Entity roomType) throws SQLException {
        ps = con.prepareStatement(
                "INSERT INTO subject_room_type VALUES (type)"
        );
        ps.execute();
    }

    private ResultSet readRoomTypeAll() throws SQLException {
        ps = con.prepareStatement(
                "SELECT * FROM subject_room_type"
        );
        return ps.executeQuery();
    }

    private RoomType.Entity readRoomTypeById(RoomType.Entity roomType) throws SQLException {
        ps = con.prepareStatement(
                "SELECT * FROM subject_room_type WHERE id=?"
        );
        ps.setInt(1, roomType.getId());
        res = ps.executeQuery();
        roomType.setType(res.getString("type"));
        return roomType;
    }

    private void updateRoomTypeById(RoomType.Entity roomType) throws SQLException {
        ps = con.prepareStatement(
                "UPDATE subject_room_type SET type=? WHERE id=?"
        );
        ps.setString(1, roomType.getType());
        ps.setInt(2, roomType.getId());
        ps.executeUpdate();
    }

    private void deleteRoomTypeAll() throws SQLException {
        ps = con.prepareStatement(
                "DELETE FROM subject_room_type"
        );
        ps.executeUpdate();
    }

    private void deleteRoomTypeById(RoomType.Entity roomType) throws SQLException {
        ps = con.prepareStatement(
                "DELETE FROM subject_room_type WHERE id=?"
        );
        ps.setInt(1, roomType.getId());
        ps.executeUpdate();
    }

    /***************************************************************************************************
     * ROOM :: FUNCTIONS
     ***************************************************************************************************/

    private void createRoom(Room.Entity room) throws SQLException {
        ps = con.prepareStatement(
                "INSERT INTO room VALUES (?, ?)"
        );
        ps.setInt(1, room.getType().getId());
        ps.setString(2, room.getName());
        ps.executeUpdate();
    }

    private ResultSet readRoomAll() throws SQLException {
        ps = con.prepareStatement(
                "SELECT * FROM room"
        );
        return ps.executeQuery();
    }

    private Room.Entity readRoomById(Room.Entity room) throws SQLException {
        ps = con.prepareStatement(
                "SELECT * FROM room WHERE id=?"
        );
        ps.setInt(1, room.getId());
        res = ps.executeQuery();
        room.getType().setType(res.getString("type"));
        room.setName(res.getString("name"));
        return room;
    }

    private ResultSet readRoomByType(RoomType.Entity roomType) throws SQLException {
        ps = con.prepareStatement(
                "SELECT * FROM room WHERE type=?"
        );
        ps.setInt(1, roomType.getId());
        return ps.executeQuery();
    }

    private void updateRoomById(Room.Entity room) throws SQLException {
        ps = con.prepareStatement(
                "UPDATE room SET type=?, name=? WHERE id=?"
        );
        ps.setInt(1, room.getType().getId());
        ps.setString(2, room.getName());
        ps.setInt(3, room.getId());
        ps.executeUpdate();
    }

    private void deleteRoomAll() throws SQLException {
        ps = con.prepareStatement(
                "DELETE FROM room"
        );
        ps.executeUpdate();
    }

    private void deleteRoomById(Room.Entity room) throws SQLException {
        ps = con.prepareStatement(
                "DELETE FROM room WHERE id=?"
        );
        ps.setInt(1, room.getId());
        ps.executeUpdate();
    }

    /***************************************************************************************************
     * STUDENT :: FUNCTIONS
     ***************************************************************************************************/

    private void createStudent(Student.Entity student) throws SQLException {

    }
    // TODO: CRUD SUBJECT

    // TODO: CRUD STUDENT

    public static DatabaseManager getInstance() {
        if (instance == null)
            instance = new DatabaseManager();
        return instance;
    }
}
