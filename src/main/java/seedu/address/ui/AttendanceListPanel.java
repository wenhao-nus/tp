package seedu.address.ui;

import java.util.Comparator;
import java.util.List;
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

        assert person != null : "Person whose attendance details to be shown cannot be null.";
        this.person = person;

        updateAttendancePanel();
    }

    /**
     * Updates the attendance panel with the {@code TutInfos} of the {@code Person}.
     */
    private void updateAttendancePanel() {
        attendanceContainer.getChildren().clear();

        // Display placeholder message if there are no tutorials enrolled for this person
        if (person.getObservableTutInfos().isEmpty()) {
            logger.fine("No attendance records to display for " + person.getName().fullName);

            Label placeholder = new Label(NO_CLASSES_PLACEHOLDER);
            placeholder.getStyleClass().add("no-classes-label");
            attendanceContainer.getChildren().add(placeholder);
            return;
        }

        // Sort tutorials first by course code, then by tutorial code alphabetically
        List<TutInfo> sortedTutInfos = person.getObservableTutInfos().stream()
                .sorted(Comparator.comparing(TutInfo::getCourseCode).thenComparing(TutInfo::getTutorialCode))
                .toList();

        logger.fine("Displaying " + person.getObservableTutInfos().size()
                + " attendance records for " + person.getName().fullName);

        for (TutInfo tutInfo : sortedTutInfos) {
            assert tutInfo != null : "TutInfo cannot be null.";
            attendanceContainer.getChildren().add(new AttendanceCard(tutInfo).getRoot());
        }
    }
}
