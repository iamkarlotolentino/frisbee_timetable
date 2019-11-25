package projectoreo.database.queries;

import org.ini4j.Ini;
import projectoreo.database.DatabaseManager;
import projectoreo.models.Student;
import projectoreo.utils.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentQueries {
  private DatabaseManager DB_MANAGER;
  private PreparedStatement ps;
  private ResultSet res;
  
  private Ini STUDENT_INI;

  public StudentQueries(DatabaseManager DB_MANAGER) {
    this.DB_MANAGER = DB_MANAGER;
    try {
      STUDENT_INI = new Ini(new File(ResourceLoader.getResource("sql/studentsql.ini").toURI()));
      DatabaseManager.LogUtils.successLoadIni("studentsql.ini");
    } catch (IOException | URISyntaxException e) {
      DatabaseManager.LogUtils.failLoadIni("studentsql.ini");
    }
  }

  public int create(Student student) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(STUDENT_INI.get("create", "create_student"));
    ps.setString(1, student.getId());
    ps.setString(2, student.getFirstName());
    ps.setString(3, student.getMiddleName());
    ps.setString(4, student.getLastName());
    ps.setInt(5, student.getSectionId().getId());
    return ps.executeUpdate();
  }

  public ResultSet readAll() throws SQLException {
    ps = DatabaseManager.con.prepareStatement(STUDENT_INI.get("read", "read_all"));
    return ps.executeQuery();
  }

  public Student readById(String id) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(STUDENT_INI.get("read", "read_byid"));
    ps.setString(1, id);
    res = ps.executeQuery();

    Student student = new Student();
    while (res.next()) {
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
    ps = DatabaseManager.con.prepareStatement(STUDENT_INI.get("read", "read_byfirstname"));
    return ps.executeQuery();
  }

  public ResultSet readByMiddleName() throws SQLException {
    ps = DatabaseManager.con.prepareStatement(STUDENT_INI.get("read", "read_bymiddlename"));
    return ps.executeQuery();
  }

  public ResultSet readByLastName() throws SQLException {
    ps = DatabaseManager.con.prepareStatement(STUDENT_INI.get("read", "read_bylastname"));
    return ps.executeQuery();
  }

  public ResultSet readBySection(int sectionId) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(STUDENT_INI.get("read", "read_bysection"));
    ps.setInt(1, sectionId);
    return ps.executeQuery();
  }

  public int updateAllById(Student student) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(STUDENT_INI.get("update", "update_all_byid"));
    ps.setString(1, student.getId());
    ps.setString(2, student.getFirstName());
    ps.setString(3, student.getMiddleName());
    ps.setString(4, student.getLastName());
    ps.setInt(5, student.getSectionId().getId());
    ps.setString(6, student.getId());
    return ps.executeUpdate();
  }

  public int deleteAll() throws SQLException {
    ps = DatabaseManager.con.prepareStatement(STUDENT_INI.get("delete", "delete_all"));
    return ps.executeUpdate();
  }

  public int deleteById(String id) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(STUDENT_INI.get("delete", "delete_byid"));
    ps.setString(1, id);
    return ps.executeUpdate();
  }
} // -- End of StudentQueries
