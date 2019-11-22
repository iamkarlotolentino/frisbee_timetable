package projectoreo.dialogs;

import javafx.scene.control.TextInputDialog;
import projectoreo.models.Section;

import java.util.Optional;

public class NewSectionDialog {

  private TextInputDialog dialog;

  public NewSectionDialog(DialogType dialogType, Section section) {
    dialog = new TextInputDialog(section != null ? section.getName() : "");

    if (dialogType == DialogType.CREATE) {
      dialog.setTitle("Create");
      dialog.setHeaderText("Creating new section");
    } else {
      dialog.setTitle("Edit");
      dialog.setHeaderText("Editing section");
    }

    dialog.setContentText("Section Name");
  }

  public Optional<String> showAndWait() {
    return dialog.showAndWait();
  }
}
