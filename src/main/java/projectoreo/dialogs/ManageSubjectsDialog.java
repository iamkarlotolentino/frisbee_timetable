package projectoreo.dialogs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import projectoreo.database.DatabaseManager;
import projectoreo.models.TakenSubjects;
import projectoreo.models.Subject;
import projectoreo.utils.Controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManageSubjectsDialog implements Initializable, Controller {

  private static final Logger LOGGER = LoggerFactory.getLogger(ManageSubjectsDialog.class);
  private final DatabaseManager DB_MANAGER = DatabaseManager.getInstance();

  private int sectionId;
  private Dialog dialog;

  private Task<ObservableList<Subject>> loadAvailableSubjectsTask;
  private Task<ObservableList<Subject>> loadCurrentSubjectsTask;

  @FXML private TextField filterAvailableSubjectsTF;
  @FXML private TextField filterCurrentSubjectsTF;

  @FXML private ListView<Subject> availableSubjectsLV;
  @FXML private ListView<Subject> currentSubjectsLV;

  @FXML private Button popB;
  @FXML private Button popAllB;
  @FXML private Button pushB;
  @FXML private Button pushAllB;

  public ManageSubjectsDialog(int sectionId) {
    this.sectionId = sectionId;

    dialog = new Dialog<>();
    dialog.setTitle("Manage Subjects");
    dialog.setHeaderText("Add/Remove Subjects");
    ButtonType doneB = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(doneB);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setup();
    listeners();
  }

  @Override
  public void setup() {
    loadAvailableSubjectsTask =
        new Task<ObservableList<Subject>>() {
          @Override
          protected ObservableList<Subject> call() throws Exception {
            ObservableList<Subject> subjects = FXCollections.observableArrayList();
            ResultSet res = DB_MANAGER.getTakenSubjectsQueries().readAvailableSubjects(sectionId);
            while (res.next()) {
              subjects.add(DB_MANAGER.getSubjectQueries().readById(res.getString("subject_id")));
            }
            return subjects;
          }
        };
    loadAvailableSubjectsTask.setOnFailed(
        failed -> {
          LOGGER.error(failed.getSource().getMessage(), failed.getSource().getException());
        });
    loadAvailableSubjectsTask.setOnSucceeded(
        succeeded -> {
          availableSubjectsLV.setItems(loadAvailableSubjectsTask.getValue());
        });
    loadAvailableSubjectsTask.run();

    // ------------------------------------------------------------------
    loadCurrentSubjectsTask =
        new Task<ObservableList<Subject>>() {
          @Override
          protected ObservableList<Subject> call() throws Exception {
            ObservableList<Subject> currentSubjects = FXCollections.observableArrayList();
            ResultSet res = DB_MANAGER.getTakenSubjectsQueries().readCurrentSubjects(sectionId);
            while (res.next()) {
              currentSubjects.add(
                  DB_MANAGER.getSubjectQueries().readById(res.getString("subject_id")));
            }
            return currentSubjects;
          }
        };
    loadCurrentSubjectsTask.setOnFailed(
        failed -> {
          LOGGER.error(failed.getSource().getMessage(), failed.getSource().getException());
        });
    loadCurrentSubjectsTask.setOnSucceeded(
        succeeded -> {
          currentSubjectsLV.setItems(loadCurrentSubjectsTask.getValue());
        });
    loadCurrentSubjectsTask.run();
  }

  @Override
  public void listeners() {
    popB.setOnAction(click -> popEvent());
    pushB.setOnAction(click -> pushEvent());
    popAllB.setOnAction(click -> popAllEvent());
    pushAllB.setOnAction(click -> pushAllEvent());
  }

  private void pushAllEvent() {
    currentSubjectsLV
        .getItems()
        .forEach(
            subject -> {
              push(subject.getId());
              availableSubjectsLV.getItems().add(subject);
            });
  }

  private void popAllEvent() {
    availableSubjectsLV
        .getItems()
        .forEach(
            subject -> {
              pop(subject.getId());
            });
    availableSubjectsLV.getItems().removeAll();
  }

  private void pushEvent() {
    if (availableSubjectsLV.getSelectionModel().getSelectedIndex() > -1) {
      push(availableSubjectsLV.getSelectionModel().getSelectedItem().getId());
      currentSubjectsLV.getItems().add(availableSubjectsLV.getSelectionModel().getSelectedItem());
      availableSubjectsLV
          .getItems()
          .remove(availableSubjectsLV.getSelectionModel().getSelectedIndex());
    }
  }

  private void popEvent() {
    if (currentSubjectsLV.getSelectionModel().getSelectedIndex() > -1) {
      pop(currentSubjectsLV.getSelectionModel().getSelectedItem().getId());
      availableSubjectsLV.getItems().add(currentSubjectsLV.getSelectionModel().getSelectedItem());
      currentSubjectsLV.getItems().remove(currentSubjectsLV.getSelectionModel().getSelectedIndex());
    }
  }

  private void pop(String subjectId) {
    try {
      DB_MANAGER.getTakenSubjectsQueries().deleteSubjectFromSection(sectionId, subjectId);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void push(String subjectId) {
    try {
      DB_MANAGER
          .getTakenSubjectsQueries()
          .create(new TakenSubjects(0, sectionId, subjectId));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Dialog getDialog() {
    return dialog;
  }
}
