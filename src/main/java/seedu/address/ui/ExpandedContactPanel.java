package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Panel containing the full details of the selected contact.
 */
public class ExpandedContactPanel extends UiPart<Region> {

    private static final String FXML = "ExpandedContactPanel.fxml";
    private static final String DEFAULT_MESSAGE = "Select or add a contact to view details";

    private final Logger logger = LogsCenter.getLogger(ExpandedContactPanel.class);

    // Container holding the PersonDetailsPanel and AttendanceListPanel.
    @FXML
    private VBox detailsContainer;

    /**
     * Creates a {@code ExpandedContactPanel}.
     * Initializes the panel to show the default message.
     */
    public ExpandedContactPanel() {
        super(FXML);
        getRoot().setPickOnBounds(false);
        getRoot().setMouseTransparent(false);
        showDefaultDetails();
    }

    /**
     * Updates the panel to display the details and attendance records of the {@code selectedPerson}.
     * Displays default message if the {@code selectedPerson} is null.
     * Applies styling to highlight the panel only when a person is selected.
     *
     * @param selectedPerson The person who is currently selected in the contact list.
     */
    public void setSelectedPerson(Person selectedPerson) {
        getRoot().getStyleClass().remove("contact-selected"); 

        if (selectedPerson == null) {
            logger.fine("Displaying default details due to no contact selected");

            showDefaultDetails();
        } else {
            logger.fine("Displaying full details for: " + selectedPerson.getName().fullName);

            showPersonDetails(selectedPerson);
            showAttendanceSection(selectedPerson);

            getRoot().getStyleClass().add("contact-selected");
        }
    }

    /**
     * Displays a default message using {@code PersonDetailsPanel}.
     */
    private void showDefaultDetails() {
        PersonDetailsPanel personDetailsPanel = new PersonDetailsPanel(DEFAULT_MESSAGE);
        updateDetailsContainer(personDetailsPanel);
    }

    /**
     * Displays the full details of {@code person} using {@code PersonDetailsPanel}.
     *
     * @param person The {@code person} to be displayed.
     */
    private void showPersonDetails(Person person) {
        PersonDetailsPanel personDetailsPanel = new PersonDetailsPanel(person);
        updateDetailsContainer(personDetailsPanel);
    }

    /**
    * Adds the {@code AttendancePanel} to the {@code detailsContainer}.
    *
    * @param person The {@code person} whose attendance record will be displayed.
    */
    private void showAttendanceSection(Person person) {
        logger.fine("Adding attendance section for: " + person.getName().fullName);

        AttendanceListPanel attendancePanel = new AttendanceListPanel(person);
        detailsContainer.getChildren().add(attendancePanel.getRoot());
    }

    /**
     * Clears the container and adds {@code PersonDetailsPanel}.
     *
     * @param personDetailsPanel The {@code PersonDetailsPanel} to display.
     */
    private void updateDetailsContainer(PersonDetailsPanel personDetailsPanel) {
        detailsContainer.getChildren().clear();
        detailsContainer.getChildren().add(personDetailsPanel.getRoot());
    }
}
