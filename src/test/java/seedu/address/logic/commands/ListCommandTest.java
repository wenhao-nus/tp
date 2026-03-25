package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_nonEmptyListIsNotFiltered_showsSameList() {
        model.setPersonToShow(null);
        // Always shows the first person since list is not empty
        expectedModel.setPersonToShow(model.getFilteredPersonList().get(0));

        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
        assertEquals(expectedModel.getPersonToShow(), model.getPersonToShow());
    }

    @Test
    public void execute_nonEmptyListIsFiltered_showsEverything() {
        model.setPersonToShow(model.getFilteredPersonList().get(3));
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        // Always shows the first person since filtered list is not empty
        expectedModel.setPersonToShow(model.getFilteredPersonList().get(0));

        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
        assertEquals(expectedModel.getPersonToShow(), model.getPersonToShow());
    }

    @Test
    public void execute_emptyListAtStart_showsNothing() {
        model = new ModelManager();
        expectedModel = new ModelManager();

        model.setPersonToShow(null);
        // Always shows the first person since list is not empty
        expectedModel.setPersonToShow(null);

        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
        assertEquals(expectedModel.getPersonToShow(), model.getPersonToShow());
    }

}
