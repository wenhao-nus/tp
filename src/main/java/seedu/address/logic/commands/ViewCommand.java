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
 * Views a person's full details in the expanded contact panel using its index
 * from the displayed list in the address book.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Displays the full details of the contact identified "
            + "by the index number in the displayed contact list.\n"
            + "Parameters: INDEX (must be a positive integer). "
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_VIEW_PERSON_SUCCESS =
            "Currently viewing the following full contact details:\n%1$s";
    
    public static final String MESSAGE_VIEW_SAME_PERSON_SUCCESS =
            "You are already viewing the following full contact details:\n%1$s";

    public static final String MESSAGE_VIEW_INDEX_ERROR = "Error viewing contact: ";

    private final Index targetIndex;

    /**
     * Creates a {@code ViewCommand} to view full details of the {@code Person} at given {@code targetIndex}.
     *
     * @param targetIndex Index of the person in the displayed list whose details will be shown.
     */
    public ViewCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();

        // Handles case of empty current displayed list for more specific error message
        if (lastShownList.isEmpty()) {
            throw new CommandException(String.format(
                    MESSAGE_VIEW_INDEX_ERROR + Messages.MESSAGE_EMPTY_DISPLAYED_LIST,
                    MESSAGE_USAGE));
        }

        // Handles cases of index exceeds number of contacts displayed
        if (isIndexOutOfBounds(lastShownList)) {
            throw new CommandException(String.format(
                    MESSAGE_VIEW_INDEX_ERROR + Messages.MESSAGE_INDEX_OUT_OF_BOUNDS + "\n%s",
                    MESSAGE_USAGE));
        }

        Person currentlyShown = model.getPersonToShow();
        Person personToView = lastShownList.get(targetIndex.getZeroBased());

        model.setPersonToShow(personToView);

        if (currentlyShown != null && currentlyShown.equals(personToView)) {
            return new CommandResult(String.format(MESSAGE_VIEW_SAME_PERSON_SUCCESS, Messages.format(personToView)));
        }

        return new CommandResult(String.format(MESSAGE_VIEW_PERSON_SUCCESS, Messages.format(personToView)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ViewCommand)) {
            return false;
        }

        ViewCommand otherViewCommand = (ViewCommand) other;
        return targetIndex.equals(otherViewCommand.targetIndex);
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
