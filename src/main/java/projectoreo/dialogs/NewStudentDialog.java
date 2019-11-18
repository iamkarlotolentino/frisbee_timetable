package projectoreo.dialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import projectoreo.models.Student;
import projectoreo.utils.ResourceLoader;

import java.io.IOException;

public class NewStudentDialog {

  private Stage dialog;
  private BorderPane rootPane;
  private TextField studentId;
  private TextField firstName;
  private TextField middleName;
  private TextField lastName;
  private ComboBox assignedSection;
  private Button create;
  private Button cancel;
  private Student studentData;

  public NewStudentDialog(NewStudentDialog.Type type) {
    try {
      rootPane = FXMLLoader.load(ResourceLoader.getResource("views/new_student.fxml"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (rootPane != null) {
      dialog = new Stage();
      dialog.initModality(Modality.APPLICATION_MODAL);
      dialog.setResizable(false);
      if (type == Type.CREATE) dialog.setTitle("Create New Student");
      else dialog.setTitle("Edit New Student");
      dialog.setScene(new Scene(rootPane));

      initialize();
      listeners();
    }
  }

  private void initialize() {
    studentId = (TextField) rootPane.lookup("#studentID");
    firstName = (TextField) rootPane.lookup("#firstName");
    middleName = (TextField) rootPane.lookup("#middleName");
    lastName = (TextField) rootPane.lookup("#lastName");

    assignedSection = (ComboBox) rootPane.lookup("#assignedSection");
    assignedSection.getItems().add("BSCS-1A");
    assignedSection.getItems().add("BSCS-1B");
    assignedSection.getItems().add("BSCS-2A");
    assignedSection.getItems().add("BSCS-2B");

    create = (Button) rootPane.getBottom().lookup("#create");
    cancel = (Button) rootPane.lookup("#cancel");
  }

  private void listeners() {
    create.setOnAction(
        click -> {
          if (!isFieldsEmpty()) {
            if (middleName.getText().isEmpty()) middleName.setText("unspecified");

            studentData =
                new Student(
                    studentId.getText(),
                    firstName.getText(),
                    middleName.getText(),
                    lastName.getText(),
                    -1);
            dialog.close();
          } else {
            Alert warning = new Alert(Alert.AlertType.WARNING);
            warning.setContentText("It is required to input all necessary data for all fields.");
            warning.setHeaderText("Missing information");
            warning.setTitle("Oops..");
            warning.showAndWait();
          }
        });
    cancel.setOnAction(
        click -> {
          dialog.close();
        });
  }

  public void populateData() {
    if (studentData != null) {
      studentId.setText(studentData.getId());
      firstName.setText(studentData.getFirstName());
      middleName.setText(studentData.getMiddleName());
      lastName.setText(studentData.getLastName());
      assignedSection.setValue(studentData.getSectionId());
    }
  }

  public void show() {
    dialog.showAndWait();
  }

  public Student getStudentData() {
    return studentData;
  }

  public void setStudentData(Student studentData) {
    this.studentData = studentData;
  }

  private boolean isFieldsEmpty() {
    return (studentId.getText().isEmpty()
            || firstName.getText().isEmpty()
            || lastName.getText().isEmpty())
        || assignedSection.getValue() == null;
  }

  public enum Type {
    CREATE,
    EDIT,
  }
}
