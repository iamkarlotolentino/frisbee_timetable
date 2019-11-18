package projectoreo.activities.data_collection;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import projectoreo.utils.ActivityLoader;
import projectoreo.utils.Controller;
import projectoreo.utils.ControllersDispatcher;

public class DataCollectionController implements Controller {

  private TabPane contentPane;

  private StudentTabController studentTabController;
  private SubjectTabController subjectTabController;

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
          ControllersDispatcher.getInstance()
                  .store(StudentTabController.class, studentTabController);

          Tab studentTab = new Tab("  Student  ");
          clipAllSides(studentTabController.getStudentTab());
          studentTab.setContent(new AnchorPane(studentTabController.getStudentTab()));
          contentPane.getTabs().add(studentTab);
        });
    studentTabIntent.start();

    ActivityLoader subjectTabIntent = new ActivityLoader();
    subjectTabIntent.setLocationPath("views/subject_tab.fxml");
    subjectTabIntent.setActivityController(new SubjectTabController());
    subjectTabIntent.onFinish(
        finish -> {
          subjectTabController = (SubjectTabController) subjectTabIntent.getActivityController();
          ControllersDispatcher.getInstance()
              .store(SubjectTabController.class, subjectTabController);

          Tab subjectTab = new Tab("  Subject  ");
          clipAllSides(subjectTabController.getSubjectTab());
          subjectTab.setContent(new AnchorPane(subjectTabController.getSubjectTab()));
          contentPane.getTabs().add(subjectTab);
        });
    subjectTabIntent.start();
  }

  @Override
  public void listeners() {
  }

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
