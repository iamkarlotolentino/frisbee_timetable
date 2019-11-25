package projectoreo.dialogs;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import projectoreo.database.DatabaseManager;
import projectoreo.dialogs.utils.DialogAlert;
import projectoreo.models.RoomType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ManageTypeDialog {

  private Dialog dialog;

  public ManageTypeDialog() {
    dialog = new Dialog();
    dialog.setTitle("View");
    dialog.setHeaderText("Manage types");

    ButtonType doneB = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(doneB, ButtonType.CANCEL);

    // Setting the layout of the components
    GridPane grid = new GridPane();
    grid.setHgap(10d);
    grid.setVgap(10d);
    grid.setPadding(new Insets(10, 10, 10, 10));

    // Declaring the input fields
    Button createB = new Button("Create");
    Button editB = new Button("Edit selected");
    Button deleteB = new Button("Delete selected");
    TextField searchTF = new TextField();
    ListView<RoomType> typeLV = new ListView<>();

    // Initializing data
    searchTF.setPrefColumnCount(15);
    searchTF.setPromptText("Filter types");
    createB.setOnAction(click -> createType(typeLV));
    editB.setOnAction(click -> editType(typeLV));
    deleteB.setOnAction(click -> deleteType(typeLV));
    try {
      DatabaseManager DB_MANAGER = DatabaseManager.getInstance();
      ResultSet res = DB_MANAGER.getRoomTypeQueries().readAll();
      while (res.next()) {
        typeLV.getItems().add(new RoomType(res.getInt("type_id"), res.getString("name")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    // Adding components to the layout
    grid.add(createB, 0, 0, 1, 1);
    grid.add(editB, 1, 0, 1, 1);
    grid.add(deleteB, 2, 0, 1, 1);
    grid.add(searchTF, 4, 0, 1, 1);
    grid.add(typeLV, 0, 1, 5, 1);
    dialog.getDialogPane().setContent(grid);

    // Request focus on the create button
    Platform.runLater(createB::requestFocus);

    dialog.showAndWait();
  }

  private void deleteType(ListView<RoomType> typeLV) {
    if (typeLV.getSelectionModel().getSelectedIndex() > -1) {
      final int selectedIndex = typeLV.getSelectionModel().getSelectedIndex();
      final RoomType selectedItem = typeLV.getSelectionModel().getSelectedItem();

      Optional<ButtonType> result = DialogAlert.requestDeleteSelectedConfirmation();
      result.ifPresent(
          selectedButton -> {
            if (selectedButton == ButtonType.OK) {
              try {
                DatabaseManager.getInstance().getRoomTypeQueries().deleteById(selectedItem.getId());
                typeLV.getItems().remove(selectedIndex);
              } catch (SQLException e) {
                e.printStackTrace();
              }
            }
          });
    }
  }

  private void editType(ListView<RoomType> typeLV) {
    if (typeLV.getSelectionModel().getSelectedIndex() > -1) {
      final int selectedIndex = typeLV.getSelectionModel().getSelectedIndex();
      final RoomType selectedItem = typeLV.getSelectionModel().getSelectedItem();

      TextInputDialog editNewDialog = new TextInputDialog();
      editNewDialog.setTitle("Edit selected type");
      editNewDialog.setContentText("New type name");
      Optional<String> result = editNewDialog.showAndWait();
      result.ifPresent(
          newTypeName -> {
            try {
              selectedItem.setType(newTypeName);
              DatabaseManager.getInstance().getRoomTypeQueries().updateById(selectedItem);
              typeLV.getItems().set(selectedIndex, selectedItem);
            } catch (SQLException e) {
              e.printStackTrace();
            }
          });
    }
  }

  private void createType(ListView<RoomType> typeLV) {
    TextInputDialog createNewDialog = new TextInputDialog();
    createNewDialog.setTitle("Create new type");
    createNewDialog.setContentText("Type name");
    Optional<String> result = createNewDialog.showAndWait();
    result.ifPresent(
        typeName -> {
          try {
            RoomType newType = new RoomType(0, typeName);
            DatabaseManager.getInstance().getRoomTypeQueries().create(newType);
            typeLV.getItems().add(newType);
          } catch (SQLException e) {
            e.printStackTrace();
          }
        });
  }
}
