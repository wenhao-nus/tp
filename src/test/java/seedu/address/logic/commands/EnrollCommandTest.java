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

public class EnrollCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_enrollUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        TutInfo validTutInfo = new TutInfo("CS2103T", "T01");
        EnrollCommand enrollCommand = new EnrollCommand(INDEX_FIRST_PERSON, validTutInfo);

        List<TutInfo> expectedTutInfos = new ArrayList<>(personToEdit.getTutInfos());
        expectedTutInfos.add(validTutInfo);
        Person expectedPerson = new PersonBuilder(personToEdit).withTutInfos(expectedTutInfos).build();

        model.setPersonToShow(null);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, expectedPerson);
        expectedModel.setPersonToShow(expectedPerson);

        String expectedMessage = String.format(EnrollCommand.MESSAGE_SUCCESS,
                validTutInfo.getCourseCode(), validTutInfo.getTutorialCode());

        assertCommandSuccess(enrollCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getPersonToShow(), model.getPersonToShow());
    }

    @Test
    public void execute_duplicateCourse_throwsCommandException() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        TutInfo validTutInfo = new TutInfo("CS2103T", "T01");

        List<TutInfo> initialTutInfos = new ArrayList<>();
        initialTutInfos.add(validTutInfo);
        Person personWithCourse = new PersonBuilder(personToEdit).withTutInfos(initialTutInfos).build();
        model.setPerson(personToEdit, personWithCourse);

        EnrollCommand enrollCommand = new EnrollCommand(INDEX_FIRST_PERSON, validTutInfo);
        String expectedMessage = String.format(EnrollCommand.MESSAGE_DUPLICATE_COURSE, validTutInfo.getCourseCode());

        assertCommandFailure(enrollCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EnrollCommand enrollCommand = new EnrollCommand(outOfBoundIndex, new TutInfo("CS2103T", "T01"));

        assertCommandFailure(enrollCommand, model, Messages.MESSAGE_INDEX_OUT_OF_BOUNDS);
    }

    @Test
    public void equals() {
        TutInfo tutInfo1 = new TutInfo("CS2103T", "T01");
        TutInfo tutInfo2 = new TutInfo("CS2109S", "T02");
        EnrollCommand enrollFirstCommand = new EnrollCommand(INDEX_FIRST_PERSON, tutInfo1);
        EnrollCommand enrollSecondCommand = new EnrollCommand(INDEX_SECOND_PERSON, tutInfo1);
        EnrollCommand enrollDiffCourseCommand = new EnrollCommand(INDEX_FIRST_PERSON, tutInfo2);

        // same object -> returns true
        assertTrue(enrollFirstCommand.equals(enrollFirstCommand));

        // same values -> returns true
        EnrollCommand enrollFirstCommandCopy = new EnrollCommand(INDEX_FIRST_PERSON, tutInfo1);
        assertTrue(enrollFirstCommand.equals(enrollFirstCommandCopy));

        // different types -> returns false
        assertFalse(enrollFirstCommand.equals(1));

        // null -> returns false
        assertFalse(enrollFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(enrollFirstCommand.equals(enrollSecondCommand));

        // different course -> returns false
        assertFalse(enrollFirstCommand.equals(enrollDiffCourseCommand));
    }
}
