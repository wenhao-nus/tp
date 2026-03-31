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

public class UnattendCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_unattendUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String inputCourse1 = "cs1101s";
        String inputCourse2 = "cs2103t";
        String expectedCourse2 = "CS2103T";
        int week = 1;

        // Setup: Enrol the person in two courses
        TutInfo tutInfo1 = new TutInfo(inputCourse1, "t01");
        TutInfo tutInfo2 = new TutInfo(inputCourse2, "t02").setAttendance(week, true);
        List<TutInfo> tutInfos = new ArrayList<>();
        tutInfos.add(tutInfo1);
        tutInfos.add(tutInfo2);

        Person personWithCourses = new PersonBuilder(personToEdit).withTutInfos(tutInfos).build();
        model.setPerson(personToEdit, personWithCourses);
        model.setPersonToShow(null);

        // Unattend the second course
        UnattendCommand unattendCommand = new UnattendCommand(INDEX_FIRST_PERSON, expectedCourse2, week);

        TutInfo expectedTutInfo2 = tutInfo2.setAttendance(week, false);
        List<TutInfo> expectedTutInfos = new ArrayList<>();
        expectedTutInfos.add(tutInfo1);
        expectedTutInfos.add(expectedTutInfo2);
        Person expectedPerson = new PersonBuilder(personWithCourses).withTutInfos(expectedTutInfos).build();

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personWithCourses, expectedPerson);
        expectedModel.setPersonToShow(expectedPerson); // Always show the unattended person

        String expectedMessage = String.format(UnattendCommand.MESSAGE_SUCCESS, INDEX_FIRST_PERSON.getOneBased(),
                personWithCourses.getName(), expectedCourse2, "T02", week);

        assertCommandSuccess(unattendCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getPersonToShow(), model.getPersonToShow());
    }

    @Test
    public void execute_unattendMultipleCourses_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String course1 = "CS1101S";
        String course2 = "CS2103T";
        int week = 1;

        // Setup: Enrol the person in two courses
        TutInfo tutInfo1 = new TutInfo(course1, "T01");
        TutInfo tutInfo2 = new TutInfo(course2, "T02").setAttendance(week, true);
        List<TutInfo> tutInfos = new ArrayList<>();
        tutInfos.add(tutInfo1);
        tutInfos.add(tutInfo2);

        Person personWithCourses = new PersonBuilder(personToEdit).withTutInfos(tutInfos).build();
        model.setPerson(personToEdit, personWithCourses);
        model.setPersonToShow(null);

        // Unattend the second course
        UnattendCommand unattendCommand = new UnattendCommand(INDEX_FIRST_PERSON, course2, week);

        TutInfo expectedTutInfo2 = tutInfo2.setAttendance(week, false);
        List<TutInfo> expectedTutInfos = new ArrayList<>();
        expectedTutInfos.add(tutInfo1);
        expectedTutInfos.add(expectedTutInfo2);
        Person expectedPerson = new PersonBuilder(personWithCourses).withTutInfos(expectedTutInfos).build();

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personWithCourses, expectedPerson);
        expectedModel.setPersonToShow(expectedPerson); // Always show the unattended person

        String expectedMessage = String.format(UnattendCommand.MESSAGE_SUCCESS, INDEX_FIRST_PERSON.getOneBased(),
                personWithCourses.getName(), course2, "T02", week);

        assertCommandSuccess(unattendCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getPersonToShow(), model.getPersonToShow());
    }

    @Test
    public void execute_courseNotFound_throwsCommandException() {
        String courseCode = "CS2103T";
        int week = 1;
        UnattendCommand unattendCommand = new UnattendCommand(INDEX_FIRST_PERSON, courseCode, week);
        String expectedMessage = String.format(UnattendCommand.MESSAGE_COURSE_NOT_FOUND, courseCode);

        assertCommandFailure(unattendCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        UnattendCommand unattendCommand = new UnattendCommand(outOfBoundIndex, "CS2103T", 1);

        assertCommandFailure(unattendCommand, model, Messages.MESSAGE_INDEX_OUT_OF_BOUNDS);
    }

    @Test
    public void equals() {
        UnattendCommand unattendFirstCommand = new UnattendCommand(INDEX_FIRST_PERSON, "CS2103T", 1);
        UnattendCommand unattendSecondCommand = new UnattendCommand(INDEX_SECOND_PERSON, "CS2103T", 1);
        UnattendCommand unattendThirdCommand = new UnattendCommand(INDEX_FIRST_PERSON, "CS1231S", 1);
        UnattendCommand unattendFourthCommand = new UnattendCommand(INDEX_FIRST_PERSON, "CS2103T", 2);

        // same object -> returns true
        assertTrue(unattendFirstCommand.equals(unattendFirstCommand));

        // same values -> returns true
        UnattendCommand unattendFirstCommandCopy = new UnattendCommand(INDEX_FIRST_PERSON, "CS2103T", 1);
        assertTrue(unattendFirstCommand.equals(unattendFirstCommandCopy));

        // different types -> returns false
        assertFalse(unattendFirstCommand.equals(1));

        // null -> returns false
        assertFalse(unattendFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(unattendFirstCommand.equals(unattendSecondCommand));

        // different course code -> returns false
        assertFalse(unattendFirstCommand.equals(unattendThirdCommand));

        // different week -> returns false
        assertFalse(unattendFirstCommand.equals(unattendFourthCommand));
    }
}
