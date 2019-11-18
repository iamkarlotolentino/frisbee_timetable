package projectoreo.activities.data_collection;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import projectoreo.managers.DatabaseManager;
import projectoreo.utils.Controller;

import java.net.URL;
import java.util.ResourceBundle;

public class SubjectTabController implements Initializable, Controller {

  private static final DatabaseManager DB_MANAGER = DatabaseManager.getInstance();

  @FXML private AnchorPane subjectTab;

  @FXML private TextField searchField;
  @FXML private Button search;

  @FXML private TableColumn idColumn;
  @FXML private TableColumn nameColumn;
  @FXML private TableColumn typeColumn;
  @FXML private TableColumn sectionsUsedColumn;

  @FXML private Button createNew;
  @FXML private Button deleteSelected;
  @FXML private Button editSelected;

  @FXML private Button importFromCSV;
  @FXML private Button viewSummaryOfData;
  @FXML private Button exportToCSV;
  @FXML private Button deleteAllData;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setup();
    listeners();
  }

  @Override
  public void setup() {}

  @Override
  public void listeners() {}

  public AnchorPane getSubjectTab() {
    return subjectTab;
  }
}
