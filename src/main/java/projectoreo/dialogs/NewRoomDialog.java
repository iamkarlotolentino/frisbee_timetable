package projectoreo.dialogs;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import projectoreo.database.DatabaseManager;
import projectoreo.dialogs.utils.DialogType;
import projectoreo.models.Room;
import projectoreo.models.RoomType;

import java.sql.ResultSet;
import java.util.Optional;

public class NewRoomDialog {

  private Task<ObservableList<RoomType>> roomTypeListTask;

  private Dialog dialog;

  public NewRoomDialog(DialogType type, Room room) {
    dialog = new Dialog();
    dialog.setTitle("Room");
    dialog.setHeaderText((type == DialogType.CREATE ? "Create New" : "Edit Selected") + " Room");

    ButtonType doneB =
        new ButtonType(
            (type == DialogType.CREATE) ? "Create New" : "Update", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(doneB, ButtonType.CANCEL);

    // Disable doneB to validate the data first
    Node _doneB = dialog.getDialogPane().lookupButton(doneB);
    _doneB.setDisable(true);

    // Setting the layout of the components
    GridPane grid = new GridPane();
    grid.setHgap(10d);
    grid.setVgap(10d);
    grid.setPadding(new Insets(20, 10, 10, 10));

    // Declaring the input fields
    TextField nameTF = new TextField();
    ComboBox<RoomType> roomTypeCB = new ComboBox<>();

    // Initialize the data to the fields when the dialog is edit
    if (room != null) {
      nameTF.setText(room.getName());
      roomTypeCB.setValue(room.getType());
    }

    // Impose a validation criteria
    nameTF
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> _doneB.setDisable((nameTF.getText().isEmpty())));
    roomTypeCB
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> _doneB.setDisable((nameTF.getText().isEmpty())));

    // Querying to the database while in the background thread
    roomTypeListTask =
        new Task<ObservableList<RoomType>>() {
          @Override
          protected ObservableList<RoomType> call() throws Exception {
            ObservableList<RoomType> rooms = FXCollections.observableArrayList();
            ResultSet res = DatabaseManager.getInstance().getRoomTypeQueries().readAll();
            while (res.next()) {
              rooms.add(new RoomType(res.getInt(1), res.getString(2)));
            }
            return rooms;
          }
        };
    roomTypeListTask.setOnSucceeded(
        succeeded -> {
          // Show the result to the view
          roomTypeCB.setItems(roomTypeListTask.getValue());
          roomTypeCB.setValue(roomTypeCB.getItems().get(0));
        });
    roomTypeListTask.run();

    // Adding the components to the layout
    grid.add(new Label("Room Name"), 0, 0);
    grid.add(nameTF, 1, 0);
    grid.add(new Label("Room Type"), 0, 1);
    grid.add(roomTypeCB, 1, 1);
    dialog.getDialogPane().setContent(grid);

    // Request focus on the first field
    Platform.runLater(nameTF::requestFocus);

    // Storing information gathered
    dialog.setResultConverter(
        dialogB -> {
          int roomId = room != null ? room.getId() : 0;
          if (dialogB == doneB) return new Room(roomId, nameTF.getText(), roomTypeCB.getValue());
          return null;
        });
  }

  public Optional showAndWait() {
    return dialog.showAndWait();
  }
}
