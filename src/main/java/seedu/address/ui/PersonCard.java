package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private ScrollPane contactInfoScrollPane;

    @FXML
    private HBox cardPane;

    @FXML
    private Label name;

    @FXML
    private Label id;

    @FXML
    private VBox fieldsContainer;

    @FXML
    private FlowPane tags;

    @FXML
    private ScrollPane tutInfosScrollPane;

    @FXML
    private VBox tutInfosContainer;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex, ListView<Person> listView) {
        super(FXML);
        this.person = person;

        assert listView != null : "ListView must not be null";

        preventScrollPaneFocusing(listView);
        redirectVerticalScroll(contactInfoScrollPane, listView);

        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);

        fieldsContainer.getChildren().addAll(
                new PersonCardField("Email", person.getEmail().toString()).getRoot(),
                new PersonCardField("Telegram", person.getDisplayTelegram()).getRoot(),
                new PersonCardField("Phone", person.getDisplayPhone()).getRoot()
        );

        person.getTutInfos().stream()
                .sorted(Comparator.comparing(tutInfo -> tutInfo.getCourseCode().toUpperCase()))
                .forEach(tutInfo -> tutInfosContainer
                        .getChildren().add(
                                new TutInfoField(tutInfo.getCourseCode(), tutInfo.getTutorialCode()).getRoot()));

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    /**
     * Maintains keyboard focus on the listview away from tutInfosScrollPane when it is clicked.
     */
    private void preventScrollPaneFocusing(ListView<Person> listView) {

        tutInfosScrollPane.setFocusTraversable(false);
        tutInfosContainer.setFocusTraversable(false);

        tutInfosScrollPane.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            event.consume();
            listView.requestFocus();
        });

        contactInfoScrollPane.setFocusTraversable(false);
        contactInfoScrollPane.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            event.consume();
            listView.requestFocus();
        });
    }

    /**
     * Redirects vertical scrolling of a ScrollPane to a ListView.
     */
    private void redirectVerticalScroll(ScrollPane scrollPane, ListView<Person> listView) {
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                ScrollEvent scrollWheelScrolled = event.copyFor(listView, listView);
                listView.fireEvent(scrollWheelScrolled);
            }
        });
    }

}
