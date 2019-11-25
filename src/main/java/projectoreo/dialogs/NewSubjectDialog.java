package projectoreo.dialogs;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import projectoreo.database.DatabaseManager;
import projectoreo.database.columns.RoomTypeColumn;
import projectoreo.dialogs.utils.DialogType;
import projectoreo.models.RoomType;
import projectoreo.models.Subject;

import java.sql.ResultSet;
import java.util.Optional;

public class NewSubjectDialog {

  private Task<Boolean> subjectListTask;

  private Dialog<Subject> dialog;
  private Optional<Subject> result;

  public NewSubjectDialog(DialogType dialogType, Subject subject) {
    dialog = new Dialog<>();

    dialog.setTitle((dialogType == DialogType.CREATE) ? "Create" : "Edit");
    dialog.setHeaderText(
        (dialogType == DialogType.CREATE) ? "Create New Subject" : "Edit Selected Subject");

    ButtonType controlButton =
        new ButtonType(
            (dialogType == DialogType.CREATE) ? "Create New" : "Update",
            ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(controlButton, ButtonType.CANCEL);

    // Setting the layout of the components
    GridPane grid = new GridPane();
    grid.setHgap(10d);
    grid.setVgap(10d);
    grid.setPadding(new Insets(20, 10, 10, 10));

    // Declaring the components
    TextField subjectIdTF = new TextField();
    TextField nameTF = new TextField();
    TextField unitsTF = new TextField();
    TextField requiredHoursTF = new TextField();
    ComboBox<RoomType> roomTypeCB = new ComboBox<>();

    // Setting the properties of the components
    subjectIdTF.setPromptText("ABC-123");
    nameTF.setPromptText("Philoshopy subject");
    unitsTF.setPromptText("0");
    requiredHoursTF.setPromptText("0");
    subjectIdTF.setMinWidth(300d); // Increasing the size of the dialog

    // Initializing the necessary data for the components
    // Executing the query in the background thread
    subjectListTask =
        new Task<Boolean>() {
          @Override
          protected Boolean call() throws Exception {
            ResultSet res = DatabaseManager.getInstance().getRoomTypeQueries().readAll();
            while (res.next()) {
              roomTypeCB
                  .getItems()
                  .add(
                      new RoomType(
                          res.getInt(RoomTypeColumn.TYPE_ID.get()),
                          res.getString(RoomTypeColumn.NAME.get())));
            }
            roomTypeCB.setValue(roomTypeCB.getItems().get(0));
            return Boolean.TRUE;
          }
        };

    // Initialization if there is a passed subject data
    if (subject != null) {
      subjectIdTF.setText(subject.getId());
      nameTF.setText(subject.getName());
      unitsTF.setText(String.valueOf(subject.getUnits()));
      requiredHoursTF.setText(String.valueOf(subject.getRequiredHours()));
      roomTypeCB.setValue(subject.getType());
    }

    // Placing the components in the layout
    grid.add(new Label("Subject ID"), 0, 0);
    grid.add(subjectIdTF, 1, 0);
    grid.add(new Label("Name"), 0, 1);
    grid.add(nameTF, 1, 1);
    grid.add(new Label("Units"), 0, 2);
    grid.add(unitsTF, 1, 2);
    grid.add(new Label("Required Hours"), 0, 3);
    grid.add(requiredHoursTF, 1, 3);
    grid.add(new Label("Type"), 0, 4);
    grid.add(roomTypeCB, 1, 4);

    // Disable button for validation process first
    //    Node okButton = dialog.getDialogPane().lookupButton(controlButton);
    //    okButton.setDisable(true);

    dialog.getDialogPane().setContent(grid);

    // Request focus on the first field
    Platform.runLater(subjectIdTF::requestFocus);

    // Storing information gathered
    dialog.setResultConverter(
        dialogButton -> {
          if (dialogButton == controlButton) {
            return new Subject(
                subjectIdTF.getText(),
                nameTF.getText(),
                Integer.parseInt(requiredHoursTF.getText()),
                Integer.parseInt(unitsTF.getText()),
                roomTypeCB.getValue(),
                0);
          }
          return null;
        });

    subjectListTask.run();
  }

  public Optional<Subject> showAndWait() {
    return dialog.showAndWait();
  }
}
