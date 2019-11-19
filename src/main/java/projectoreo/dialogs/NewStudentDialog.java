package projectoreo.dialogs;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import projectoreo.managers.DatabaseManager;
import projectoreo.models.Section;
import projectoreo.models.Student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class NewStudentDialog {

  private Dialog dialog;

  public NewStudentDialog(DialogType dialogType, Student student) {
    dialog = new Dialog<>();

    String buttonText = "Create";
    if (dialogType == DialogType.CREATE) {
      dialog.setTitle("Create New Student");
      dialog.setHeaderText("Create New Student");
    } else {
      dialog.setTitle("Edit Student");
      dialog.setHeaderText("Edit Student");
      buttonText = "Update";
    }

    ButtonType controlButton = new ButtonType(buttonText, ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(controlButton, ButtonType.CANCEL);

    GridPane grid = new GridPane();
    grid.setHgap(10d);
    grid.setVgap(10d);
    grid.setPadding(new Insets(20, 10, 10, 10));

    TextField studentId = new TextField();
    TextField firstName = new TextField();
    TextField middleName = new TextField();
    TextField lastName = new TextField();
    ComboBox<Section> assignedSection = new ComboBox<>();

    studentId.setMinWidth(300d);
    // TODO: Put in separate execution service to prevent blocking the UI thread
    try {
      ResultSet res = DatabaseManager.getInstance().getSectionQueries().readSectionAll();
      while (res.next()) {
        assignedSection
            .getItems()
            .add(new Section(res.getInt("section_id"), res.getString("name")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    assignedSection.setValue(assignedSection.getItems().get(0));

    // Initialization if there is a passed subject data
    if (student != null) {
      studentId.setText(student.getId());
      firstName.setText(student.getFirstName());
      middleName.setText(student.getMiddleName());
      lastName.setText(student.getLastName());
      assignedSection.setValue(student.getSectionId());
    }

    grid.add(new Label("Student ID"), 0, 0);
    grid.add(studentId, 1, 0);
    grid.add(new Label("First Name"), 0, 1);
    grid.add(firstName, 1, 1);
    grid.add(new Label("Middle Name"), 0, 2);
    grid.add(middleName, 1, 2);
    grid.add(new Label("Last Name"), 0, 3);
    grid.add(lastName, 1, 3);
    grid.add(new Label("Assign Section"), 0, 4);
    grid.add(assignedSection, 1, 4);

    // Disable button for validation process first
    Node okButton = dialog.getDialogPane().lookupButton(controlButton);
    okButton.setDisable(true);

    // Validation
    studentId
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) ->
                validation(studentId, firstName, lastName, okButton));
    firstName
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) ->
                validation(studentId, firstName, lastName, okButton));
    middleName
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) ->
                validation(studentId, firstName, lastName, okButton));
    lastName
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) ->
                validation(studentId, firstName, lastName, okButton));
    assignedSection
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) ->
                validation(studentId, firstName, lastName, okButton));

    dialog.getDialogPane().setContent(grid);

    // Request focus on the first field
    Platform.runLater(studentId::requestFocus);

    // Storing information gathered
    dialog.setResultConverter(
        dialogButton -> {
          if (dialogButton == controlButton) {
            return new Student(
                studentId.getText(),
                firstName.getText(),
                middleName.getText(),
                lastName.getText(),
                assignedSection.getValue());
          } else return null;
        });
  }

  private void validation(
          TextField studentId,
          TextField firstName,
          TextField lastName,
          Node okButton) {
    okButton.setDisable(
        (studentId.getText().isEmpty()
            || firstName.getText().isEmpty()
            || lastName.getText().isEmpty()));
  }

  public Optional showAndWait() {
    return dialog.showAndWait();
  }
}
