package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 * An UI component that displays the attendance status for a single week
 * of a {@code TutInfo} belonging to a {@code Person}.
 */
public class AttendanceCardField extends UiPart<Region> {

    private static final String FXML = "AttendanceListCardField.fxml";
    private static final String WEEK_STRING = "Week ";
    private static final String ATTENDED_STRING = "✓";
    private static final String NOT_ATTENDED_STRING = "-";

    @FXML
    private Label week;
    @FXML
    private Label isAttended;

    /**
     * Creates a {@code AttendanceCardField} with the given week and attendance status to display.
     *
     * @param week       The week number for tutorial attendance (between 1 and 13 (inclusive)).
     * @param isAttended The boolean indicating if the tutorial was attended for that week.
     */
    public AttendanceCardField(int week, boolean isAttended) {
        super(FXML);
        this.week.setText(WEEK_STRING + week);
        if (isAttended) {
            this.isAttended.setText(ATTENDED_STRING);
            this.isAttended.getStyleClass().add("attended");
        } else {
            this.isAttended.setText(NOT_ATTENDED_STRING);
            this.isAttended.getStyleClass().add("unattended");
        }
    }
}
