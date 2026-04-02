package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using its index from the displayed list in the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer). "
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Successfully deleted the following contact:\n"
            + "Deleted Person: %1$s";

    public static final String MESSAGE_DELETE_INDEX_ERROR = "Error deleting contact: ";

    private final Index targetIndex;

    /**
     * Creates a {@code DeleteCommand} to delete the {@code Person} at given {@code targetIndex}.
     *
     * @param targetIndex Index of the person in the displayed list to be deleted.
     */
    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (isIndexOutOfBounds(lastShownList)) {
            throw new CommandException(String.format(
                    MESSAGE_DELETE_INDEX_ERROR + Messages.MESSAGE_INDEX_OUT_OF_BOUNDS + "\n%s",
                    MESSAGE_USAGE));
        }

        Person currentlyShown = model.getPersonToShow();

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deletePerson(personToDelete);

        // Clear the currently shown person first
        model.setPersonToShow(null);

        // Restore the currently viewed person if the deleted person was not the one being shown.
        if (!personToDelete.equals(currentlyShown)) {
            model.setPersonToShow(currentlyShown);
        }

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }

    /**
    * Returns true if the target index is outside the range of the displayed person list.
    */
    private boolean isIndexOutOfBounds(List<Person> list) {
        return targetIndex.getZeroBased() >= list.size();
    }
}
