package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Unsets a single optional field of an existing person in the address book.
 */
public class UnsetCommand extends Command {

    public static final String COMMAND_WORD = "unset";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Unsets exactly one optional field of the person "
            + "identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) [OPTIONAL_FIELD_PREFIX] "
            + "Optional fields: " + PREFIX_PHONE + " " + PREFIX_ADDRESS + " "
            + PREFIX_TELEGRAM + " " + PREFIX_TAG + "\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_TELEGRAM;

    public static final String MESSAGE_NOT_UNSET = "Exactly one field to unset must be provided.";
    public static final String MESSAGE_MULTIPLE_FIELDS = "Only one field can be unset at a time.";
    public static final String MESSAGE_NAME_CANNOT_BE_UNSET = "Name is a mandatory field and cannot be unset.";
    public static final String MESSAGE_EMAIL_CANNOT_BE_UNSET = "Email is a mandatory field and cannot be unset.";
    public static final String MESSAGE_TUTINFO_CANNOT_BE_UNSET =
        "Tutorials or courses cannot be removed using 'unset'.\n"
        + "Tips: Use 'unenroll' to remove a course, which also removes its tutorial.";
    public static final String MESSAGE_FIELD_VALUE_NOT_ALLOWED =
            "Unset only accepts a field prefix with no value. Example: unset 1 tg/";
    public static final String MESSAGE_UNSET_SUCCESS =
            "Successfully unset %1$s (Previously: %2$s) for %3$s at index %4$s.";
    public static final String MESSAGE_FIELD_ALREADY_MISSING =
            "Note: %1$s for %2$s at index %3$s is already missing; No changes were made.";

    private final Index index;
    private final Prefix fieldPrefix;

    /**
     * Creates an UnsetCommand to unset the field identified by {@code fieldPrefix}
     * of the person at {@code index}.
     */
    public UnsetCommand(Index index, Prefix fieldPrefix) {
        requireNonNull(index);
        requireNonNull(fieldPrefix);
        this.index = index;
        this.fieldPrefix = fieldPrefix;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INDEX_OUT_OF_BOUNDS);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        String previousValue = getDisplayValue(personToEdit, fieldPrefix);
        Person editedPerson = unsetField(personToEdit, fieldPrefix);

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.setPersonToShow(editedPerson);

        // Unsetting an already missing optional field
        if (!isFieldSet(personToEdit, fieldPrefix)) {
            return new CommandResult(String.format(MESSAGE_FIELD_ALREADY_MISSING,
                    capitaliseFirstLetter(getDisplayName(fieldPrefix)), personToEdit.getName(), index.getOneBased()));
        }

        return new CommandResult(String.format(MESSAGE_UNSET_SUCCESS,
                getDisplayName(fieldPrefix), previousValue, personToEdit.getName(), index.getOneBased()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UnsetCommand)) {
            return false;
        }

        UnsetCommand otherUnsetCommand = (UnsetCommand) other;
        return index.equals(otherUnsetCommand.index) && fieldPrefix.equals(otherUnsetCommand.fieldPrefix);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("fieldPrefix", fieldPrefix)
                .toString();
    }

    /**
     * Returns whether the field identified by {@code fieldPrefix} is currently set.
     */
    private boolean isFieldSet(Person person, Prefix fieldPrefix) {
        switch (fieldPrefix.getPrefix()) {
        case "p/":
            return person.getPhone().isPresent();
        case "a/":
            return person.getAddress().isPresent();
        case "tg/":
            return person.getTelegram().isPresent();
        case "t/":
            return !person.getTags().isEmpty();
        default:
            throw new IllegalArgumentException("Unsupported unset field prefix: " + fieldPrefix);
        }
    }

    /**
     * Returns the user-facing name of the field identified by {@code fieldPrefix}.
     */
    private String getDisplayName(Prefix fieldPrefix) {
        switch (fieldPrefix.getPrefix()) {
        case "p/":
            return "phone number";
        case "a/":
            return "address";
        case "tg/":
            return "telegram";
        case "t/":
            return "tag";
        default:
            throw new IllegalArgumentException("Unsupported unset field prefix: " + fieldPrefix);
        }
    }

    /**
     * Returns the display value of the field identified by {@code fieldPrefix}.
     */
    private String getDisplayValue(Person person, Prefix fieldPrefix) {
        switch (fieldPrefix.getPrefix()) {
        case "p/":
            return person.getDisplayPhone();
        case "a/":
            return person.getDisplayAddress();
        case "tg/":
            return person.getDisplayTelegram();
        case "t/":
            return person.getTags().stream()
                    .sorted(Comparator.comparing(tag -> tag.tagName))
                    .map(Tag::toString)
                    .collect(Collectors.joining(" "));
        default:
            throw new IllegalArgumentException("Unsupported unset field prefix: " + fieldPrefix);
        }
    }

    /**
     * Returns a copy of {@code person} with the field identified by {@code fieldPrefix} unset.
     */
    private Person unsetField(Person person, Prefix fieldPrefix) {
        switch (fieldPrefix.getPrefix()) {
        case "p/":
            return new Person(person.getName(), Optional.empty(), person.getEmail(),
                    person.getAddress(), person.getTelegram(), person.getTags(), person.getTutInfos());
        case "a/":
            return new Person(person.getName(), person.getPhone(), person.getEmail(),
                    Optional.empty(), person.getTelegram(), person.getTags(), person.getTutInfos());
        case "tg/":
            return new Person(person.getName(), person.getPhone(), person.getEmail(),
                    person.getAddress(), Optional.empty(), person.getTags(), person.getTutInfos());
        case "t/":
            return new Person(person.getName(), person.getPhone(), person.getEmail(),
                    person.getAddress(), person.getTelegram(), Collections.emptySet(), person.getTutInfos());
        default:
            throw new IllegalArgumentException("Unsupported unset field prefix: " + fieldPrefix);
        }
    }

    /**
     * Capatilise the first letter of the string given.
     */
    private String capitaliseFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
