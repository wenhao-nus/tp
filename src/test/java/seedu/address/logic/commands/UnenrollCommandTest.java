package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.TutInfo;
import seedu.address.testutil.PersonBuilder;

public class UnenrollCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_unenrollUnfilteredList_success() {
        Person originalPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        TutInfo tutInfoToEnroll = new TutInfo("CS2103T", "T01");

        List<TutInfo> initialTutInfos = new ArrayList<>(originalPerson.getTutInfos());
        initialTutInfos.add(tutInfoToEnroll);
        Person personWithCourse = new PersonBuilder(originalPerson).withTutInfos(initialTutInfos).build();

        model.setPerson(originalPerson, personWithCourse);
        model.setPersonToShow(null);

        UnenrollCommand unenrollCommand = new UnenrollCommand(INDEX_FIRST_PERSON, "CS2103T");

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personWithCourse, originalPerson);
        expectedModel.setPersonToShow(originalPerson);

        String expectedMessage = String.format(UnenrollCommand.MESSAGE_SUCCESS, "CS2103T");

        assertCommandSuccess(unenrollCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getPersonToShow(), model.getPersonToShow());
    }

    @Test
    public void execute_notEnrolled_throwsCommandException() {
        model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String courseToRemove = "CS2103T";
        UnenrollCommand unenrollCommand = new UnenrollCommand(INDEX_FIRST_PERSON, courseToRemove);

        String expectedMessage = String.format(UnenrollCommand.MESSAGE_NOT_ENROLLED, courseToRemove);

        assertCommandFailure(unenrollCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        UnenrollCommand unenrollCommand = new UnenrollCommand(outOfBoundIndex, "CS2103T");

        assertCommandFailure(unenrollCommand, model, Messages.MESSAGE_INDEX_OUT_OF_BOUNDS);
    }

    @Test
    public void equals() {
        String course1 = "CS2103T";
        String course2 = "CS2109S";
        UnenrollCommand unenrollFirstCommand = new UnenrollCommand(INDEX_FIRST_PERSON, course1);
        UnenrollCommand unenrollSecondCommand = new UnenrollCommand(INDEX_SECOND_PERSON, course1);
        UnenrollCommand unenrollDiffCourseCommand = new UnenrollCommand(INDEX_FIRST_PERSON, course2);

        // same object -> returns true
        assertTrue(unenrollFirstCommand.equals(unenrollFirstCommand));

        // same values -> returns true
        UnenrollCommand unenrollFirstCommandCopy = new UnenrollCommand(INDEX_FIRST_PERSON, course1);
        assertTrue(unenrollFirstCommand.equals(unenrollFirstCommandCopy));

        // different types -> returns false
        assertFalse(unenrollFirstCommand.equals(1));

        // null -> returns false
        assertFalse(unenrollFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(unenrollFirstCommand.equals(unenrollSecondCommand));

        // different course -> returns false
        assertFalse(unenrollFirstCommand.equals(unenrollDiffCourseCommand));
    }
}
