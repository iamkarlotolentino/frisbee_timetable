package projectoreo.activities.data_collection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import projectoreo.dialogs.ManageTypeDialog;
import projectoreo.dialogs.utils.DialogAlert;
import projectoreo.dialogs.utils.DialogType;
import projectoreo.dialogs.NewRoomDialog;
import projectoreo.models.Room;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RoomTabController extends DataCollectionTemplate {

  private static final String ROOM_SELECTED_DELETE = "Selected room has been deleted";
  private static final String ROOM_DELETE_ALL = "All data in the room has been wiped";
  private static final String ROOM_NEW = "New room data has been added";
  private static final String ROOM_EDIT = "Selected room has been updated";
  private static final String ROOM_WORK_REFRESH_START =
      "Populating room data to the table. Working...";
  private static final String ROOM_WORK_REFRESH_FINISH =
      "Successfully populated all room data in the table. Done!";
  private static final String ROOM_WORK_REFRESH_FAILED =
      "We have encountered an error while populating room data. Failed!";

  private Task<ObservableList<Room>> loadRoomTask;

  @FXML private TableView<Room> tableView;

  @FXML private TableColumn<Room, String> idColumn;
  @FXML private TableColumn<Room, String> nameColumn;
  @FXML private TableColumn<Room, String> typeColumn;

  @FXML private Button manageTypeB;

  public RoomTabController() {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
  }

  @Override
  public void setup() {
    super.setup();

    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
  }

  @Override
  public void listeners() {
    super.listeners();

    refresh.setOnAction(click -> loadSectionTask());
    createNew.setOnAction(click -> createNewRoom());
    editSelected.setOnAction(click -> editSelectedRoom());
    deleteSelected.setOnAction(click -> deleteSelectedRoom());
    manageTypeB.setOnAction(click -> new ManageTypeDialog());
  }

  private void createNewRoom() {
    NewRoomDialog newRoomDialog = new NewRoomDialog(DialogType.CREATE, null);
    Optional<Room> result = newRoomDialog.showAndWait();
    result.ifPresent(
        roomData -> {
          try {
            DB_MANAGER.getRoomQueries().create(roomData);
            setCurrentStatus(ROOM_NEW, false);
            tableView.getItems().add(roomData);
          } catch (SQLException e) {
            if (e.getErrorCode() == 19) DialogAlert.requestDuplicateError("[ROOM_ID]");
            else e.printStackTrace();
          }
        });
  }

  private void editSelectedRoom() {
    NewRoomDialog editRoomDialog =
        new NewRoomDialog(DialogType.EDIT, tableView.getSelectionModel().getSelectedItem());
    Optional<Room> result = editRoomDialog.showAndWait();
    result.ifPresent(
        roomData -> {
          try {
            // Newly created item cannot be edited
            if (tableView.getSelectionModel().getSelectedItem().getId() != 0) {
              DB_MANAGER.getRoomQueries().updateAllById(roomData);
              setCurrentStatus(ROOM_EDIT, false);
              tableView.getItems().set(tableView.getSelectionModel().getSelectedIndex(), roomData);
            } else DialogAlert.requestSelectedNotUpdatedWarning();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        });
  }

  private void deleteSelectedRoom() {
    Optional<ButtonType> result = DialogAlert.requestDeleteSelectedConfirmation();
    result.ifPresent(
        selectedButton -> {
          if (selectedButton == ButtonType.OK) {
            try {
              // Newly created item cannot be deleted
              if (tableView.getSelectionModel().getSelectedItem().getId() != 0) {
                DB_MANAGER
                    .getRoomQueries()
                    .deleteById(tableView.getSelectionModel().getSelectedItem().getId());
                tableView.getItems().remove(tableView.getSelectionModel().getSelectedIndex());
                setCurrentStatus(ROOM_SELECTED_DELETE, false);
              } else DialogAlert.requestSelectedNotUpdatedWarning();
            } catch (SQLException e) {
              e.printStackTrace();
            }
          }
        });
  }

  private void loadSectionTask() {
    setCurrentStatus(ROOM_WORK_REFRESH_START, true);
    loadRoomTask =
        new Task<ObservableList<Room>>() {
          @Override
          protected ObservableList<Room> call() {
            ObservableList<Room> roomsList = FXCollections.observableArrayList();
            try {
              ResultSet res = DB_MANAGER.getRoomQueries().readAll();
              while (res.next()) {
                roomsList.add(
                    new Room(
                        res.getInt("room_id"),
                        res.getString("name"),
                        DB_MANAGER.getRoomTypeQueries().readById(res.getInt("type_id"))));
              }
              res.close();
            } catch (SQLException e) {
              e.printStackTrace();
            }
            return roomsList;
          }
        };
    loadRoomTask.setOnFailed(
        failed -> {
          setCurrentStatus(ROOM_WORK_REFRESH_FAILED, false);
        });
    loadRoomTask.setOnSucceeded(
        succeeded -> {
          tableView.setItems(loadRoomTask.getValue());
          setCurrentStatus(ROOM_WORK_REFRESH_FINISH, false);
        });

    ExecutorService es = Executors.newFixedThreadPool(1);
    es.execute(loadRoomTask);
    es.shutdown();
  }

  AnchorPane getRoomTab() {
    return contentPane;
  }
}
