package seedu.address.ui;

import java.util.Comparator;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * An UI component that displays full details of a {@code person}.
 */
public class PersonDetailsPanel extends UiPart<Region> {

    private static final String FXML = "PersonDetailsPanel.fxml";

    private static final String[] FIELD_NAMES = { "Email", "Telegram", "Phone", "Address" };
    private static final String EMPTY_FIELD_VALUE = "---";

    private final Logger logger = LogsCenter.getLogger(PersonDetailsPanel.class);

    @FXML
    private Label name;

    @FXML
    private HBox fieldsContainer; // Contains fieldNamesColumn and fieldValuesColumn

    @FXML
    private VBox fieldNamesColumn;

    @FXML
    private VBox fieldValuesColumn;

    @FXML
    private ScrollPane fieldValuesScrollPane;

    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PersonDetailsPanel} showing the default details.
     *
     * @param defaultMessage The message to display when no contact is selected.
     */
    public PersonDetailsPanel(String defaultMessage) {
        super(FXML);
        displayDefaultDetails(defaultMessage);
    }

    /**
     * Creates a {@code PersonDetailsPanel} showing the details of {@code person}.
     *
     * @param person The {@code person} whose details will be displayed.
     */
    public PersonDetailsPanel(Person person) {
        super(FXML);
        displayPersonDetails(person);
    }

    /**
     * Displays the default details in the panel when no contact is selected.
     * Shows the default message in the Name field, while other fields are empty.
     *
     * @param message The default message to display.
     */
    private void displayDefaultDetails(String message) {
        logger.fine("Showing default details: " + message);

        name.setText(message);

        String[] fieldValues = { "", "", "", "" };
        displayFields(fieldValues);

        tags.getChildren().clear();
    }

    /**
     * Displays the full details of a {@code person} in the panel.
     *
     * @param person The {@code person} whose details are displayed.
     */
    private void displayPersonDetails(Person person) {

        name.setText(formatValue(person.getName().fullName));

        String[] fieldValues = {
                person.getEmail().value,
                person.getTelegram().value,
                person.getPhone().value,
                person.getAddress().value
        };

        displayFields(fieldValues);
        displayTags(person);
    }

    /**
    * Displays the tags of a {@code person} in the panel.
    *
    * @param person The {@code person} whose tags are displayed.
    */
    private void displayTags(Person person) {
        tags.getChildren().clear();

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    /**
     * Displays all the field names and their corresponding field values.
     *
     * @param fieldValues Array of field values with same order as FIELD_NAMES.
     */
    private void displayFields(String[] fieldValues) {
        assert FIELD_NAMES.length == fieldValues.length
                : "Length of field names and values arrays must be equal";

        fieldNamesColumn.getChildren().clear();
        fieldValuesColumn.getChildren().clear();

        for (int i = 0; i < fieldValues.length; i++) {
            addFieldToColumns(FIELD_NAMES[i], fieldValues[i]);
        }
    }

    /**
    * Creates and adds two labels for a single field to the VBox columns.
    * The labels each represent the field name and its value, e.g. "Telegram:" and "@alice123".
    *
    * @param fieldName The name of the field.
    * @param fieldValue The value of the field.
    */
    private void addFieldToColumns(String fieldName, String fieldValue) {
        String formattedValue = formatValue(fieldValue);

        Label nameLabel = createFieldLabel(fieldName + ":", fieldValue);
        Label valueLabel = createFieldLabel(formattedValue, fieldValue);

        fieldNamesColumn.getChildren().add(nameLabel);
        fieldValuesColumn.getChildren().add(valueLabel);
    }

    /**
     * Creates a field label and applies styling for missing field.
     *
     * @param text The text to display in the label.
     * @param originalValue The original field value used to check if the field is missing.
     * @return A {@code Label} with the text with styling applied for missing field.
     */
    private Label createFieldLabel(String text, String originalValue) {
        Label label = new Label(text);

        if (isMissingValue(originalValue)) {
            label.getStyleClass().add("missing-field");
        }

        return label;
    }

    /**
     * Formats a field value for display.
     * Returns "---" if the value is empty, or a dash ("-") representing an unfilled optional field.
     * Returns the value unchanged otherwise.
     *
     * @param value The value of the field.
     * @return The formatted field value for display.
     */
    private String formatValue(String value) {
        assert value != null : "Field values must not be null";

        if (isMissingValue(value)) {
            return EMPTY_FIELD_VALUE;
        }

        return value;
    }

    /**
    * Checks whether a field value is missing.
    *
    * @param value The field value to check.
    * @return true if the value is missing, false otherwise.
    */
    private boolean isMissingValue(String value) {
        return (value.isEmpty() || value.equals("-"));
    }
}
