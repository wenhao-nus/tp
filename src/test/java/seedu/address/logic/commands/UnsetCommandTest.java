package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code UnsetCommand}.
 */
public class UnsetCommandTest {
    private static final String INDEX_OUT_OF_BOUNDS_MESSAGES = String.format(
                    Messages.MESSAGE_INDEX_OUT_OF_BOUNDS + "\n%s", UnsetCommand.MESSAGE_USAGE);

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_unsetPhoneUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personToEdit).withoutPhone().build();

        UnsetCommand unsetCommand = new UnsetCommand(INDEX_FIRST_PERSON, CliSyntax.PREFIX_PHONE);

        String expectedMessage = String.format(UnsetCommand.MESSAGE_UNSET_SUCCESS,
                "phone number", "94351253", personToEdit.getName(), INDEX_FIRST_PERSON.getOneBased());

        model.setPersonToShow(null);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);
        expectedModel.setPersonToShow(editedPerson);

        assertCommandSuccess(unsetCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getPersonToShow(), model.getPersonToShow());
    }

    @Test
    public void execute_unsetAddressUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_THIRD_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personToEdit).withoutAddress().build();

        UnsetCommand unsetCommand = new UnsetCommand(INDEX_THIRD_PERSON, CliSyntax.PREFIX_ADDRESS);

        String expectedMessage = String.format(UnsetCommand.MESSAGE_UNSET_SUCCESS,
                "address", "wall street", personToEdit.getName(), INDEX_THIRD_PERSON.getOneBased());

        model.setPersonToShow(null);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);
        expectedModel.setPersonToShow(editedPerson);

        assertCommandSuccess(unsetCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getPersonToShow(), model.getPersonToShow());
    }

    @Test
    public void execute_unsetTelegramUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personToEdit).withoutTelegram().build();

        UnsetCommand unsetCommand = new UnsetCommand(INDEX_FIRST_PERSON, CliSyntax.PREFIX_TELEGRAM);

        String expectedMessage = String.format(UnsetCommand.MESSAGE_UNSET_SUCCESS,
                "telegram", "@Alice_Pauline1", personToEdit.getName(), INDEX_FIRST_PERSON.getOneBased());

        model.setPersonToShow(null);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);
        expectedModel.setPersonToShow(editedPerson);

        assertCommandSuccess(unsetCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getPersonToShow(), model.getPersonToShow());
    }

    @Test
    public void execute_unsetTagsUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personToEdit).withTags().build();

        UnsetCommand unsetCommand = new UnsetCommand(INDEX_SECOND_PERSON, CliSyntax.PREFIX_TAG);

        String expectedMessage = String.format(UnsetCommand.MESSAGE_UNSET_SUCCESS,
                "tag", "[friends] [owesMoney]", personToEdit.getName(), INDEX_SECOND_PERSON.getOneBased());

        model.setPersonToShow(null);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);
        expectedModel.setPersonToShow(editedPerson);

        assertCommandSuccess(unsetCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getPersonToShow(), model.getPersonToShow());
    }

    @Test
    public void execute_phoneAlreadyMissing_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personWithoutPhone = new PersonBuilder(personToEdit).withoutPhone().build();
        model.setPerson(personToEdit, personWithoutPhone);

        UnsetCommand unsetCommand = new UnsetCommand(INDEX_FIRST_PERSON, CliSyntax.PREFIX_PHONE);

        String expectedMessage = String.format(
                UnsetCommand.MESSAGE_FIELD_ALREADY_MISSING,
                "Phone number", personWithoutPhone.getName(), INDEX_FIRST_PERSON.getOneBased()
        );

        model.setPersonToShow(null);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPersonToShow(personWithoutPhone);

        assertCommandSuccess(unsetCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getPersonToShow(), model.getPersonToShow());
    }

    @Test
    public void execute_addressAlreadyMissing_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personWithoutAddress = new PersonBuilder(personToEdit).withoutAddress().build();
        model.setPerson(personToEdit, personWithoutAddress);

        UnsetCommand unsetCommand = new UnsetCommand(INDEX_FIRST_PERSON, CliSyntax.PREFIX_ADDRESS);

        String expectedMessage = String.format(
                UnsetCommand.MESSAGE_FIELD_ALREADY_MISSING,
                "Address", personWithoutAddress.getName(), INDEX_FIRST_PERSON.getOneBased()
        );

        model.setPersonToShow(null);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPersonToShow(personWithoutAddress);

        assertCommandSuccess(unsetCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getPersonToShow(), model.getPersonToShow());
    }

    @Test
    public void execute_telegramAlreadyMissing_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personWithoutTelegram = new PersonBuilder(personToEdit).withoutTelegram().build();
        model.setPerson(personToEdit, personWithoutTelegram);

        UnsetCommand unsetCommand = new UnsetCommand(INDEX_FIRST_PERSON, CliSyntax.PREFIX_TELEGRAM);

        String expectedMessage = String.format(
                UnsetCommand.MESSAGE_FIELD_ALREADY_MISSING,
                "Telegram", personWithoutTelegram.getName(), INDEX_FIRST_PERSON.getOneBased()
        );

        model.setPersonToShow(null);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPersonToShow(personWithoutTelegram);

        assertCommandSuccess(unsetCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getPersonToShow(), model.getPersonToShow());
    }

    @Test
    public void execute_tagsAlreadyMissing_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personWithoutTags = new PersonBuilder(personToEdit).withoutTags().build();
        model.setPerson(personToEdit, personWithoutTags);

        UnsetCommand unsetCommand = new UnsetCommand(INDEX_FIRST_PERSON, CliSyntax.PREFIX_TAG);

        String expectedMessage = String.format(
                UnsetCommand.MESSAGE_FIELD_ALREADY_MISSING,
                "Tag", personWithoutTags.getName(), INDEX_FIRST_PERSON.getOneBased()
        );

        model.setPersonToShow(null);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPersonToShow(personWithoutTags);

        assertCommandSuccess(unsetCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getPersonToShow(), model.getPersonToShow());
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        UnsetCommand unsetCommand = new UnsetCommand(outOfBoundIndex, CliSyntax.PREFIX_EMAIL);

        assertCommandFailure(unsetCommand, model, INDEX_OUT_OF_BOUNDS_MESSAGES);
    }

    @Test
    public void execute_invalidFieldPrefix_throwsIllegalArgumentException() {
        UnsetCommand unsetCommand = new UnsetCommand(INDEX_FIRST_PERSON, new Prefix("x/"));

        assertThrows(IllegalArgumentException.class, "Unsupported unset field prefix: x/", () ->
                unsetCommand.execute(model));
    }

    @Test
    public void execute_emptyList_failure() {
        model.updateFilteredPersonList(p -> false);
        UnsetCommand unsetCommand = new UnsetCommand(INDEX_FIRST_PERSON, new Prefix("p/"));

        assertCommandFailure(unsetCommand, model,
                String.format(Messages.MESSAGE_EMPTY_DISPLAYED_LIST, UnsetCommand.MESSAGE_USAGE));
    }

    @Test
    public void equals() {
        UnsetCommand unsetFirstCommand = new UnsetCommand(INDEX_FIRST_PERSON, CliSyntax.PREFIX_TELEGRAM);
        UnsetCommand unsetSecondCommand = new UnsetCommand(INDEX_SECOND_PERSON, CliSyntax.PREFIX_TELEGRAM);
        UnsetCommand unsetDifferentFieldCommand = new UnsetCommand(INDEX_FIRST_PERSON, CliSyntax.PREFIX_ADDRESS);

        assertTrue(unsetFirstCommand.equals(unsetFirstCommand));

        UnsetCommand unsetFirstCommandCopy = new UnsetCommand(INDEX_FIRST_PERSON, CliSyntax.PREFIX_TELEGRAM);
        assertTrue(unsetFirstCommand.equals(unsetFirstCommandCopy));

        assertFalse(unsetFirstCommand.equals(null));
        assertFalse(unsetFirstCommand.equals(1));
        assertFalse(unsetFirstCommand.equals(unsetSecondCommand));
        assertFalse(unsetFirstCommand.equals(unsetDifferentFieldCommand));
    }

    @Test
    public void toStringMethod() {
        UnsetCommand unsetCommand = new UnsetCommand(INDEX_FIRST_PERSON, CliSyntax.PREFIX_PHONE);
        String expected = UnsetCommand.class.getCanonicalName() + "{index=" + INDEX_FIRST_PERSON
                + ", fieldPrefix=p/}";
        assertEquals(expected, unsetCommand.toString());
    }
}
