package projectoreo.utils;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ActivityLoader {

  private static final Logger LOGGER = LoggerFactory.getLogger(ActivityLoader.class);

  private String resourcePath;
  private FXMLLoader activityViewLoader;
  private Task<Parent> taskLoader;

  public ActivityLoader() {}

  public ActivityLoader(String resourcePath) {
    this.resourcePath = resourcePath;
  }

  public void setLocationPath(String resourcePath) {
    this.resourcePath = resourcePath;
    initialize();
  }

  public void initialize() {
    activityViewLoader = new FXMLLoader(ResourceLoader.getResource(resourcePath));

    taskLoader =
        new Task<Parent>() {
          @Override
          protected Parent call() {
            try {
              return activityViewLoader.load();
            } catch (IOException e) {
              e.printStackTrace();
            }
            return null;
          }
        };
  }

  public Parent getActivity() {
    return taskLoader.getValue();
  }

  public void start() {
    new Thread(taskLoader).start();
  }

  public void onRunning(EventHandler<WorkerStateEvent> value) {
    taskLoader.setOnRunning(value);
  }

  public void onFailed(EventHandler<WorkerStateEvent> value) {
    taskLoader.setOnFailed(value);
  }

  public void onFinish(EventHandler<WorkerStateEvent> value) {
    taskLoader.setOnSucceeded(value);
  }

  public Controller getActivityController() {
    return activityViewLoader.getController();
  }

  public void setActivityController(Object controller) {
    activityViewLoader.setController(controller);
  }
}
