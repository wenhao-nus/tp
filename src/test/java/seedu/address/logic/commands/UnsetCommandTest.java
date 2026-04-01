package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for UnsetCommand.
 */
public class UnsetCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_unsetPhone_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person unsetPerson = new PersonBuilder(firstPerson).withoutPhone().build();

        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        descriptor.setPhone(Optional.empty());
        UnsetCommand unsetCommand = new UnsetCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = "Unset phone (previously: 94351253) for Alice Pauline (person at index 1)";

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(firstPerson, unsetPerson);
        expectedModel.setPersonToShow(unsetPerson);

        assertCommandSuccess(unsetCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_unsetAllFields_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person unsetPerson = new PersonBuilder(firstPerson)
                .withoutPhone()
                .withoutEmail()
                .withoutAddress()
                .withoutTelegram()
                .withTags()
                .build();

        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        descriptor.setPhone(Optional.empty());
        descriptor.setEmail(Optional.empty());
        descriptor.setAddress(Optional.empty());
        descriptor.setTelegram(Optional.empty());
        descriptor.setTags(java.util.Collections.emptySet());
        UnsetCommand unsetCommand = new UnsetCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = "Unset phone (previously: 94351253), email (previously: alice@example.com), "
                + "address (previously: 123, Jurong West Ave 6, #08-111), "
                + "telegram (previously: @Alice_Pauline1) and tags (previously: [friends]) "
                + "for Alice Pauline (person at index 1)";

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(firstPerson, unsetPerson);
        expectedModel.setPersonToShow(unsetPerson);

        assertCommandSuccess(unsetCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        descriptor.setPhone(Optional.empty());
        final UnsetCommand standardCommand = new UnsetCommand(INDEX_FIRST_PERSON, descriptor);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(descriptor);
        UnsetCommand commandWithSameValues = new UnsetCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new UnsetCommand(INDEX_SECOND_PERSON, descriptor)));

        // different descriptor -> returns false
        EditPersonDescriptor differentDescriptor = new EditPersonDescriptor();
        differentDescriptor.setEmail(Optional.empty());
        assertFalse(standardCommand.equals(new UnsetCommand(INDEX_FIRST_PERSON, differentDescriptor)));
    }
}
