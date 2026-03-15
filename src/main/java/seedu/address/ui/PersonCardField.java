package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCardField extends UiPart<Region> {

    private static final String FXML = "PersonListCardField.fxml";

    @FXML
    private Label fieldType;
    @FXML
    private Label value;

    /**
     * Creates a {@code PersonCardField} with the given field type and value to display.
     * Displays {@code "—"} if the value is empty or absent.
     *
     * @param fieldType The label describing the field (e.g. "Phone", "Address").
     * @param value The value to display. Use an empty string or dash if the field is absent.
     */
    public PersonCardField(String fieldType, String value) {
        super(FXML);
        this.fieldType.setText(fieldType + ":");
        if (value.isEmpty() || value.equals("-")) {
            this.value.setText("-");
            this.value.getStyleClass().add("field-missing");
        } else {
            this.value.setText(value);
        }
    }
}
