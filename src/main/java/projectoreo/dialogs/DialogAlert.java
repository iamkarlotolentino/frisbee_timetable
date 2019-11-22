package projectoreo.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class DialogAlert {

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

  public static Optional<ButtonType> requestDeleteSelectedConfirmation() {
    Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
    deleteAlert.setTitle("Warning!");
    deleteAlert.setHeaderText("Delete selected item");
    deleteAlert.setContentText(
        "You are about to delete a particular item. "
            + "By committing this, there won't be any backup to recover this action.");
    return deleteAlert.showAndWait();
  }

  public static Optional<ButtonType> requestDeleteAllConfirmation() {
    Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
    deleteAlert.setTitle("Critical Warning!");
    deleteAlert.setHeaderText("Warning! Please think of your actions wisely!");
    deleteAlert.setContentText(
        "You are about to delete all the data. "
            + "By committing this, there won't be any backup to recover this action.");
    return deleteAlert.showAndWait();
  }

  public static void requestSelectedNotUpdatedWarning() {
    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
    infoAlert.setTitle("Information");
    infoAlert.setHeaderText("Newly created item cannot be deleted/edited.");
    infoAlert.setContentText(
            "Before you can do that, please force-refresh first to update to the latest data.");
    infoAlert.showAndWait();
  }
}
