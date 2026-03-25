package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class AttendanceCard extends UiPart<Region> {

    private static final String FXML = "AttendanceListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private ListView<Person> personListView;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public AttendanceCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);

        fieldsContainer.getChildren().addAll(
                new PersonCardField("Email", person.getEmail().value).getRoot(),
                new PersonCardField("Telegram", person.getTelegram().value).getRoot(),
                new PersonCardField("Phone", person.getPhone().value).getRoot()
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
}
