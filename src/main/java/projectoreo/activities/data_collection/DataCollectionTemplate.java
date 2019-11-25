package projectoreo.activities.data_collection;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import projectoreo.activities.front.FrontController;
import projectoreo.database.DatabaseManager;
import projectoreo.utils.Controller;
import projectoreo.utils.ControllersDispatcher;

import java.net.URL;
import java.util.ResourceBundle;

public class DataCollectionTemplate implements Initializable, Controller {

  protected static final DatabaseManager DB_MANAGER = DatabaseManager.getInstance();

  @FXML protected AnchorPane contentPane;
  @FXML protected TextField searchField;
  @FXML protected Button search;
  @FXML protected Button createNew;
  @FXML protected Button deleteSelected;
  @FXML protected Button editSelected;
  @FXML protected Button importFromCSV;
  @FXML protected Button viewSummaryOfData;
  @FXML protected Button exportToCSV;
  @FXML protected Button deleteAllData;
  @FXML protected Button refresh;

  @FXML private TableView tableView;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setup();
    listeners();
  }

  @Override
  public void setup() {}

  @Override
  public void listeners() {
    // An event when there's no longer selected student item.
    // Thus, we need to disable the respective buttons.
    tableView
        .getSelectionModel()
        .selectedIndexProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue.intValue() == -1) {
                deleteSelected.setDisable(true);
                editSelected.setDisable(true);
              }
            });
    // Event when there's a selected student item.
    // Thus, we enable the respective buttons.
    tableView.setOnMouseClicked(
        click -> {
          if (tableView.getSelectionModel().getFocusedIndex() > -1) {
            deleteSelected.setDisable(false);
            editSelected.setDisable(false);
          }
        });
  }

  public void setCurrentStatus(String status, boolean isWorking) {
    ((FrontController) ControllersDispatcher.getInstance().get(FrontController.class))
        .setCurrentStatus(status, isWorking);
  }
}
