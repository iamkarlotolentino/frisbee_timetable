package projectoreo.activities.data_collection;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import projectoreo.activities.front.FrontController;
import projectoreo.dialogs.DialogType;
import projectoreo.dialogs.NewSubjectDialog;
import projectoreo.dialogs.SQLErrorAlert;
import projectoreo.managers.DatabaseManager;
import projectoreo.models.RoomType;
import projectoreo.models.Subject;
import projectoreo.utils.ControllersDispatcher;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class SubjectTabController extends DataCollectionTemplate {

  private Task<ObservableList<Subject>> loadSubjectTask;

  @FXML private TableView<Subject> tableView;

  @FXML private TableColumn<Subject, String> idColumn;
  @FXML private TableColumn<Subject, String> nameColumn;
  @FXML private TableColumn<Subject, String> typeColumn;
  @FXML private TableColumn<Subject, String> sectionsUsedColumn;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setup();
    listeners();
  }

  @Override
  public void setup() {
    super.setup();
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    typeColumn.setCellValueFactory(
        param -> {
          if (param.getValue() != null && param.getValue().getType() != null) {
            return new SimpleStringProperty(param.getValue().getType().getType());
          } else return new SimpleStringProperty("<invalid>");
        });
    //    sectionsUsedColumn.setCellValueFactory(new PropertyValueFactory<>(""));
  }

  @Override
  public void listeners() {
    super.listeners();
    createNew.setOnAction(
        click -> {
          NewSubjectDialog newSubjectDialog = new NewSubjectDialog(DialogType.CREATE);
          Optional<Subject> subject = newSubjectDialog.showAndWait();
          subject.ifPresent(
              subjectData -> {
                try {
                  DB_MANAGER.getSubjectQueries().create(subjectData);
                } catch (SQLException e) {
                  if (e.getErrorCode() == 19) {
                    SQLErrorAlert.requestDuplicateError("[SUBJECT_ID]");
                  } else e.printStackTrace();
                }
              });
        });
    deleteSelected.setOnAction(click -> {});
    editSelected.setOnAction(click -> {});

    importFromCSV.setOnAction(click -> {});
    viewSummaryOfData.setOnAction(click -> {});
    exportToCSV.setOnAction(click -> {});
    deleteAllData.setOnAction(click -> {});
    refresh.setOnAction(
        click -> {
          loadSubjectTask();
          loadSubjectTask.run();
        });
  }

  private void loadSubjectTask() {
    loadSubjectTask =
        new Task<ObservableList<Subject>>() {
          @Override
          protected ObservableList<Subject> call() {
            ObservableList<Subject> subjectList = FXCollections.observableArrayList();
            try {
              ResultSet res = DB_MANAGER.getSubjectQueries().readAll();
              while (res.next()) {
                subjectList.add(
                    new Subject(
                        res.getString("subject_id"),
                        res.getString("subject_name"),
                        res.getString("desc"),
                        new RoomType(res.getInt("type_id"), res.getString("room_name"))));
              }
              res.close();
            } catch (SQLException e) {
              e.printStackTrace();
            }
            return subjectList;
          }
        };
    loadSubjectTask.setOnRunning(
        running -> {
          setCurrentStatus("Populating data of all subjects...", true);
        });
    loadSubjectTask.setOnSucceeded(
        succeeded -> {
          tableView.setItems(loadSubjectTask.getValue());
          setCurrentStatus("Successfully populated all subjects data", false);
        });
  }

  public AnchorPane getSubjectTab() {
    return contentPane;
  }
}
