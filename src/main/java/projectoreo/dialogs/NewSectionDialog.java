package projectoreo.dialogs;

import javafx.scene.control.TextInputDialog;
import projectoreo.dialogs.utils.DialogType;
import projectoreo.models.Section;

import java.util.Optional;

public class NewSectionDialog {

  private TextInputDialog dialog;

  public NewSectionDialog(DialogType dialogType, Section section) {
    dialog = new TextInputDialog(section != null ? section.getName() : "");
    dialog.setTitle((dialogType == DialogType.CREATE) ? "Create":"Edit");
    dialog.setContentText("Section Name");
  }

  public Optional<String> showAndWait() {
    return dialog.showAndWait();
  }
}
