package projectoreo.dialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import projectoreo.models.Student;
import projectoreo.utils.ActivityLoader;
import projectoreo.utils.ResourceLoader;

import java.io.IOException;

public class NewStudentDialog {

  private static NewStudentDialog instance;

  private Stage dialog;

  private BorderPane rootPane;

  private NewStudentDialog() {
    try {
      rootPane = FXMLLoader.load(ResourceLoader.getResource("views/new_student.fxml"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (rootPane != null) {
      dialog = new Stage();
      dialog.initModality(Modality.APPLICATION_MODAL);
      dialog.setTitle("Create New Student");
      dialog.setScene(new Scene(rootPane));
      dialog.showAndWait();
    }
  }

  public static Student show() {
    if (instance == null) instance = new NewStudentDialog();

    Student student = new Student();

    return student;
  }
}
