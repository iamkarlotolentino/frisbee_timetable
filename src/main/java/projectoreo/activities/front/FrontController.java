package projectoreo.activities.front;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import projectoreo.activities.data_collection.DataCollectionController;
import projectoreo.utils.Controller;
import projectoreo.utils.ControllersDispatcher;
import sun.plugin.javascript.navig.Anchor;

import java.net.URL;
import java.util.ResourceBundle;

public class FrontController implements Initializable, Controller {

  @FXML private BorderPane rootPane;
  @FXML private AnchorPane welcomeInfoPane;

  @FXML private ToggleButton dataCollection;
  @FXML private ToggleButton scheduleTuning;
  @FXML private ToggleButton generateSchedule;

  @FXML private Button about;

  private DataCollectionController dataCollectionController;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    ControllersDispatcher.getInstance().store(FrontController.class, this);
    setup();
  }

  @Override
  public void setup() {
    listeners();
  }

  @Override
  public void listeners() {
    dataCollection.setOnAction(
        click -> {
          System.out.println("I'm being clicked!");

          // EXPLAINED: The click event sets the button in selected state, before we could evaluate the state
          if (dataCollection.isSelected()) {

            // Prevent multiple new objects
            if (dataCollectionController == null)
              dataCollectionController = new DataCollectionController();
            // Store the controller in the dispatcher
            ControllersDispatcher.getInstance()
                .store(DataCollectionController.class, dataCollectionController);
            setContentPane(dataCollectionController.getContentPane());
          } else {
            setContentPane(welcomeInfoPane);
          }
        });
    scheduleTuning.setOnAction(
        click -> {
          System.out.println("Hi, there!");
        });
    generateSchedule.setOnAction(
        click -> {
          System.out.println("Ohh, hi!");
        });
    about.setOnAction(FrontController::about);
  }

  /** Changes the center section of the BorderPane (rootPane) */
  public void setContentPane(Node contentPane) {
    rootPane.setCenter(contentPane);
  }

  private static void about(ActionEvent click) {
    Alert about = new Alert(Alert.AlertType.INFORMATION);
    about.setTitle("About this software");
    about.setHeaderText("About this software");
    about.setContentText("It is a prerequisite for passing my database systems, respectively.");
    about.showAndWait();
  }
}
