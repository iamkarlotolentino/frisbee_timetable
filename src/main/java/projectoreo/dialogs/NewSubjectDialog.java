package projectoreo.dialogs;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import projectoreo.models.RoomType;
import projectoreo.models.Subject;

import java.util.Optional;

public class NewSubjectDialog {

  private Dialog<Subject> dialog;

  /**
   * Creates a new subject form dialog
   *
   * <p>Pass a null subject when creating a new subject.
   *
   * @param dialogType
   * @param subject
   */
  public NewSubjectDialog(DialogType dialogType, Subject subject) {
    dialog = new Dialog<>();

    String buttonText = "Create";
    if (dialogType == DialogType.CREATE) {
      dialog.setTitle("Create New Subject");
      dialog.setHeaderText("Create New Subject");
    } else {
      dialog.setTitle("Edit Subject");
      dialog.setHeaderText("Edit Subject");
      buttonText = "Update";
    }

    ButtonType controlButton = new ButtonType(buttonText, ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(controlButton, ButtonType.CANCEL);

    GridPane grid = new GridPane();
    grid.setHgap(10d);
    grid.setVgap(10d);
    grid.setPadding(new Insets(20, 10, 10, 10));

    TextField subjectId = new TextField();
    TextField name = new TextField();
    TextField desc = new TextField();
    ComboBox<String> assignedRoomType = new ComboBox<>();

    subjectId.setMinWidth(300d); // Increasing the size of the dialog
    assignedRoomType.setValue("-select-");
    assignedRoomType.getItems().add("LEC");
    assignedRoomType.getItems().add("CMP-LAB");
    assignedRoomType.getItems().add("BIO-LAB");

    // Initialization if there is a passed subject data
    if (subject != null) {
      subjectId.setText(subject.getId());
      name.setText(subject.getName());
      desc.setText(subject.getDesc());
      assignedRoomType.setValue(subject.getType().getType());
    }

    grid.add(new Label("Subject ID"), 0, 0);
    grid.add(subjectId, 1, 0);
    grid.add(new Label("Full Name"), 0, 1);
    grid.add(name, 1, 1);
    grid.add(new Label("Description"), 0, 2);
    grid.add(desc, 1, 2);
    grid.add(new Label("Subject Type"), 0, 3);
    grid.add(assignedRoomType, 1, 3);

    // Disable button for validation process first
    Node okButton = dialog.getDialogPane().lookupButton(controlButton);
    okButton.setDisable(true);

    subjectId.textProperty().addListener(validation(subjectId, name, assignedRoomType, okButton));
    name.textProperty().addListener(validation(subjectId, name, assignedRoomType, okButton));
    assignedRoomType
        .valueProperty()
        .addListener(validation(subjectId, name, assignedRoomType, okButton));

    dialog.getDialogPane().setContent(grid);

    // Request focus on the first field
    Platform.runLater(() -> subjectId.requestFocus());

    // Storing information gathered
    dialog.setResultConverter(
        dialogButton -> {
          if (dialogButton == controlButton) {
            return new Subject(
                subjectId.getText(), name.getText(), desc.getText(), new RoomType(1, "Text"));
          }
          return null;
        });
  }

  private ChangeListener<String> validation(
      TextField subjectId, TextField name, ComboBox<String> assignedRoomType, Node okButton) {
    return (observable, oldValue, newValue) -> {
      okButton.setDisable(
          (subjectId.getText().isEmpty()
              || name.getText().isEmpty()
              || assignedRoomType.getValue().equals("-select-")));
    };
  }

  public Optional<Subject> showAndWait() {
    return dialog.showAndWait();
  }
}
