package projectoreo.activities.data_collection;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import projectoreo.dialogs.NewStudentDialog;
import projectoreo.utils.Controller;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentTabController implements Initializable, Controller {

  @FXML private AnchorPane studentTab;

  @FXML private TextField searchField;
  @FXML private Button search;

  @FXML private TableColumn idColumn;
  @FXML private TableColumn firstNameColumn;
  @FXML private TableColumn middleNameColumn;
  @FXML private TableColumn lastNameColumn;

  @FXML private Button createNewStudent;
  @FXML private Button deleteSelectedStudent;
  @FXML private Button editSelectedStudent;

  @FXML private Button importFromCSV;
  @FXML private Button viewSummaryOfData;
  @FXML private Button exportToCSV;
  @FXML private Button deleteAllData;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setup();
    listeners();
  }

  @Override
  public void setup() {}

  @Override
  public void listeners() {
    search.setOnAction(click -> {});

    createNewStudent.setOnAction(
        click -> {
          NewStudentDialog.show();
        });
    deleteSelectedStudent.setOnAction(
        click -> {
          System.out.println(click.getSource());
        });
    editSelectedStudent.setOnAction(
        click -> {
          System.out.println(click.getSource());
        });
    importFromCSV.setOnAction(
        click -> {
          System.out.println(click.getSource());
        });
    viewSummaryOfData.setOnAction(
        click -> {
          System.out.println(click.getSource());
        });
    exportToCSV.setOnAction(
        click -> {
          System.out.println(click.getSource());
        });
    deleteAllData.setOnAction(
        click -> {
          System.out.println(click.getSource());
        });
  }

  public AnchorPane getStudentTab() {
    return studentTab;
  }

  public void setStudentTab(AnchorPane studentTab) {
    this.studentTab = studentTab;
  }
}
