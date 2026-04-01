package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Telegram;
import seedu.address.model.person.TutInfo;
import seedu.address.model.tag.Tag;

/**
 * Unsets the fields of an existing person in the address book.
 */
public class UnsetCommand extends Command {

    public static final String COMMAND_WORD = "unset";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Unsets the fields of the person identified "
            + "by the index number used in the displayed person list. "
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_PHONE + "] "
            + "[" + PREFIX_EMAIL + "] "
            + "[" + PREFIX_ADDRESS + "] "
            + "[" + PREFIX_TELEGRAM + "] "
            + "[" + PREFIX_TAG + "]...\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_PHONE + " " + PREFIX_TELEGRAM;

    public static final String MESSAGE_UNSET_PERSON_SUCCESS = "Unset %1$s for %2$s (person at index %3$s)";
    public static final String MESSAGE_NOT_UNSET = "At least one field to unset must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "Email must be unique! "
            + "This email already exists in the address book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to unset fields
     * @param editPersonDescriptor details to unset the person with
     */
    public UnsetCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INDEX_OUT_OF_BOUNDS);
        }

        Person personToUnset = lastShownList.get(index.getZeroBased());
        Person unsetPropertyPerson = createUnsetPerson(personToUnset, editPersonDescriptor);

        if (!personToUnset.isSamePerson(unsetPropertyPerson) && model.hasPerson(unsetPropertyPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToUnset, unsetPropertyPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.setPersonToShow(unsetPropertyPerson); // Always show the unset person

        return new CommandResult(getSuccessMessage(personToUnset, editPersonDescriptor, index));
    }

    /**
     * Creates a success message for unsetting fields.
     */
    private static String getSuccessMessage(Person personToUnset, EditPersonDescriptor editPersonDescriptor,
                                            Index index) {
        List<String> unsetFields = new ArrayList<>();
        editPersonDescriptor.getPhone().ifPresent(p ->
                unsetFields.add("phone (previously: " + personToUnset.getDisplayPhone() + ")"));
        editPersonDescriptor.getEmail().ifPresent(e ->
                unsetFields.add("email (previously: " + personToUnset.getDisplayEmail() + ")"));
        editPersonDescriptor.getAddress().ifPresent(a ->
                unsetFields.add("address (previously: " + personToUnset.getDisplayAddress() + ")"));
        editPersonDescriptor.getTelegram().ifPresent(t ->
                unsetFields.add("telegram (previously: " + personToUnset.getDisplayTelegram() + ")"));
        editPersonDescriptor.getTags().ifPresent(t -> {
            StringBuilder tagBuilder = new StringBuilder();
            personToUnset.getTags().forEach(tagBuilder::append);
            unsetFields.add("tags (previously: " + (tagBuilder.length() == 0 ? "none" : tagBuilder.toString()) + ")");
        });

        String fieldsPart = String.join(", ", unsetFields);
        int lastCommaIndex = fieldsPart.lastIndexOf(", ");
        if (lastCommaIndex != -1) {
            fieldsPart = fieldsPart.substring(0, lastCommaIndex) + " and " + fieldsPart.substring(lastCommaIndex + 2);
        }

        return String.format(MESSAGE_UNSET_PERSON_SUCCESS, fieldsPart, personToUnset.getName(), index.getOneBased());
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToUnset}
     * unset with {@code editPersonDescriptor}.
     */
    private static Person createUnsetPerson(Person personToUnset, EditPersonDescriptor editPersonDescriptor) {
        assert personToUnset != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToUnset.getName());

        Optional<Phone> updatedPhone = editPersonDescriptor.getPhone().orElse(personToUnset.getPhone());
        Optional<Email> updatedEmail = editPersonDescriptor.getEmail().orElse(personToUnset.getEmail());
        Optional<Address> updatedAddress = editPersonDescriptor.getAddress().orElse(personToUnset.getAddress());
        Optional<Telegram> updatedTelegram = editPersonDescriptor.getTelegram().orElse(personToUnset.getTelegram());

        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToUnset.getTags());
        List<TutInfo> tutInfos = personToUnset.getTutInfos();

        return new Person(
                updatedName,
                updatedPhone,
                updatedEmail,
                updatedAddress,
                updatedTelegram,
                updatedTags,
                tutInfos
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnsetCommand)) {
            return false;
        }

        UnsetCommand otherUnsetCommand = (UnsetCommand) other;
        return index.equals(otherUnsetCommand.index)
                && editPersonDescriptor.equals(otherUnsetCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }
}
