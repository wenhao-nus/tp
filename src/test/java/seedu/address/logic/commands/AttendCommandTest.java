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

public class AttendCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_attendUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String courseCode = "CS2103T";
        int week = 1;
        // Setup: Enrol the person first
        TutInfo tutInfo = new TutInfo(courseCode, "T01");
        List<TutInfo> tutInfos = new ArrayList<>(personToEdit.getTutInfos());
        tutInfos.add(tutInfo);
        Person personWithCourse = new PersonBuilder(personToEdit).withTutInfos(tutInfos).build();
        model.setPerson(personToEdit, personWithCourse);
        model.setPersonToShow(null);

        AttendCommand attendCommand = new AttendCommand(INDEX_FIRST_PERSON, courseCode, week);

        TutInfo expectedTutInfo = tutInfo.setAttendance(week, true);
        List<TutInfo> expectedTutInfos = new ArrayList<>(tutInfos);
        expectedTutInfos.set(expectedTutInfos.indexOf(tutInfo), expectedTutInfo);
        Person expectedPerson = new PersonBuilder(personWithCourse).withTutInfos(expectedTutInfos).build();

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personWithCourse, expectedPerson);
        expectedModel.setPersonToShow(expectedPerson); // Always show the attended person

        String expectedMessage = String.format(AttendCommand.MESSAGE_SUCCESS, INDEX_FIRST_PERSON.getOneBased(),
                personWithCourse.getName(), courseCode, "T01", week);

        assertCommandSuccess(attendCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getPersonToShow(), model.getPersonToShow());
    }

    @Test
    public void execute_attendMultipleCourses_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String course1 = "CS1101S";
        String course2 = "CS2103T";
        int week = 1;

        // Setup: Enrol the person in two courses
        TutInfo tutInfo1 = new TutInfo(course1, "T01");
        TutInfo tutInfo2 = new TutInfo(course2, "T02");
        List<TutInfo> tutInfos = new ArrayList<>();
        tutInfos.add(tutInfo1);
        tutInfos.add(tutInfo2);

        Person personWithCourses = new PersonBuilder(personToEdit).withTutInfos(tutInfos).build();
        model.setPerson(personToEdit, personWithCourses);

        // Set third person full details shown before attending the first person
        model.setPersonToShow(model.getFilteredPersonList().get(2));

        // Attend the second course
        AttendCommand attendCommand = new AttendCommand(INDEX_FIRST_PERSON, course2, week);

        TutInfo expectedTutInfo2 = tutInfo2.setAttendance(week, true);
        List<TutInfo> expectedTutInfos = new ArrayList<>();
        expectedTutInfos.add(tutInfo1);
        expectedTutInfos.add(expectedTutInfo2);
        Person expectedPerson = new PersonBuilder(personWithCourses).withTutInfos(expectedTutInfos).build();

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personWithCourses, expectedPerson);
        expectedModel.setPersonToShow(expectedPerson); // Always show the attended person

        String expectedMessage = String.format(AttendCommand.MESSAGE_SUCCESS, INDEX_FIRST_PERSON.getOneBased(),
                personWithCourses.getName(), course2, "T02", week);

        assertCommandSuccess(attendCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getPersonToShow(), model.getPersonToShow());
    }

    @Test
    public void execute_courseNotFound_throwsCommandException() {
        String courseCode = "CS2103T";
        int week = 1;
        AttendCommand attendCommand = new AttendCommand(INDEX_FIRST_PERSON, courseCode, week);
        String expectedMessage = String.format(AttendCommand.MESSAGE_COURSE_NOT_FOUND, courseCode);

        assertCommandFailure(attendCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        AttendCommand attendCommand = new AttendCommand(outOfBoundIndex, "CS2103T", 1);

        assertCommandFailure(attendCommand, model, Messages.MESSAGE_INDEX_OUT_OF_BOUNDS);
    }

    @Test
    public void equals() {
        AttendCommand attendFirstCommand = new AttendCommand(INDEX_FIRST_PERSON, "CS2103T", 1);
        AttendCommand attendSecondCommand = new AttendCommand(INDEX_SECOND_PERSON, "CS2103T", 1);
        AttendCommand attendThirdCommand = new AttendCommand(INDEX_FIRST_PERSON, "CS1231S", 1);
        AttendCommand attendFourthCommand = new AttendCommand(INDEX_FIRST_PERSON, "CS2103T", 2);

        // same object -> returns true
        assertTrue(attendFirstCommand.equals(attendFirstCommand));

        // same values -> returns true
        AttendCommand attendFirstCommandCopy = new AttendCommand(INDEX_FIRST_PERSON, "CS2103T", 1);
        assertTrue(attendFirstCommand.equals(attendFirstCommandCopy));

        // different types -> returns false
        assertFalse(attendFirstCommand.equals(1));

        // null -> returns false
        assertFalse(attendFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(attendFirstCommand.equals(attendSecondCommand));

        // different course code -> returns false
        assertFalse(attendFirstCommand.equals(attendThirdCommand));

        // different week -> returns false
        assertFalse(attendFirstCommand.equals(attendFourthCommand));
    }
}
