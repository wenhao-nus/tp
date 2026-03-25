package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * An UI component that displays attendance record of a {@code person}.
 */
public class AttendanceListPanel extends UiPart<Region> {

    private static final String FXML = "AttendanceListPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(AttendanceListPanel.class);

    private final Person person;

    @FXML
    private Label label;

    /**
     * Creates an {@code AttendancePanel} showing the attendance record.
     *
     * @param person The {@code person} whose attendance record will be displayed.
     */
    public AttendanceListPanel(Person person) {
        super(FXML);
        this.person = person;

        label.setText("to be implemented");
    }
}
