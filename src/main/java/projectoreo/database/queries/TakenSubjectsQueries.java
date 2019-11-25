package projectoreo.database.queries;

import org.ini4j.Ini;
import projectoreo.database.DatabaseManager;
import projectoreo.models.TakenSubjects;
import projectoreo.utils.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TakenSubjectsQueries {
  private DatabaseManager databaseManager;
  private PreparedStatement ps;
  private ResultSet res;
  
  private Ini STS_INI;

  public TakenSubjectsQueries(DatabaseManager databaseManager) {
    this.databaseManager = databaseManager;
    try {
      STS_INI =
          new Ini(
              new File(ResourceLoader.getResource("sql/takensubjects.sql.ini").toURI()));
      DatabaseManager.LogUtils.successLoadIni("takensubjects.sql.ini");
    } catch (IOException | URISyntaxException e) {
      DatabaseManager.LogUtils.failLoadIni("sectiontakensubjects.ini");
    }
  }

  public int create(TakenSubjects takenSubjects) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(STS_INI.get("create", "insert_sts"));
    ps.setInt(1, takenSubjects.getSectionId());
    ps.setString(2, takenSubjects.getSubjectId());
    return ps.executeUpdate();
  }

  public ResultSet readAll() throws SQLException {
    ps = DatabaseManager.con.prepareStatement(STS_INI.get("read", "read_all"));
    return ps.executeQuery();
  }

  public TakenSubjects readById(int id) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(STS_INI.get("read", "read_all_byid"));
    ps.setInt(1, id);
    res = ps.executeQuery();

    TakenSubjects takenSubjects = new TakenSubjects();
    while (res.next()) {
      takenSubjects.setId(id);
      takenSubjects.setSectionId(res.getInt("section__fk"));
      takenSubjects.setSubjectId(res.getString("subject__fk"));
    }
    return takenSubjects;
  }

  public ResultSet readCurrentSubjects(int id) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(STS_INI.get("read", "read_currentsubjects"));
    ps.setInt(1, id);
    return ps.executeQuery();
  }

  public ResultSet readAvailableSubjects(int id) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(STS_INI.get("read", "read_availablesubjects"));
    ps.setInt(1, id);
    return ps.executeQuery();
  }

  // TODO: Place here the query which finds which subject has been used and are not.

  public int updateAllById(TakenSubjects takenSubjects) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(STS_INI.get("update", "update_all_byid"));
    ps.setInt(1, takenSubjects.getSectionId());
    ps.setString(2, takenSubjects.getSubjectId());
    ps.setInt(3, takenSubjects.getId());
    return ps.executeUpdate();
  }

  // TODO: There are left query functions.

  public int deleteAll() throws SQLException {
    ps = DatabaseManager.con.prepareStatement(STS_INI.get("delete", "delete_all"));
    return ps.executeUpdate();
  }

  public int deleteSubjectFromSection(int sectionId, String subjectId) throws SQLException {
    ps = DatabaseManager.con.prepareStatement(STS_INI.get("delete", "delete_byid"));
    ps.setInt(1, sectionId);
    ps.setString(2, subjectId);
    return ps.executeUpdate();
  }
} // -- End of SectionTakenSubjectsQueries
