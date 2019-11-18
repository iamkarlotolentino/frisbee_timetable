package projectoreo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import projectoreo.activities.front.FrontController;
import projectoreo.managers.DatabaseManager;
import projectoreo.utils.ActivityLoader;
import projectoreo.utils.ControllersDispatcher;

/** Hello world! */
public class App extends Application {

  private Stage primaryStage;

  @Override
  public void init() throws Exception {
    super.init();
    ControllersDispatcher.getInstance();
    DatabaseManager.getInstance();
  }

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.primaryStage.setTitle("Automated Lecture-Course Timetable System");
    this.primaryStage.setMinHeight(650d);
    this.primaryStage.setMinWidth(1000d);

    ActivityLoader intent = new ActivityLoader();
    intent.setLocationPath("views/front.fxml");
    intent.setActivityController(new FrontController());
    intent.onFinish(
        finish -> {
          this.primaryStage.setScene(new Scene(intent.getActivity()));
          this.primaryStage.show();
        });
    intent.start();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
