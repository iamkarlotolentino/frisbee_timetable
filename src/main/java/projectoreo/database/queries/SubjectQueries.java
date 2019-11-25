package projectoreo.database.queries;

import org.ini4j.Ini;
import projectoreo.database.DatabaseManager;
import projectoreo.database.columns.SubjectColumn;
import projectoreo.models.Subject;
import projectoreo.utils.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SubjectQueries {
  private DatabaseManager DB_MANAGER;
  private PreparedStatement ps;
  private ResultSet res;

  private Ini SUBJECT_INI;

  public SubjectQueries(DatabaseManager DB_MANAGER) {
    this.DB_MANAGER = DB_MANAGER;
    try {
      SUBJECT_INI = new Ini(new File(ResourceLoader.getResource("sql/subjectsql.ini").toURI()));
      DatabaseManager.LogUtils.successLoadIni("subjectsql.ini");
    } catch (IOException | URISyntaxException e) {
      DatabaseManager.LogUtils.failLoadIni("subjectsql.ini");
    }
  }

  public int create(Subject subject) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(SUBJECT_INI.get("create", "create_subject"));
    ps.setString(1, subject.getId());
    ps.setString(2, subject.getName());
    ps.setInt(3, subject.getUnits());
    ps.setInt(4, subject.getRequiredHours());
    ps.setInt(5, subject.getType().getId());
    return ps.executeUpdate();
  }

  public ResultSet readAll() throws SQLException {
    ps = DatabaseManager.con.prepareStatement(SUBJECT_INI.get("read", "read_all"));
    return ps.executeQuery();
  }

  public Subject readById(String id) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(SUBJECT_INI.get("read", "read_all_byid"));
    ps.setString(1, id);
    res = ps.executeQuery();

    Subject subject = new Subject();
    while (res.next()) {
      subject.setId(res.getString(SubjectColumn.SUBJECT_ID.get()));
      subject.setName(res.getString(SubjectColumn.NAME.get()));
      subject.setUnits(res.getInt(SubjectColumn.UNITS.get()));
      subject.setRequiredHours(res.getInt(SubjectColumn.REQUIRED_HOURS.get()));
      subject.setType(DB_MANAGER.getRoomTypeQueries().readById(SubjectColumn.TYPE_ID.get()));
    }
    return subject;
  }

  public ResultSet readByType() throws SQLException {
    ps = DatabaseManager.con.prepareStatement(SUBJECT_INI.get("read", "read_all_bytype"));
    return ps.executeQuery();
  }

  public int updateById(Subject subject) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(SUBJECT_INI.get("update", "update_all_by_id"));
    ps.setString(1, subject.getId());
    ps.setString(2, subject.getName());
    ps.setInt(3, subject.getUnits());
    ps.setInt(4, subject.getRequiredHours());
    ps.setInt(5, subject.getType().getId());
    ps.setString(6, subject.getId());
    return ps.executeUpdate();
  }

  public int deleteAll() throws SQLException {
    ps = DatabaseManager.con.prepareStatement(SUBJECT_INI.get("delete", "delete_all"));
    return ps.executeUpdate();
  }

  public int deleteById(String id) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(SUBJECT_INI.get("delete", "delete_byid"));
    ps.setString(1, id);
    return ps.executeUpdate();
  }
} // -- End of SubjectQueries
