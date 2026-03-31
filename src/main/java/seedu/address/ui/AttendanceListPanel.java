package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
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
    private ListView<TutInfo> tutInfoListView;

    /**
     * Creates an {@code AttendancePanel} showing the attendance record.
     *
     * @param person The {@code person} whose attendance record will be displayed.
     */
    public AttendanceListPanel(Person person) {
        super(FXML);
        this.person = person;
        tutInfoListView.setPlaceholder(new Label(NO_CLASSES_PLACEHOLDER));
        tutInfoListView.getItems().setAll(person.getObservableTutInfos());
        tutInfoListView.setCellFactory(listView -> new TutInfoListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code TutInfo} using a {@code AttendanceCard}.
     */
    class TutInfoListViewCell extends ListCell<TutInfo> {
        @Override
        protected void updateItem(TutInfo tutInfo, boolean empty) {
            super.updateItem(tutInfo, empty);
            if (empty || tutInfo == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new AttendanceCard(tutInfo).getRoot());
            }
        }
    }
}
