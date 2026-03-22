package seedu.address.ui;

import static seedu.address.model.person.Telegram.TELEGRAM_PREFIX;
import static seedu.address.ui.PersonDetailsPanel.EMPTY_FIELD_VALUE;

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
     * @param value The value to display.
     */
    public PersonCardField(String fieldType, String value) {
        super(FXML);
        this.fieldType.setText(fieldType + ":");
        this.value.setText(formatValue(fieldType, value));
        if (value.equals("-")) {
            applyMissingFieldStyle();
        }
    }

    private String formatValue(String fieldType, String value) {
        if (value.equals("-")) {
            return EMPTY_FIELD_VALUE;
        }
        if (fieldType.equals("Telegram")) {
            return TELEGRAM_PREFIX + value;
        } else {
            return value;
        }
    }

    private void applyMissingFieldStyle() {
        this.fieldType.getStyleClass().add("field-missing");
        this.value.getStyleClass().add("field-missing");
    }
}
