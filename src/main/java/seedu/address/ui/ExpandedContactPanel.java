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
    private static final String DEFAULT_MESSAGE = "Select a contact from the list for full details.";

    private final Logger logger = LogsCenter.getLogger(ExpandedContactPanel.class);

    // Container holding the detail sections in Expanded Contact Panel.
    @FXML
    private VBox detailsContainer;

    /**
     * Creates a {@code ExpandedContactPanel}.
     * Initializes the panel to show the default message.
     */
    public ExpandedContactPanel() {
        super(FXML);
        showDefaultDetails();
    }

    /**
     * Updates the panel with the selected {@code person} whose contact details will be shown.
     * Displays default message if the selected contact is null.
     *
     * @param selectedPerson The person who is currently selected in the contact list.
     */
    public void setSelectedPerson(Person selectedPerson) {
        if (selectedPerson == null) {
            showDefaultDetails();
        } else {
            showPersonDetails(selectedPerson);
        }
    }

    /**
     * Displays a default message using {@code PersonDetailsPanel}.
     */
    private void showDefaultDetails() {
        PersonDetailsPanel panel = new PersonDetailsPanel(DEFAULT_MESSAGE);
        updateDetailsContainer(panel);
    }

    /**
     * Displays the full details of {@code person} using {@code PersonDetailsPanel}.
     *
     * @param person The {@code person} to be displayed.
     */
    private void showPersonDetails(Person person) {
        PersonDetailsPanel panel = new PersonDetailsPanel(person);
        updateDetailsContainer(panel);
    }

    /**
     * Clears the container and adds {@code PersonDetailsPanel}.
     *
     * @param panel The {@code PersonDetailsPanel} to display.
     */
    private void updateDetailsContainer(PersonDetailsPanel panel) {
        detailsContainer.getChildren().clear();
        detailsContainer.getChildren().add(panel.getRoot());
    }
}
