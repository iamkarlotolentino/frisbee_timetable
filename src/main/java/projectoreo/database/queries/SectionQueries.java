package projectoreo.database.queries;

import org.ini4j.Ini;
import projectoreo.database.DatabaseManager;
import projectoreo.models.Section;
import projectoreo.utils.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SectionQueries {
  private DatabaseManager DB_MANAGER;
  private PreparedStatement ps;
  private ResultSet res;

  private Ini SECTION_INI;

  public SectionQueries(DatabaseManager DB_MANAGER) {
    this.DB_MANAGER = DB_MANAGER;
    try {
      SECTION_INI = new Ini(new File(ResourceLoader.getResource("sql/sectionsql.ini").toURI()));
      DatabaseManager.LogUtils.successLoadIni("sectionsql.ini");
    } catch (IOException | URISyntaxException e) {
      DatabaseManager.LogUtils.failLoadIni("sectionsql.ini");
    }
  }

  public int create(Section section) throws SQLException {
    ps =
        DatabaseManager.con.prepareStatement(SECTION_INI.get("create", "create_section"));
    ps.setString(1, section.getName());
    return ps.executeUpdate();
  }

  public ResultSet readAll() throws SQLException {
    ps = DatabaseManager.con.prepareStatement(SECTION_INI.get("read", "read_all"));
    return ps.executeQuery();
  }

  public Section readSectionById(int id) throws SQLException {
    ps =
        DatabaseManager.con.prepareStatement(SECTION_INI.get("read", "read_all_byid"));
    ps.setInt(1, id);
    res = ps.executeQuery();

    // TODO: Take note the studentsCount if we'll consider modifying the query instead.
    return new Section(
        res.getInt("section_id"), res.getString("name"), 0);
  }

  public int updateNameById(Section section) throws SQLException {

    ps =
        DatabaseManager.con.prepareStatement(SECTION_INI.get("update", "update_name_byid"));
    ps.setString(1, section.getName());
    ps.setInt(2, section.getId());
    return ps.executeUpdate();
  }

  public int deleteAll() throws SQLException {
    ps =
        DatabaseManager.con.prepareStatement(SECTION_INI.get("delete", "delete_all"));
    return ps.executeUpdate();
  }

  public int deleteById(int id) throws SQLException {
    ps =
        DatabaseManager.con.prepareStatement(SECTION_INI.get("delete", "delete_byid"));
    ps.setInt(1, id);
    return ps.executeUpdate();
  }

  public int deleteSectionByName(int id) throws SQLException {
    ps =
        DatabaseManager.con.prepareStatement(SECTION_INI.get("delete", "delete_byname"));
    ps.setInt(1, id);
    return ps.executeUpdate();
  }
  // ---- End of SectionQueries
} // -- End of SectionQueries
