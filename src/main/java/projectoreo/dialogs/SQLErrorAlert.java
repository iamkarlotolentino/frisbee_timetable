package projectoreo.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class SQLErrorAlert {

  public static void requestDuplicateError(String fields) {
    Alert duplicate = new Alert(Alert.AlertType.ERROR);
    duplicate.setTitle("Duplicate data detected");
    duplicate.setHeaderText("We have found a similar data.");
    duplicate.setContentText(
        "As have been enforced by constraints, it is not allowed to"
            + " have identical data for \n-->"
            + fields
            + "\nPlease try again.");
    duplicate.showAndWait();
  }

  public static Optional<ButtonType> requestConfirmationWarning() {
    Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
    deleteAlert.setTitle("THIS IS AN UNRECOVERABLE ACTION");
    deleteAlert.setHeaderText("Warning! Please think of your actions wisely!");
    deleteAlert.setContentText(
        "You are about to delete all the data about STUDENTS. "
            + "By committing this, there won't be any backup to recover this action.");
    return deleteAlert.showAndWait();
  }
}
