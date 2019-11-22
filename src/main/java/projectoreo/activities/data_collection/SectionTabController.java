package projectoreo.activities.data_collection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import projectoreo.dialogs.*;
import projectoreo.models.Section;
import projectoreo.utils.ActivityLoader;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SectionTabController extends DataCollectionTemplate {

  private static final String SECTION_SELECTED_DELETE = "Selected section has been deleted";
  private static final String SECTION_DELETE_ALL = "All data in the section has been wiped";
  private static final String SECTION_NEW = "New section data has been added";
  private static final String SECTION_EDIT = "Selected section has been updated";
  private static final String SECTION_WORK_REFRESH_START =
      "Populating section data to the table. Working...";
  private static final String SECTION_WORK_REFRESH_FINISH =
      "Successfully populated all section data in the table. Done!";
  private static final String SECTION_WORK_REFRESH_FAILED =
      "We have encountered an error while populating section data. Failed!";

  private Task<ObservableList<Section>> loadSectionTask;

  @FXML private TableView<Section> tableView;

  @FXML private TableColumn<Section, String> idColumn;
  @FXML private TableColumn<Section, String> nameColumn;
  @FXML private TableColumn<Section, String> studentsCountColumn;

  @FXML private Button viewStudentsB;
  @FXML private Button manageSubjectsB;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
  }

  @Override
  public void setup() {
    super.setup();
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    studentsCountColumn.setCellValueFactory(new PropertyValueFactory<>("studentsCount"));
  }

  @Override
  public void listeners() {
    super.listeners();

    // Populating data in the table view
    refresh.setOnAction(
        click -> {
          loadSectionTask();

          ExecutorService executorService = Executors.newFixedThreadPool(1);
          executorService.execute(loadSectionTask);
          executorService.shutdown();
        });
    // --------------------------------------------------------------
    createNew.setOnAction(
        click -> {
          NewSectionDialog newSectionDialog = new NewSectionDialog(DialogType.CREATE, null);
          Optional<String> section = newSectionDialog.showAndWait();
          section.ifPresent(
              name -> {
                try {
                  Section sectionData = new Section(0, name, 0);
                  DB_MANAGER.getSectionQueries().create(sectionData);
                  tableView.getItems().add(sectionData);
                  setCurrentStatus(SECTION_NEW, false);
                } catch (SQLException e) {
                  if (e.getErrorCode() == 19) DialogAlert.requestDuplicateError("[SECTION_ID]");
                  else e.printStackTrace();
                }
              });
        });
    // --------------------------------------------------------------
    editSelected.setOnAction(
        click -> {
          if (tableView.getSelectionModel().getSelectedIndex() > -1) {
            NewSectionDialog editSectionDialog =
                new NewSectionDialog(
                    DialogType.EDIT, tableView.getSelectionModel().getSelectedItem());
            Optional<String> result = editSectionDialog.showAndWait();
            result.ifPresent(
                name -> {
                  try {
                    Section section = tableView.getSelectionModel().getSelectedItem();
                    Section updatedSection =
                        new Section(section.getId(), name, section.getStudentsCount());
                    DB_MANAGER.getSectionQueries().updateNameById(updatedSection);
                    tableView
                        .getItems()
                        .set(tableView.getSelectionModel().getSelectedIndex(), updatedSection);
                    setCurrentStatus(SECTION_EDIT, false);
                  } catch (SQLException e) {
                    if (e.getErrorCode() == 19) DialogAlert.requestDuplicateError("[SECTION_ID]");
                    else e.printStackTrace();
                  }
                });
          }
        });
    // --------------------------------------------------------------
    deleteSelected.setOnAction(
        click -> {
          Optional<ButtonType> result = DialogAlert.requestDeleteSelectedConfirmation();
          result.ifPresent(
              buttonType -> {
                if (result.get() == ButtonType.OK) {
                  try {
                    // If did not pushed through
                    if (DB_MANAGER
                            .getSectionQueries()
                            .deleteById(tableView.getSelectionModel().getSelectedItem().getId())
                        != 1) {
                      DialogAlert.requestSelectedNotUpdatedWarning();
                      setCurrentStatus(
                          "Please use force-refresh first to delete the new item (selected item)",
                          false);
                    } else {
                      tableView.getItems().remove(tableView.getSelectionModel().getSelectedIndex());
                      setCurrentStatus(SECTION_SELECTED_DELETE, false);
                    }
                  } catch (SQLException e) {
                    e.printStackTrace();
                  }
                }
              });
        });

    // Extending a functionality to activate extra buttons
    tableView
        .getSelectionModel()
        .selectedIndexProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue.intValue() == -1) {
                deleteSelected.setDisable(true);
                editSelected.setDisable(true);
                viewStudentsB.setDisable(true);
                manageSubjectsB.setDisable(true);
              }
            });
    tableView.setOnMouseClicked(
        click -> {
          if (tableView.getSelectionModel().getFocusedIndex() > -1) {
            viewStudentsB.setDisable(false);
            manageSubjectsB.setDisable(false);
            deleteSelected.setDisable(false);
            editSelected.setDisable(false);
          }
        });
    // --------------------------------------------------------------
    viewStudentsB.setOnAction(
        click -> {
          // If empty or no students in the section
          if (tableView.getSelectionModel().getSelectedItem().getStudentsCount() < 1) {
            Alert noStudentsAlert = new Alert(Alert.AlertType.INFORMATION);
            noStudentsAlert.setTitle("Ooops..");
            noStudentsAlert.setHeaderText("No student data found");
            noStudentsAlert.setContentText(
                "There is no student in this particular section. "
                    + "Make sure to add a student first in this section.");
            noStudentsAlert.showAndWait();
          } else new ViewStudentsDialog(tableView.getSelectionModel().getSelectedItem());
        });
    manageSubjectsB.setOnAction(
        click -> {
          ActivityLoader intent = new ActivityLoader();
          intent.setLocationPath("views/manage_subjects.fxml");
          intent.setActivityController(
              new ManageSubjectsDialog(tableView.getSelectionModel().getSelectedItem().getId()));
          intent.onFinish(
              finish -> {
                ((ManageSubjectsDialog) intent.getActivityController())
                    .getDialog()
                    .getDialogPane()
                    .setContent(intent.getActivity());
                ((ManageSubjectsDialog) intent.getActivityController()).getDialog().showAndWait();
              });
          intent.start();
        });
    // --------------------------------------------------------------
    deleteAllData.setOnAction(
        click -> {
          Optional<ButtonType> result = DialogAlert.requestDeleteAllConfirmation();
          result.ifPresent(
              buttonType -> {
                if (result.get() == ButtonType.OK) {
                  try {
                    DB_MANAGER.getSectionQueries().deleteAll();
                    tableView.getItems().clear();
                    setCurrentStatus(SECTION_DELETE_ALL, false);
                  } catch (SQLException e) {
                    e.printStackTrace();
                  }
                }
              });
        });
  }

  private void loadSectionTask() {
    refresh.setDisable(true);
    setCurrentStatus(SECTION_WORK_REFRESH_START, true);
    loadSectionTask =
        new Task<ObservableList<Section>>() {
          @Override
          protected ObservableList<Section> call() {
            ObservableList<Section> sectionList = FXCollections.observableArrayList();
            try {
              ResultSet res = DB_MANAGER.getSectionQueries().readAll();
              while (res.next()) {
                sectionList.add(
                    new Section(
                        res.getInt("section_id"),
                        res.getString("name"),
                        res.getInt("students_count")));
              }
              res.close();
            } catch (SQLException e) {
              e.printStackTrace();
            }
            return sectionList;
          }
        };
    loadSectionTask.setOnFailed(
        failed -> {
          refresh.setDisable(false);
          setCurrentStatus(SECTION_WORK_REFRESH_FAILED, false);
        });
    loadSectionTask.setOnSucceeded(
        succeeded -> {
          refresh.setDisable(false);
          tableView.setItems(loadSectionTask.getValue());
          setCurrentStatus(SECTION_WORK_REFRESH_FINISH, false);
        });
  }

  AnchorPane getSectionTab() {
    return contentPane;
  }
}
