package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;
import seedu.address.model.person.TutInfo;

/**
 * An UI component that displays attendance record of a {@code TutInfo} belonging to a {@code person}.
 */
public class AttendanceListPanel extends UiPart<Region> {

    private static final String FXML = "AttendanceListPanel.fxml";
    private static final String NO_CLASSES_PLACEHOLDER = "No classes added yet";

    private final Logger logger = LogsCenter.getLogger(AttendanceListPanel.class);

    private final Person person;

    @FXML
    private VBox attendanceContainer;

    /**
     * Creates an {@code AttendancePanel} showing the attendance record.
     *
     * @param person The {@code person} whose attendance record will be displayed.
     */
    public AttendanceListPanel(Person person) {
        super(FXML);
        this.person = person;
        populateAttendance();
    }

    private void populateAttendance() {
        attendanceContainer.getChildren().clear();

        if (person.getObservableTutInfos().isEmpty()) {
            logger.fine("No attendance records to display for " + person.getName().fullName);

            attendanceContainer.getChildren().add(new Label(NO_CLASSES_PLACEHOLDER));
            return;
        }

        logger.fine("Displaying " + person.getObservableTutInfos().size()
                + " attendance records for " + person.getName().fullName);

        for (TutInfo tutInfo : person.getObservableTutInfos()) {
            attendanceContainer.getChildren().add(new AttendanceCard(tutInfo).getRoot());
        }
    }
}
