package projectoreo.activities.data_collection;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import projectoreo.utils.ActivityLoader;
import projectoreo.utils.Controller;

public class DataCollectionController implements Controller {

  private TabPane contentPane;

  private Tab studentTab;
  private StudentTabController studentTabController;

  public DataCollectionController() {
    contentPane = new TabPane();
    contentPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    setup();
  }

  @Override
  public void setup() {
    ActivityLoader studentTabIntent = new ActivityLoader();
    studentTabIntent.setLocationPath("views/student_tab.fxml");
    studentTabIntent.setActivityController(new StudentTabController());
    studentTabIntent.onFinish(
        finish -> {
          studentTabController = (StudentTabController) studentTabIntent.getActivityController();

          Tab studentTab = new Tab("  Student  ");
          clipAllSides(studentTabController.getStudentTab());
          studentTab.setContent(new AnchorPane(studentTabController.getStudentTab()));
          contentPane.getTabs().add(studentTab);

          // TODO: Next tabs are placed in here
        });
    studentTabIntent.start();
  }

  @Override
  public void listeners() {}

  private void clipAllSides(AnchorPane pane) {
    AnchorPane.setTopAnchor(pane, 0d);
    AnchorPane.setRightAnchor(pane, 0d);
    AnchorPane.setBottomAnchor(pane, 0d);
    AnchorPane.setLeftAnchor(pane, 0d);
  }

  public TabPane getContentPane() {
    return contentPane;
  }
}
