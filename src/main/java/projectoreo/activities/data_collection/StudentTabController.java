package projectoreo.activities.data_collection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import projectoreo.activities.front.FrontController;
import projectoreo.dialogs.NewStudentDialog;
import projectoreo.managers.DatabaseManager;
import projectoreo.models.Student;
import projectoreo.utils.Controller;
import projectoreo.utils.ControllersDispatcher;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class StudentTabController implements Initializable, Controller {

  private static final DatabaseManager DB_MANAGER = DatabaseManager.getInstance();
  private static final Label statusBar =
      ((FrontController) ControllersDispatcher.getInstance().get(FrontController.class))
          .getCurrentStatus();

  private Task<ObservableList<Student>> loadStudentTask;

  private ObservableList<Student> students;

  @FXML private AnchorPane studentTab;

  @FXML private TextField searchField;
  @FXML private Button search;

  @FXML private TableView<Student> tableView;
  @FXML private TableColumn<Student, String> idColumn;
  @FXML private TableColumn<Student, String> firstNameColumn;
  @FXML private TableColumn<Student, String> middleNameColumn;
  @FXML private TableColumn<Student, String> lastNameColumn;

  @FXML private Button createNewStudent;
  @FXML private Button deleteSelectedStudent;
  @FXML private Button editSelectedStudent;

  @FXML private Button importFromCSV;
  @FXML private Button viewSummaryOfData;
  @FXML private Button exportToCSV;
  @FXML private Button deleteAllData;
  @FXML private Button refresh;

  public StudentTabController() {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    students = FXCollections.observableArrayList();
    setup();
    listeners();
  }

  private void loadStudentTask() {
    loadStudentTask =
        new Task<ObservableList<Student>>() {
          @Override
          protected ObservableList<Student> call() throws Exception {
            ObservableList<Student> studentList = FXCollections.observableArrayList();
            try {
              ResultSet res = DB_MANAGER.getStudentQueries().readAll();
              while (res.next()) {
                for (int i = 0; i < res.getMetaData().getColumnCount(); i++) {}
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
    loadStudentTask.setOnRunning(
        running -> {
          ((FrontController) ControllersDispatcher.getInstance().get(FrontController.class))
              .setCurrentStatus("Populating data of all students...");
        });
    loadStudentTask.setOnSucceeded(
        succeeded -> {
          tableView.setItems(loadStudentTask.getValue());
          ((FrontController) ControllersDispatcher.getInstance().get(FrontController.class))
              .setCurrentStatus("Successfully populated all students data");
        });
  }

  @Override
  public void setup() {
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    middleNameColumn.setCellValueFactory(new PropertyValueFactory<>("middleName"));
    lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
  }

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
                deleteSelectedStudent.setDisable(true);
                editSelectedStudent.setDisable(true);
              }
            });
    // Event when there's a selected student item.
    // Thus, we enable the respective buttons.
    tableView.setOnMouseClicked(
        click -> {
          if (tableView.getSelectionModel().getFocusedIndex() > -1) {
            deleteSelectedStudent.setDisable(false);
            editSelectedStudent.setDisable(false);
          }
        });
    // Populating the data in the tableView
    refresh.setOnAction(
        click -> {
          loadStudentTask();
          loadStudentTask.run();
        });
    // Create a dialog to input information, then entered in the database
    createNewStudent.setOnAction(
        click -> {
          NewStudentDialog dialog = new NewStudentDialog(NewStudentDialog.Type.CREATE);
          dialog.show();
          if (dialog.getStudentData() != null) {
            try {
              DB_MANAGER.getStudentQueries().create(dialog.getStudentData());
              tableView.getItems().add(dialog.getStudentData());
              statusBar.setText("New student data has been added.");
            } catch (SQLException e) {
              promptDuplicatedError(e);
            }
          }
        });
    deleteSelectedStudent.setOnAction(
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
                statusBar.setText("Particular student has been deleted from the database.");
              } catch (SQLException e) {
                e.printStackTrace();
              }
            }
          }
        });
    editSelectedStudent.setOnAction(
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
            statusBar.setText("Particular student has been updated.");
          } catch (SQLException e) {
            promptDuplicatedError(e);
          }
        });
    importFromCSV.setOnAction(
        click -> {
          System.out.println(click.getSource());
        });
    viewSummaryOfData.setOnAction(
        click -> {
          System.out.println(click.getSource());
        });
    exportToCSV.setOnAction(
        click -> {
          System.out.println(click.getSource());
        });
    deleteAllData.setOnAction(
        click -> {
          Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
          deleteAlert.setTitle("THIS IS AN UNRECOVERABLE ACTION");
          deleteAlert.setHeaderText("Warning! Please think of your actions wisely!");
          deleteAlert.setContentText(
              "You are about to delete all the data about STUDENTS. "
                  + "By committing this, there won't be any backup to recover this action.");
          Optional<ButtonType> result = deleteAlert.showAndWait();
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

  private void promptDuplicatedError(SQLException e) {
    Alert error = new Alert(Alert.AlertType.WARNING);
    // Duplicate studentID as enforced by constraints
    if (e.getErrorCode() == 19) {
      error.setTitle("Existing Student ID");
      error.setHeaderText("Duplicate student ID has been detected");
      error.setContentText(
          "The entered student ID has been already used by other student."
              + " Conflict will happen if proceeds. Thus, please use other student ID instead");
      error.setAlertType(Alert.AlertType.ERROR);
      error.showAndWait();
    } else {
      e.printStackTrace();
    }
  }

  public AnchorPane getStudentTab() {
    return studentTab;
  }

  public void setStudentTab(AnchorPane studentTab) {
    this.studentTab = studentTab;
  }

  public void refreshData() {
    if (refresh != null) refresh.fire();
  }
}
