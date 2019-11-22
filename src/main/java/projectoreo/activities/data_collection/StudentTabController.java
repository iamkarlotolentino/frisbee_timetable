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
import javafx.stage.FileChooser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import projectoreo.activities.front.FrontController;
import projectoreo.dialogs.DialogAlert;
import projectoreo.dialogs.DialogType;
import projectoreo.dialogs.NewStudentDialog;
import projectoreo.models.Section;
import projectoreo.models.Student;
import projectoreo.utils.ControllersDispatcher;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudentTabController extends DataCollectionTemplate {

  private static final Logger LOGGER = LoggerFactory.getLogger(StudentTabController.class);

  private static String STUDENT_SELECTED_DELETE = "Selected student has been deleted";
  private static String STUDENT_DELETE_ALL = "All data in the student has been wiped";
  private static String STUDENT_NEW = "New student data has been added";
  private static String STUDENT_EDIT = "Selected student has been updated";
  private static String STUDENT_WORK_REFRESH_START =
      "Populating student data to the table. Working...";
  private static String STUDENT_WORK_REFRESH_FINISH =
      "Successfully populated all student data in the table. Done!";
  private static String STUDENT_WORK_REFRESH_FAILED =
      "We have encountered an error while populating student data. Failed!";

  private Task<ObservableList<Student>> loadStudentTask;
  private Task<Boolean> importCSVTask;
  private Task<Boolean> exportCSVTask;

  @FXML private TableView<Student> tableView;

  @FXML private TableColumn<Student, String> idColumn;
  @FXML private TableColumn<Student, String> firstNameColumn;
  @FXML private TableColumn<Student, String> middleNameColumn;
  @FXML private TableColumn<Student, String> lastNameColumn;

  StudentTabController() {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setup();
    listeners();
  }

  @Override
  public void setup() {
    super.setup();
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    middleNameColumn.setCellValueFactory(new PropertyValueFactory<>("middleName"));
    lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
  }

  @Override
  public void listeners() {
    super.listeners();
    // Populating the data in the tableView
    refresh.setOnAction(
        click -> {
          loadStudentTask();

          ExecutorService executorService = Executors.newFixedThreadPool(1);
          executorService.execute(loadStudentTask);
          executorService.shutdown();
        });
    // Create a dialog to input information, then entered in the database
    createNew.setOnAction(
        click -> {
          NewStudentDialog newStudentDialog = new NewStudentDialog(DialogType.CREATE, null);
          Optional<Student> student = newStudentDialog.showAndWait();
          student.ifPresent(
              studentData -> {
                try {
                  DB_MANAGER.getStudentQueries().create(studentData);
                  tableView.getItems().add(studentData);
                  setCurrentStatus(STUDENT_NEW, false);
                } catch (SQLException e) {
                  if (e.getErrorCode() == 19) DialogAlert.requestDuplicateError("[STUDENT_ID]");
                  else e.printStackTrace();
                }
              });
        });
    editSelected.setOnAction(
        click -> {
          if (tableView.getSelectionModel().getSelectedIndex() > -1) {
            NewStudentDialog editSubject =
                new NewStudentDialog(
                    DialogType.EDIT, tableView.getSelectionModel().getSelectedItem());
            Optional<Student> student = editSubject.showAndWait();
            student.ifPresent(
                studentData -> {
                  try {
                    DB_MANAGER.getStudentQueries().updateAllById(studentData);
                    tableView
                        .getItems()
                        .set(tableView.getSelectionModel().getSelectedIndex(), studentData);
                    setCurrentStatus(STUDENT_EDIT, false);
                  } catch (SQLException e) {
                    if (e.getErrorCode() == 19) DialogAlert.requestDuplicateError("[STUDENT_ID]");
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
                // If did not pushed through
                if (DB_MANAGER
                        .getStudentQueries()
                        .deleteById(tableView.getSelectionModel().getSelectedItem().getId())
                    != 1) {
                  DialogAlert.requestSelectedNotUpdatedWarning();
                  setCurrentStatus(
                      "Please use force-refresh first to delete the new item (selected item)",
                      false);
                } else {
                  tableView.getItems().remove(tableView.getSelectionModel().getSelectedIndex());
                  setCurrentStatus(STUDENT_SELECTED_DELETE, false);
                }
              } catch (SQLException e) {
                e.printStackTrace();
              }
            }
          }
        });
    importFromCSV.setOnAction(
        click -> {
          FileChooser importCSV = new FileChooser();
          // TODO: Detect if the dialog has cancelled to prevent the error tracing
          importCSVTask(
              importCSV.showOpenDialog(
                  ((FrontController) ControllersDispatcher.getInstance().get(FrontController.class))
                      .getPrimaryStage()));

          ExecutorService executorService = Executors.newFixedThreadPool(1);
          executorService.execute(importCSVTask);
          executorService.shutdown();
        });
    viewSummaryOfData.setOnAction(
        click -> {
          System.out.println(click.getSource());
        });
    exportToCSV.setOnAction(
        click -> {
          FileChooser exportCSV = new FileChooser();
          exportCSVTask(
              exportCSV.showSaveDialog(
                  ((FrontController) ControllersDispatcher.getInstance().get(FrontController.class))
                      .getPrimaryStage()));
        });
    deleteAllData.setOnAction(
        click -> {
          Optional<ButtonType> result = DialogAlert.requestDeleteAllConfirmation();
          if (result.get() == ButtonType.OK) {
            try {
              DB_MANAGER.getStudentQueries().deleteAll();
              tableView.getItems().clear();
              setCurrentStatus(STUDENT_DELETE_ALL, false);
            } catch (SQLException e) {
              e.printStackTrace();
            }
          }
        });
  }

  private void exportCSVTask(File selectedFile) {
    importFromCSV.setDisable(true);
    exportToCSV.setDisable(true);
    setCurrentStatus("Exporting CSV File from " + selectedFile.getPath(), true);

    exportCSVTask =
        new Task<Boolean>() {
          @Override
          protected Boolean call() throws Exception {
            return true;
          }
        };
  }

  private void loadStudentTask() {
    refresh.setDisable(true);
    setCurrentStatus(STUDENT_WORK_REFRESH_START, true);
    loadStudentTask =
        new Task<ObservableList<Student>>() {
          @Override
          protected ObservableList<Student> call() {
            ObservableList<Student> studentList = FXCollections.observableArrayList();
            try {
              ResultSet res = DB_MANAGER.getStudentQueries().readAll();
              while (res.next()) {
                studentList.add(
                    new Student(
                        res.getString("student_id"),
                        res.getString("first_name"),
                        res.getString("middle_name"),
                        res.getString("last_name"),
                        DB_MANAGER.getSectionQueries().readSectionById(res.getInt("section__fk"))));
              }
              res.close();
            } catch (SQLException e) {
              e.printStackTrace();
            }
            return studentList;
          }
        };
    loadStudentTask.setOnFailed(
        failed -> {
          refresh.setDisable(false);
          setCurrentStatus(STUDENT_WORK_REFRESH_FAILED, false);
        });
    loadStudentTask.setOnSucceeded(
        succeeded -> {
          refresh.setDisable(false);
          tableView.setItems(loadStudentTask.getValue());
          setCurrentStatus(STUDENT_WORK_REFRESH_FINISH, false);
        });
  }

  private void importCSVTask(File selectedFile) {
    importFromCSV.setDisable(true);
    exportToCSV.setDisable(true);
    setCurrentStatus("Importing CSV file from" + selectedFile.getPath(), true);
    importCSVTask =
        new Task<Boolean>() {
          @Override
          protected Boolean call() throws Exception {
            // TODO: Detect if there was a duplicate while parsing. Skip then alert at the end
            CSVParser parser =
                CSVParser.parse(selectedFile, Charset.defaultCharset(), CSVFormat.DEFAULT);
            for (CSVRecord csvRecord : parser) {
              DB_MANAGER
                  .getStudentQueries()
                  .create(
                      new Student(
                          csvRecord.get(0),
                          csvRecord.get(1),
                          csvRecord.get(2),
                          csvRecord.get(3),
                          new Section(Integer.valueOf(csvRecord.get(4)), "", 0)));
            }
            return true;
          }
        };
    importCSVTask.setOnFailed(
        failed -> {
          LOGGER.error(failed.getSource().getMessage(), failed.getSource().getException());
          DialogAlert.requestDuplicateError("[STUDENT_ID]");
          importCSVTask.cancel();
          setCurrentStatus("Failed to import CSV file", false);
          importFromCSV.setDisable(false);
          exportToCSV.setDisable(false);
        });
    importCSVTask.setOnSucceeded(
        succeeded -> {
          setCurrentStatus(
              "Successfully imported the CSV file from " + selectedFile.getPath(), false);
          refresh.fire();
          importFromCSV.setDisable(false);
          exportToCSV.setDisable(false);
        });
  }

  AnchorPane getStudentTab() {
    return contentPane;
  }
}
