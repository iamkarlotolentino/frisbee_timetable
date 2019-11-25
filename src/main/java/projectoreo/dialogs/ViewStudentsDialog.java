package projectoreo.dialogs;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import projectoreo.database.DatabaseManager;
import projectoreo.models.Section;
import projectoreo.models.Student;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewStudentsDialog {

  private Dialog dialog;

  public ViewStudentsDialog(Section section) {
    if (section == null) return;

    dialog = new Dialog();
    dialog.setTitle("Student List");
    dialog.setHeaderText(section.getName());

    ButtonType controlBT = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().add(controlBT);

    GridPane grid = new GridPane();
    grid.setHgap(10d);
    grid.setVgap(10d);
    grid.setPadding(new Insets(20, 10, 10, 10));

    TextField searchTF = new TextField();
    ListView<Student> studentLV = new ListView<>();

    searchTF.setPromptText("Filter student list");
    studentLV.setOrientation(Orientation.VERTICAL);
    try {
      ResultSet res =
          DatabaseManager.getInstance().getStudentQueries().readBySection(section.getId());
      while (res.next()) {
        studentLV
            .getItems()
            .add(
                new Student(
                    res.getString("student_id"),
                    res.getString("first_name"),
                    res.getString("middle_name"),
                    res.getString("last_name"),
                    section));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    grid.add(searchTF, 0, 0);
    grid.add(studentLV, 0, 1);

    dialog.getDialogPane().setContent(grid);

    // Request focus on the search field
    Platform.runLater(searchTF::requestFocus);

    dialog.showAndWait();
  }
}
