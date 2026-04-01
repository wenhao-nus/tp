package seedu.address.ui;

import java.util.function.Consumer;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    // True if the current selection was triggered manually via mouse click or keyboard.
    // False if the selection was triggered via a user command.
    private boolean isManualSelection = false;

    @FXML
    private ListView<Person> personListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Person> personList, Consumer<Person> onPersonSelected) {
        super(FXML);
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new PersonListViewCell());

        logger.fine("Initializing PersonListPanel with " + personList.size() + " contacts in the list.");

        personListView.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            isManualSelection = true;
        });

        personListView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            isManualSelection = true;
        });

        personListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)
                -> onPersonSelected.accept(newValue));
    }

    /**
     * Scrolls the {@code PersonListPanel} to show and select the given {@code Person}.
     * This method should only be triggered by user-command and not by manual interaction (mouse or keyboard).
     *
     * @param person the {@code Person} to scroll to and select.
     */
    public void scrollToPerson(Person person) {
        assert person != null : "The person being scrolled to in PersonListPanel must not be null";
        assert !isManualSelection : "scrollToPerson should not be called by a mouse click";

        int index = personListView.getItems().indexOf(person);

        if (index >= 0) {
            personListView.scrollTo(index);
            personListView.getSelectionModel().select(index);
        }
    }

    /**
     * Clears any currently selected person in the person list so no person is highlighted.
     */
    public void clearSelection() {
        personListView.getSelectionModel().clearSelection();
    }

    /**
     * Returns true if the selection was triggered manually by a mouse click or keyboard press.
     */
    public boolean isManualSelection() {
        return isManualSelection;
    }

    /**
     * Marks the current selection as not triggered manually (i.e. triggered via commands).
     */
    public void resetManualSelection() {
        isManualSelection = false;
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                PersonCard card = new PersonCard(person, getIndex() + 1);
                setGraphic(card.getRoot());
            }
        }
    }

}
