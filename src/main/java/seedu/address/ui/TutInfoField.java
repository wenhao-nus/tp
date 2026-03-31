package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class TutInfoField extends UiPart<Region> {

    private static final String FXML = "TutInfoField.fxml";

    @FXML
    private Label courseCode;
    @FXML
    private Label tutorialCode;

    /**
     * Creates a {@code TutInfoField} with the given course name and tut name to display.
     *
     * @param courseCode The label describing the field (e.g. "Phone", "Address").
     * @param tutorialCode The value to display.
     */
    public TutInfoField(String courseCode, String tutorialCode) {
        super(FXML);
        this.courseCode.setText(courseCode);
        this.tutorialCode.setText(tutorialCode);
    }
}
