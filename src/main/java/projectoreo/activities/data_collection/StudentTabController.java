package projectoreo.activities.data_collection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import projectoreo.dialogs.NewStudentDialog;
import projectoreo.dialogs.SQLErrorAlert;
import projectoreo.models.Student;

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

  private Task<ObservableList<Student>> loadStudentTask;
  private Task<Boolean> importCSVTask;
  private Task<Boolean> exportCSVTask;

  @FXML private TableView<Student> tableView;

  @FXML private TableColumn<Student, String> idColumn;
  @FXML private TableColumn<Student, String> firstNameColumn;
  @FXML private TableColumn<Student, String> middleNameColumn;
  @FXML private TableColumn<Student, String> lastNameColumn;

  public StudentTabController() {}

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
          loadStudentTask.run();
        });
    // Create a dialog to input information, then entered in the database
    createNew.setOnAction(
        click -> {
          NewStudentDialog dialog = new NewStudentDialog(NewStudentDialog.Type.CREATE);
          dialog.show();
          if (dialog.getStudentData() != null) {
            try {
              DB_MANAGER.getStudentQueries().create(dialog.getStudentData());
              tableView.getItems().add(dialog.getStudentData());
              setCurrentStatus("New student data has been added.", false);
            } catch (SQLException e) {
              promptDuplicatedError(e);
            }
          }
        });
    deleteSelected.setOnAction(
        click -> {
          if (tableView.getSelectionModel().getSelectedIndex() > -1) {
            Alert selectedDeleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
            selectedDeleteAlert.setTitle("You're deleting a student");
            selectedDeleteAlert.setHeaderText("Delete a student");
            selectedDeleteAlert.setContentText(
                "You are deleting a particular data of a student. "
                    + "Please proceed with utmost certainty.");
            Optional<ButtonType> result = selectedDeleteAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
              try {
                DB_MANAGER
                    .getStudentQueries()
                    .deleteById(tableView.getSelectionModel().getSelectedItem().getId());
                tableView.getItems().remove(tableView.getSelectionModel().getSelectedIndex());
                setCurrentStatus("Particular student has been deleted from the database.", false);
              } catch (SQLException e) {
                e.printStackTrace();
              }
            }
          }
        });
    editSelected.setOnAction(
        click -> {
          NewStudentDialog editStudentDialog = new NewStudentDialog(NewStudentDialog.Type.EDIT);
          editStudentDialog.setStudentData(tableView.getSelectionModel().getSelectedItem());
          editStudentDialog.populateData();
          editStudentDialog.show();
          try {
            DB_MANAGER.getStudentQueries().updateAllById(editStudentDialog.getStudentData());
            tableView
                .getItems()
                .set(
                    tableView.getSelectionModel().getSelectedIndex(),
                    editStudentDialog.getStudentData());
            setCurrentStatus("Particular student has been updated.", false);
          } catch (SQLException e) {
            promptDuplicatedError(e);
          }
        });
    importFromCSV.setOnAction(
        click -> {
          FileChooser importCSV = new FileChooser();
          importCSVTask(importCSV.showOpenDialog(null));

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
          exportCSVTask(exportCSV.showSaveDialog(null));
        });
    deleteAllData.setOnAction(
        click -> {
          Optional<ButtonType> result = SQLErrorAlert.requestConfirmationWarning();
          if (result.get() == ButtonType.OK) {
            try {
              DB_MANAGER.getStudentQueries().deleteAll();
              refresh.fire();
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
    setCurrentStatus("Populating student data in the table", true);
    loadStudentTask =
        new Task<ObservableList<Student>>() {
          @Override
          protected ObservableList<Student> call() throws Exception {
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
                        res.getInt("section__fk")));
              }
              res.close();
            } catch (SQLException e) {
              e.printStackTrace();
            }
            return studentList;
          }
        };
    loadStudentTask.setOnSucceeded(
        succeeded -> {
          tableView.setItems(loadStudentTask.getValue());
          setCurrentStatus("Successfully populated all students data", false);
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
                          csvRecord.get(2),
                          1));
            }
            return true;
          }
        };
    importCSVTask.setOnFailed(
        failed -> {
          SQLErrorAlert.requestDuplicateError("[STUDENT_ID]");
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

  private void promptDuplicatedError(SQLException e) {
    // Duplicate studentID as enforced by constraints
    if (e.getErrorCode() == 19) {
      SQLErrorAlert.requestDuplicateError("[STUDENT_ID]");
    } else {
      e.printStackTrace();
    }
  }

  public AnchorPane getStudentTab() {
    return contentPane;
  }
}
