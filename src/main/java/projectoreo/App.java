package projectoreo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import projectoreo.activities.front.FrontController;
import projectoreo.managers.DatabaseManager;
import projectoreo.utils.ActivityLoader;
import projectoreo.utils.ControllersDispatcher;

/** Hello world! */
public class App extends Application {

  private Stage primaryStage;

  public static void main(String[] args) {
    launch(args);
  }

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
    this.primaryStage
        .getIcons()
        .add(new Image("https://image.flaticon.com/icons/png/128/1160/1160519.png"));

    ActivityLoader intent = new ActivityLoader();
    intent.setLocationPath("views/front.fxml");
    intent.setActivityController(new FrontController());
    intent.onFinish(
        finish -> {
          ((FrontController) intent.getActivityController()).setPrimaryStage(this.primaryStage);
          this.primaryStage.setScene(new Scene(intent.getActivity()));
          this.primaryStage.show();
        });
    intent.start();
  }
}
