package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

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
     */
    public PersonCardField(String fieldType, String value) {
        super(FXML);
        this.fieldType.setText(fieldType);
        this.value.setText(value);
        }
}
