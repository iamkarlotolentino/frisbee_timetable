package projectoreo.activities.data_collection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import projectoreo.database.columns.SubjectColumn;
import projectoreo.dialogs.utils.DialogAlert;
import projectoreo.dialogs.utils.DialogType;
import projectoreo.dialogs.NewSubjectDialog;
import projectoreo.models.RoomType;
import projectoreo.models.Subject;

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
  @FXML private TableColumn<Subject, String> unitsColumn;
  @FXML private TableColumn<Subject, String> requiredHoursColumn;
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
    typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    unitsColumn.setCellValueFactory(new PropertyValueFactory<>("units"));
      requiredHoursColumn.setCellValueFactory(new PropertyValueFactory<>("requiredHours"));
    sectionsUsedColumn.setCellValueFactory(new PropertyValueFactory<>("sectionsUsed"));
  }

  @Override
  public void listeners() {
    super.listeners();
    createNew.setOnAction(
        click -> {
          NewSubjectDialog newSubjectDialog = new NewSubjectDialog(DialogType.CREATE, null);
          Optional<Subject> subject = newSubjectDialog.showAndWait();
          subject.ifPresent(
              subjectData -> {
                try {
                  DB_MANAGER.getSubjectQueries().create(subjectData);
                  tableView.getItems().add(subjectData);
                  setCurrentStatus("New subject data has been added", false);
                } catch (SQLException e) {
                  if (e.getErrorCode() == 19) DialogAlert.requestDuplicateError("[SUBJECT_ID]");
                  else e.printStackTrace();
                }
              });
        });
    editSelected.setOnAction(
        click -> {
          if (tableView.getSelectionModel().getSelectedIndex() > -1) {
            NewSubjectDialog editSubject =
                new NewSubjectDialog(
                    DialogType.EDIT, tableView.getSelectionModel().getSelectedItem());
            Optional<Subject> subject = editSubject.showAndWait();
            subject.ifPresent(
                subjectData -> {
                  try {
                    DB_MANAGER.getSubjectQueries().updateById(subjectData);
                    tableView
                        .getItems()
                        .set(tableView.getSelectionModel().getSelectedIndex(), subjectData);
                  } catch (SQLException e) {
                    if (e.getErrorCode() == 19) DialogAlert.requestDuplicateError("[SUBJECT_ID]");
                    else e.printStackTrace();
                  }
                });
          }
        });
    deleteSelected.setOnAction(
        click -> {
          if (tableView.getSelectionModel().getSelectedIndex() > -1) {
            Optional<ButtonType> result = DialogAlert.requestDeleteSelectedConfirmation();
            if (result.get() == ButtonType.OK) {
              try {
                DB_MANAGER
                    .getSubjectQueries()
                    .deleteById(tableView.getSelectionModel().getSelectedItem().getId());
                tableView.getItems().remove(tableView.getSelectionModel().getSelectedIndex());
              } catch (SQLException e) {
                e.printStackTrace();
              }
            }
          }
        });

    importFromCSV.setOnAction(click -> {});
    viewSummaryOfData.setOnAction(click -> {});
    exportToCSV.setOnAction(click -> {});
    deleteAllData.setOnAction(
        click -> {
          Optional<ButtonType> result = DialogAlert.requestDeleteAllConfirmation();
          if (result.get() == ButtonType.OK) {
            try {
              DB_MANAGER.getSubjectQueries().deleteAll();
              tableView.getItems().clear();
            } catch (SQLException e) {
              e.printStackTrace();
            }
          }
        });
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
                            res.getString(SubjectColumn.SUBJECT_ID.get()),
                        res.getString(SubjectColumn.NAME.get()),
                        res.getInt(SubjectColumn.UNITS.get()),
                        res.getInt(SubjectColumn.REQUIRED_HOURS.get()),
                        new RoomType(res.getInt("type_id"), res.getString(6)),
                        res.getInt("total_count")));
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
