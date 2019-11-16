package projectoreo.utils;

import javafx.scene.control.TextField;

public class TextFieldLimiter {

    /**
     * Code taken from : https://stackoverflow.com/questions/15159988/javafx-2-2-textfield-maxlength
     *      * @param tf
     * @param maxLength
     */

    public static void apply(final TextField tf, final int maxLength) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tf.getText().length() > maxLength) {
                String s = tf.getText().substring(0, maxLength);
                tf.setText(s);
            }
        });
    }
}
