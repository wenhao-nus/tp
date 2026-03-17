package seedu.address.ui;

import java.util.Comparator;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

    private static final String[] FIELD_NAMES = { "Name", "Email", "Telegram", "Phone", "Address" };
    private static final String EMPTY_FIELD_VALUE = "---";

    private final Logger logger = LogsCenter.getLogger(PersonDetailsPanel.class);

    @FXML
    private HBox fieldsContainer; // Contains fieldNamesColumn and fieldValuesColumn

    @FXML
    private VBox fieldNamesColumn;

    @FXML
    private VBox fieldValuesColumn;

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
        String[] fieldValues = { message, "", "", "", "" };

        displayFields(FIELD_NAMES, fieldValues);
        tags.getChildren().clear();
    }

    /**
     * Displays the full details of a {@code person} in the panel.
     *
     * @param person The {@code person} whose details are displayed.
     */
    private void displayPersonDetails(Person person) {
        String[] fieldValues = { person.getName().fullName, person.getEmail().value, person.getTelegram().value,
                person.getPhone().value, person.getAddress().value };

        displayFields(FIELD_NAMES, fieldValues);
        displayTags(person);
    }

    /**
    * Displays the tags of a {@code person} in the panel.
    *
    * @param person The {@code person} whose tags are displayed.
    */
    private void displayTags(Person person) {
        tags.getChildren().clear();

        person.getTags().stream().sorted(Comparator.comparing(tag -> tag.tagName))
                                        .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    /**
     * Displays field names and their corresponding field values.
     *
     * @param fieldNames  Array of field names.
     * @param fieldValues Array of field values.
     */
    private void displayFields(String[] fieldNames, String[] fieldValues) {
        fieldNamesColumn.getChildren().clear();
        fieldValuesColumn.getChildren().clear();

        for (int i = 0; i < fieldNames.length; i++) {
            Label nameLabel = new Label(fieldNames[i] + ":");
            Label valueLabel = new Label(formatValue(fieldValues[i]));

            fieldNamesColumn.getChildren().add(nameLabel);
            fieldValuesColumn.getChildren().add(valueLabel);
        }
    }

    /**
     * Formats a field value for display.
     * Returns "---" if the value is null, empty, or a dash ("-") representing an optional field that was not filled.
     * Returns the actual value otherwise.
     *
     * @param value The value of the field.
     * @return The formatted field value for display.
     */
    private String formatValue(String value) {
        if (value == null || value.isEmpty() || value.equals("-")) {
            return EMPTY_FIELD_VALUE;
        }

        return value;
    }
}
